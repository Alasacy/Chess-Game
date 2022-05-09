package chessgame.entities;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import chessgame.app.Game;
import chessgame.entities.pawnstates.*;
import chessgame.utils.Constants;
import chessgame.utils.EntityManager;

public class Pawn implements IEnemies {
	int myId;
	int health;
	int attack;
	public float aggroRange = 6f;
	
	Vector2 position;
	public Vector2 homePosition;
	World world;
	Body myBody;
	public EntityManager entityManager;
	public Sprite sprite;
	
	boolean hasTakenDamage = false;
	float dmgTime = 0;
	
	//State	
	public PawnState idleState = new PawnIdle(this);
	public PawnState chaseState = new PawnChase(this);
	public PawnState homeState = new PawnHome(this);
	public PawnState moveState = new PawnMove(this);
	PawnState currentState = idleState;
	
	//Entity size
	float width = 0.5f;
	float height = 1f;
	float jumpSensorwidth = 0.5f;
	
	public Pawn (Vector2 position, World world, EntityManager entityManager, int id) {
		homePosition = new Vector2(position.x/Constants.PixelPerMeter+width, position.y/Constants.PixelPerMeter+height);
		this.position = homePosition;
		this.world = world;
		this.entityManager = entityManager;
		myId = id;
		health = 2;
		attack = 1;
	}
	
	public void initialize() {
		sprite = new Sprite(new Texture (Gdx.files.internal("assets/pawn/darkPawn.png").file().getAbsolutePath()));
		createBody(); 
		
		//Adds the pawn to the entityManager
    	entityManager.addEntity(this);
	}
	
	@Override
	public void createBody() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(position.x, position.y));
		
		myBody = world.createBody(bodyDef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width, height);
		
		myBody.createFixture(shape, 1000f).setUserData("Enemy");;
		myBody.setFixedRotation(true);
		myBody.setUserData(this);
		
		//adding a weakpoint
		addNewBoxSensor(myBody, width * 0.95f, height / 3.5f, new Vector2(0f, height), "weakpoint");
		
		//adding a rightJumpSensor and leftJumpSensor to detect when the pawn needs to jump
		addNewBoxSensor(myBody, this.jumpSensorwidth, 0.05f, new Vector2(1f, -this.height/2), "rightJumpSensor");
		addNewBoxSensor(myBody, this.jumpSensorwidth, 0.05f, new Vector2(-1f, -this.height/2), "leftJumpSensor");
		addNewBoxSensor(myBody, this.jumpSensorwidth/20, 0.05f, new Vector2(1f, -this.height/2), "rightJumpSensor");
		addNewBoxSensor(myBody, this.jumpSensorwidth/20, 0.05f, new Vector2(-1f, -this.height/2), "leftJumpSensor");
	}
	
	public void removeBody() {
		world.destroyBody(myBody);
	}

	@Override
	public Vector2 getPosition() {
		return position;
	}

	@Override
	public void move(Vector2 newPos) {
		myBody.setTransform(newPos, 0f);
	}

	@Override
	public Sprite getSprite() {
		return sprite;
	}

	@Override
	public int getHealth() {
		return health;
	}
	
	public void setHealth(int health) {
        this.health = health;
    }

	@Override
	public void takeDamage(int damage) {
		dmgTime = 0;
		hasTakenDamage = true;
		if(damage <= health)
			health -= damage;
		else {
			kill();
			return;
		}
	}

	@Override
	public int getAttack() {
		return attack;
	}

	public int getId() {
        return myId;
    }

	@Override
	public void moveTo(Vector2 target) {
		float xVal = position.x - target.x;
		float yVal = position.y - target.y;
		
		if(xVal > 0) {
			myBody.setLinearVelocity(-3, myBody.getLinearVelocity().y);
		} else {
			myBody.setLinearVelocity(3, myBody.getLinearVelocity().y);
		}
	}

	@Override
	public void updateState(Batch batch) {
		
		currentState.Update();
		
		keepWithinBounds();
		position = myBody.getPosition();
		if(batch != null) {
			dmgColorTime(Color.RED, 0.25f);
			sprite.setPosition(position.x - sprite.getWidth()/2 , position.y - sprite.getHeight()/2);
			sprite.setSize(2, 2);
			sprite.draw(batch);	
		}
		if(health <= 0)
			kill();		
	}

	@Override
	public void kill() {
		entityManager.removeEntity(this);
	}

	@Override
	public Body getBody() {
		return myBody;
	}
	
	public void keepWithinBounds() {
		if(myBody.getPosition().x > Game.mapSize.x-width) {
			myBody.setTransform(new Vector2(Game.mapSize.x-width, myBody.getPosition().y), 0f);
		}
		else if(myBody.getPosition().x < (0+width)) {
			myBody.setTransform(new Vector2(0+width, myBody.getPosition().y), 0f);
		}
		if(myBody.getPosition().y < 0) {
			kill();
		}
	}

	@Override
	public Player getClosestPlayer(Float dist) {
		List<Player> playerList = entityManager.playerList;
		Player target = null;
		
		float finalDist = -1;
		for(Player player : playerList) {
			float distance = (position.dst2(player.getPosition().x, player.getPosition().y)/Constants.PixelPerMeter);
			if((target == null && distance <= dist)) {
				target = player;
				finalDist = distance;
			} else if((distance <= dist && distance < finalDist)) {
				target = player;
				finalDist = distance;
			}
		}
		return target;
	}
	
	/**
	 * Changes between the states in the stateMachine
	 */
	public void changeState(PawnState state) {
		currentState = state;
		currentState.Enter();
	}

	@Override
	public IState getCurrentState() {
		return currentState;
	}
	
	/**
	 * Method to give an existing body a new box-sensor
	 * @param thisBody - the body to add the sensor to
	 * @param length - the length of the sensor
	 * @param height - the height of the sensor
	 * @param centerPosition - the position of the box, relative to the body
	 * @param userData - the user-data to add to the added shape
	 */
	void addNewBoxSensor(Body thisBody, float length, float height, Vector2 centerPosition, String userData) {
		PolygonShape shape = new PolygonShape();
		//creating a fixture that will serve as a sensor for the pawn
		FixtureDef fixDef = new FixtureDef();
		fixDef.isSensor = true;
		shape.setAsBox(length, height, centerPosition, 0);
		fixDef.shape = shape;
		
		myBody.createFixture(fixDef).setUserData(userData);
	}
	
	public void dmgColorTime(Color color, float time) {
		if(dmgTime > time && hasTakenDamage) {
			sprite.setColor(Color.WHITE);
			hasTakenDamage = false;
		} else if(hasTakenDamage) {
			sprite.setColor(color);
			dmgTime += Gdx.graphics.getDeltaTime();
		}
	}

	@Override
	public void jump() {
		myBody.setLinearVelocity(new Vector2(myBody.getLinearVelocity().x, 20f));
		
	}
}
