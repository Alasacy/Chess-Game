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
import chessgame.entities.bishopStates.BishopFireState;
import chessgame.entities.bishopStates.BishopIdleState;
import chessgame.entities.bishopStates.BishopRetreatState;
import chessgame.entities.bishopStates.BishopState;
import chessgame.entities.pawnstates.PawnState;
import chessgame.utils.Constants;
import chessgame.utils.Direction;
import chessgame.utils.EntityManager;

public class Bishop implements IEnemies {
	
	int health;
	int attack;
	EntityManager entityManager;
	Body myBody;
	Vector2 position;
	World world;
	
	//States
	IState currentState;
	public BishopState bishopIdleState = new BishopIdleState(this);
	public BishopState bishopFireState = new BishopFireState(this);
	public BishopState bishopRetreatState = new BishopRetreatState(this);
	
	boolean hasTakenDamage = false;
	float dmgTime = 0;
	
	float width = .5f;
	float height = 1.5f;;
	
	Sprite sprite;
	public Texture bishopTex;
	public Texture bishopAttackTex;
	
	public Bishop(Vector2 position, World world, EntityManager entityManager) {
		this.position = new Vector2(position.x/Constants.PixelPerMeter+width, position.y/Constants.PixelPerMeter+height);
		this.world = world;
		this.entityManager = entityManager;
		
		health = 1;
		attack = 1;
	}
	
	@Override
	public void initialize() {
		bishopTex = new Texture (Gdx.files.internal("assets/enemies/bishops1.png").file().getAbsolutePath());
		bishopAttackTex = new Texture (Gdx.files.internal("assets/enemies/bishops2.png").file().getAbsolutePath());
		sprite = new Sprite(bishopTex);
		entityManager.addEntity(this);
		currentState = bishopIdleState;
		createBody();
	}
	
	@Override
	public void createBody() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(position.x, position.y));
		
		myBody = world.createBody(bodyDef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width, height);
		
		myBody.createFixture(shape, 1f).setUserData("Enemy");;
		myBody.setFixedRotation(true);
		myBody.setUserData(this);
		
		//adding a weakpoint
		addNewBoxSensor(myBody, width * 0.95f, height / 3.5f, new Vector2(0f, height), "weakpoint");
	}
	
	

	@Override
	public Vector2 getPosition() {
		return position;
	}

	@Override
	public void move(Vector2 newPos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Sprite getSprite() {
		return sprite;
	}

	@Override
	public Body getBody() {
		return myBody;
	}

	@Override
	public void kill() {
		entityManager.removeEntity(this);
	}

	@Override
	public void removeBody() {
		world.destroyBody(myBody);
	}

	@Override
	public void updateState(Batch batch) {
		keepWithinBounds();
		position = myBody.getPosition();
		currentState.Update();
		if(batch != null) {
			dmgColorTime(Color.RED, 0.25f);
			sprite.setPosition(position.x - sprite.getWidth()/2 , position.y - sprite.getHeight()/2);
			sprite.setSize(1, 3);
			sprite.draw(batch);	
		}
	}
	
	public void shootBullet(Player player) {
		Bullet b = new Bullet(this.position, world, entityManager, Direction.UP, player);
		b.initialize();
	}

	@Override
	public void moveTo(Vector2 target) {
		// TODO Auto-generated method stub
		
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
	
	/**
	 * Changes between the states in the stateMachine
	 */
	public void changeState(BishopState state) {
		currentState = state;
		currentState.Enter();
	}

	@Override
	public int getHealth() {
		return health;
	}

	@Override
	public void takeDamage(int damage) {
		dmgTime = 0;
		hasTakenDamage = true;
		if(damage < health)
			health -= damage;
		else {
			kill();
			return;
		}
	}
	
	public void dmgColorTime(Color color, float time) {
		if(dmgTime > time && hasTakenDamage) {
			//sprite.setColor(Color.WHITE);
			hasTakenDamage = false;
		} else if(hasTakenDamage) {
			//sprite.setColor(color);
			dmgTime += Gdx.graphics.getDeltaTime();
		}
	}

	@Override
	public int getAttack() {
		return attack;
	}

	@Override
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

	@Override
	public IState getCurrentState() {
		// TODO Auto-generated method stub
		return currentState;
	}

	@Override
	public void jump() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
