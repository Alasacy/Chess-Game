package chessgame.entities;

import java.util.ArrayList;
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
import chessgame.entities.knightbossstates.KnightBossChase;
import chessgame.entities.knightbossstates.KnightBossDormant;
import chessgame.entities.knightbossstates.KnightBossHighJump;
import chessgame.entities.knightbossstates.KnightBossIdle;
import chessgame.entities.knightbossstates.KnightBossOmega;
import chessgame.entities.knightbossstates.KnightBossRandom;
import chessgame.entities.knightbossstates.KnightBossState;
import chessgame.entities.knightbossstates.KnightBossStunned;
import chessgame.entities.knightstates.*;
import chessgame.utils.Constants;
import chessgame.utils.Direction;
import chessgame.utils.EntityAnimation;
import chessgame.utils.EntityManager;
import chessgame.utils.EntityType;
import chessgame.utils.GameSound;
import chessgame.utils.HUD;
import chessgame.utils.Rumble;
import chessgame.world.PhysicsWorld;

public class KnightBoss implements IEnemies {
	int health;
	int attack;
	public float aggroRange = 35f;
	
	public boolean activated;
	Vector2 position;
	public Vector2 homePosition;
	public World world;
	Body myBody;
	public EntityManager entityManager;
	public Sprite sprite;
	
	boolean invisFrame;
	long hitTime;
	
	boolean canJump;
	long jumpTime;
	long thinkingTime;
	boolean grounded;
	public boolean lookingRight;
	float preXVal;
	public boolean hit;
	public int allJumps;
	public boolean spawnBullet;
	public boolean telegrafChase = false;
	
	public ArrayList<IEntities> minions = new ArrayList<IEntities>();
	
	//State	
	public KnightBossState superJumpState = new KnightBossHighJump(this);
	public KnightBossState idleState = new KnightBossIdle(this);
	public KnightBossState stunnedState = new KnightBossStunned(this);
	public KnightBossState chaseState = new KnightBossChase(this);
	public KnightBossState randState = new KnightBossRandom(this);
	public KnightBossState dormantState = new KnightBossDormant(this);
	public KnightBossState omegaState = new KnightBossOmega(this);
	KnightBossState currentState = dormantState;
	public KnightBossState prevState = stunnedState;
	
	//Entity size
	public float width = 0.8f*2.6f;
	public float height = 1.5f*2.6f;
	
	EntityAnimation eyeFlare;
	EntityAnimation chasingLeft;
	EntityAnimation chasingRight;
	

	public KnightBoss (Vector2 position, World world, EntityManager entityManager) {
		homePosition = new Vector2(position.x/Constants.PixelPerMeter+width, position.y/Constants.PixelPerMeter+height);
		this.position = homePosition;
		this.world = world;
		this.entityManager = entityManager;
		health = 5;
		attack = 1;
		lookingRight = false;
		grounded = false;
		allJumps = 0;
		activated = false;
		thinkingTime = 700;
		spawnBullet = false;
		
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
		addNewBoxSensor(myBody, width * 0.95f, height / 6f, new Vector2(0f, height), "weakpoint");
		addNewBoxSensor(myBody, width * 0.95f, height /16f, new Vector2(0f, -height), "hoof");
		addNewBoxSensor(myBody, width / 16f, height * 0.9f, new Vector2(-width, 0), "bumper");
		addNewBoxSensor(myBody, width / 16f, height * 0.9f, new Vector2(width, 0), "bumper");
	}
	
	

	@Override
	public Vector2 getPosition() {
		return position;
	}

	@Override
	public void move(Vector2 newPos) {
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
		if(entityManager.doorMap.containsKey(1)) {
			Door door = entityManager.doorMap.get(1);
			door.doorState();
		}
		GameSound.stopMusic();
		GameSound.playMusic(5);
		entityManager.removeEntity(this);
	}

	@Override
	public void removeBody() {
		world.destroyBody(myBody);
	}

	@Override
	public void updateState(Batch batch) {
		
		currentState.Update();
		
		
		
		keepWithinBounds();
		position = myBody.getPosition();
		if(batch != null) {
			sprite.setPosition(position.x - sprite.getWidth()/2 , position.y - sprite.getHeight()/2);
			sprite.setSize(1.6f*2.6f, 3.2f*2.6f);
			sprite.draw(batch);	
		}
		if(health <= 0)
			kill();
		
		if(telegrafChase) {
			telegrafChase = eyeFlare.playOnce(batch, (position.x - (sprite.getWidth()/2.1f)) , (position.y - ((sprite.getHeight()/2f)*(3f/2.65f))));
		}
		
		if(currentState == chaseState) {
			if (lookingRight)
				chasingRight.render(batch, (position.x - (sprite.getWidth()/2.1f)) , (position.y - ((sprite.getHeight()/2f)*(3f/2.65f)))+0.05f, false);
			else
				chasingLeft.render(batch, (position.x - (sprite.getWidth()/2.1f)) , (position.y - ((sprite.getHeight()/2f)*(3f/2.65f)))+0.05f, false);
		}
		if(System.currentTimeMillis() < hitTime + 100) {
			sprite.setColor(Color.RED);
			attack = 0;
		} else {
			sprite.setColor(Color.WHITE);
			attack = 1;
			hit = false;
		}
		
		if(System.currentTimeMillis() > jumpTime + thinkingTime && grounded) {
			canJump = true;
		}
	}

	@Override
	public void initialize() {
		sprite = new Sprite(new Texture (Gdx.files.internal("assets/enemies/KnightBoss.png").file().getAbsolutePath()));
		createBody();
		
		Texture flareSheet = new Texture (Gdx.files.internal("assets/enemies/KnightBossEyeFlare-Sheet.png").file().getAbsolutePath());
		eyeFlare = new EntityAnimation(flareSheet, 10, 20f, this, new Vector2(144, 288));
		Texture chasingLeftSheet = new Texture (Gdx.files.internal("assets/enemies/KnightBossChasing-Sheet-export.png").file().getAbsolutePath());
		chasingLeft = new EntityAnimation(chasingLeftSheet, 2, 6f, this, new Vector2(144, 288), true);
		Texture chasingRightSheet = new Texture (Gdx.files.internal("assets/enemies/KnightBossChasingRight-Sheet-export.png").file().getAbsolutePath());
		chasingRight = new EntityAnimation(chasingRightSheet, 2, 6f, this, new Vector2(144, 288), true);
		//Adds the knight to the entityManager
    	entityManager.addEntity(this);
    	currentState.Enter();
	}

	@Override
	public void moveTo(Vector2 target) {
		if (canJump) {
			float xVal = position.x - target.x;
			float yVal = target.y - position.y;
			float jumpPower = 32f;
			if (health == 1)
				jumpPower = 28f;
			if (yVal < -10f)
				jumpPower = 22f;
			
			if(xVal > 0) {
				if (lookingRight) {
					sprite.flip(true, false);
					lookingRight = false;
				}
				myBody.setLinearVelocity(-xVal, jumpPower);
			} else {
				if (!lookingRight) {
					sprite.flip(true, false);
					lookingRight = true;
				}
				myBody.setLinearVelocity(-xVal*1.5f, jumpPower);
			}
			
			preXVal = xVal;
			canJump = false;
			grounded = false;
			allJumps += 1;
		}	
	}
	
	public void grounded() {
		if (!grounded) {
			myBody.setLinearVelocity(Vector2.Zero);
			grounded = true;
			Rumble.rumble(1f, 0.4f);
			GameSound.playSoundEffect(1, 1);
			jumpTime = System.currentTimeMillis();
		}
	}
	public boolean isGrounded() {
		return grounded;
	}
	
	public void bump() {
		sprite.flip(true, false);
		if (lookingRight)
			lookingRight = false;
		else
			lookingRight = true;
		
		myBody.setLinearVelocity(new Vector2(-myBody.getLinearVelocity().x, myBody.getLinearVelocity().y));
	}
	
	@Override
	public int getHealth() {
		return health;
	}

	@Override
	public void takeDamage(int damage) {
		attack = 0;
		if(damage <= health)
			health -= damage;
		else {
			kill();
			return;
		}
		
		if(health ==1)
			thinkingTime = 0;
		else
			thinkingTime -= 120;
		
		invisFrame = true;
		hitTime = System.currentTimeMillis();
		hit = true;
		float xVal;

		List<Player> players = entityManager.playerList;
		for (Player player : players) {
			float targetDir = player.getPosition().x-getPosition().x;
			if (targetDir < 0)
				xVal = -80f;
			else
				xVal = 80f;
			player.myBody.setLinearVelocity(new Vector2(xVal, 15f));
		}
		
		HUD.BossHp(5, damage);
	}

	@Override
	public int getAttack() {
		return attack;
	}

	@Override
	public void keepWithinBounds() {
		if(myBody.getPosition().x >Game.mapSize.x-width) {
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
		return currentState;
	}
	
	/**
	 * Changes between the states in the stateMachine
	 */
	public void changeState(KnightBossState state) {
		prevState = currentState;
		currentState = state;
		currentState.Enter();
	}

	@Override
	public void jump() {
		canJump = false;
		grounded = false;
		myBody.setLinearVelocity(0f, 28f);
		allJumps += 1;
	}
	
	public void superJump(Float power, boolean shockWave) {
		if (canJump) {
			canJump = false;
			grounded = false;
			myBody.setLinearVelocity(0f, power);
			allJumps += 1;
			spawnBullet = true;
		}
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


	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
