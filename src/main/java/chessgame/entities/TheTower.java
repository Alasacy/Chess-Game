package chessgame.entities;

import java.util.List;

import com.badlogic.gdx.Gdx;
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
import chessgame.entities.theTowerStates.TheTowerDormant;
import chessgame.entities.theTowerStates.TheTowerIdle;
import chessgame.entities.theTowerStates.TheTowerJump;
import chessgame.entities.theTowerStates.TheTowerSmash;
import chessgame.entities.theTowerStates.TheTowerState;
import chessgame.utils.Constants;
import chessgame.utils.EntityAnimation;
import chessgame.utils.EntityManager;
import chessgame.utils.GameSound;
import chessgame.utils.HUD;

public class TheTower implements IEnemies {
	int health;
	int attack;
	public float aggroRange = 6f;
	
	Vector2 position;
	Vector2 homePosition;
	World world;
	public EntityManager entityManager;
	
	//Bodies
	Body myBody;
	Body leftHand;
	Body rightHand;
	Body leftWave;
	Body rightWave;
	
	//Sprites
	public Texture sprite;
	Texture sprite2;
	Texture sprite3;
	public Texture dormantSprite;
	EntityAnimation bodyAnimation;
	public Sprite leftHandsprite;
	public Sprite rightHandsprite;
	Texture waveSprite;
	EntityAnimation waveAnimation;
	EntityAnimation waveAnimation2;
	public boolean dormant = true;
	
	//Time
	boolean invisFrame;
	long hitTime;
	
	//Hand-attack
	public boolean smash = false;
	public boolean returnHand = false;
	public boolean quickReturn = false;
	public boolean frozen = false;
	Vector2 leftDesired;
	Vector2 rightDesired;
	
	//ShockWave
	Vector2 leftWaveStart;
	Vector2 rightWaveStart;
	public boolean shockWave = false;
	float waveLength = 4f;
	float waveSpeed = 15f;
	
	//Jump-attack
	public boolean jump = false;
	
	//State	
	public TheTowerState idleState = new TheTowerIdle(this);
	public TheTowerState smashState = new TheTowerSmash(this);
	public TheTowerState jumpState = new TheTowerJump(this);
	public TheTowerDormant dormantState = new TheTowerDormant(this);
	TheTowerState currentState = dormantState;
	
	//Entity size
	float width = 2f;
	float height = 3f;
	public int jumpCounter = 2;
	
	public TheTower (Vector2 position, World world, EntityManager entityManager) {
		homePosition = new Vector2(position.x/Constants.PixelPerMeter+width, position.y/Constants.PixelPerMeter+height);
		this.position = homePosition;
		this.world = world;
		this.entityManager = entityManager;
		health = 3;
		attack = 1;
		
		leftDesired = new Vector2(position.x -5, position.y + 4);
		rightDesired = new Vector2(position.x +5, position.y + 4);
	}
	
	public void initialize() {
		
		dormantSprite = sprite = new Texture (Gdx.files.internal("assets/Enemies/TheTower/TheTowerDormantSheet.png").file().getAbsolutePath());
		sprite = new Texture (Gdx.files.internal("assets/Enemies/TheTower/TheTowerSpriteSheet.png").file().getAbsolutePath());
		sprite2 = new Texture (Gdx.files.internal("assets/Enemies/TheTower/TheTowerSpriteSheet2.png").file().getAbsolutePath());
		sprite3 = new Texture (Gdx.files.internal("assets/Enemies/TheTower/TheTowerSpriteSheet3.png").file().getAbsolutePath());
		bodyAnimation = new EntityAnimation(dormantSprite, 4, 7f, this, new Vector2(64f, 96f));
		
		leftWaveStart =new Vector2(position.x - width, position.y+0.8f - (height));
		rightWaveStart =new Vector2(position.x + width, position.y+0.8f - (height));
		
		rightHandsprite = new Sprite(new Texture (Gdx.files.internal("assets/Enemies/TheTower/RightHand.png").file().getAbsolutePath()));
		leftHandsprite = new Sprite(new Texture (Gdx.files.internal("assets/Enemies/TheTower/LeftHand.png").file().getAbsolutePath()));
		waveSprite = new Texture (Gdx.files.internal("assets/Enemies/TheTower/TowerWave-Sheet.png").file().getAbsolutePath());
		waveAnimation = new EntityAnimation(waveSprite, 8, 8f, this, new Vector2(32f, 32f));
		waveAnimation2 = waveAnimation;
		
		//Creates the bodies for the different elements of the boss
		createBody();
		createHands();
		createShockWave();
		
		currentState.Enter();
		
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
		
		//creating a fixture that will serve as the boss' weakpoint
		FixtureDef fixDef = new FixtureDef();
		fixDef.isSensor = true;
		shape.setAsBox(width * 0.95f, 0.1f, new Vector2(0f, height), 0);
		fixDef.shape = shape;
		
		myBody.createFixture(fixDef).setUserData("weakpoint");
		
		//creating a fixture that will serve as the boss groundCheck-platter.
		fixDef = new FixtureDef();
		fixDef.isSensor = true;
		//the shape should be lower than the players width and height
		shape.setAsBox(width * 0.95f, height / 8, new Vector2(0f, -height), 0);
		fixDef.shape = shape;
		
		myBody.createFixture(fixDef).setUserData("Tfoot");
	}
	/**
	 * Works similarly to createBody, but creates the hands bodies
	 */
	private void createHands() {

		//Creates the boss Hands
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(position.x - 5, position.y + 3));
		bodyDef.gravityScale = 0;
		
		leftHand = world.createBody(bodyDef);
		bodyDef.position.set(new Vector2(position.x + 5, position.y + 3));
		rightHand = world.createBody(bodyDef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(1.5f, 1.5f);
		leftHand.createFixture(shape, 100000f).setUserData("TLHand");
		rightHand.createFixture(shape, 100000f).setUserData("TLHand");
		leftHand.getFixtureList().get(0).setFriction(0f);
		rightHand.getFixtureList().get(0).setFriction(0f);
		
		leftHand.setUserData(this);
		rightHand.setUserData(this);
	}
	/**
	 * Creates the bodies for the shockWave
	 */
	private void createShockWave() {
		//shockwave
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(leftWaveStart);
		bodyDef.gravityScale = 0;
		
		leftWave = world.createBody(bodyDef);
		bodyDef.position.set(rightWaveStart);
		rightWave = world.createBody(bodyDef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(1, 0.8f);
		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = shape;
		fixDef.isSensor = true;
		leftWave.createFixture(fixDef).setUserData("Wave");
		rightWave.createFixture(fixDef).setUserData("Wave");
		leftWave.setUserData(this);
		rightWave.setUserData(this);
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
	}

	@Override
	public Sprite getSprite() {
		return null;
	}

	@Override
	public int getHealth() {
		return health;
	}

	@Override
	public void takeDamage(int damage) {
			float xVal;
			//Knocks players away
			Player player;
	        player = getClosestPlayer(2f);
	        
	        if(player != null) {
		        float targetDir = player.getPosition().x-getPosition().x;
		        if (targetDir < 0)
		        	xVal = -80f;
		        else
		        	xVal = 80f;
		        player.myBody.setLinearVelocity(new Vector2(xVal, 15f));
	        }
	        
			attack = 0;
			if(damage <= health)
				health -= damage;
			else {
				kill();
				return;
			}
			
			
			HUD.BossHp(3, damage);
	}

	@Override
	public int getAttack() {
		return attack;
	}
	public void jump() {
		myBody.applyLinearImpulse(new Vector2(0, 1000000f), this.position, true);
		jump = true;
	}
	
	@Override
	public void moveTo(Vector2 target) {
		float xVal = position.x - target.x;
		
		if(xVal > 0) {
			myBody.setLinearVelocity(-3, myBody.getLinearVelocity().y);
		} else {
			myBody.setLinearVelocity(3, myBody.getLinearVelocity().y);
		}
	}
	
	/**
	 * Moves the hand of the entity to a set destination
	 * @param string - "left" or "right" depending on which hand
	 * @param target - where to aim the hand
	 */
	public void moveHandTo(String string, Vector2 target, boolean home, int speed) {
		//The abs from target to hand in x-direction
		float xLeftVal = leftHand.getPosition().x - target.x;
		float xRightVal = rightHand.getPosition().x - target.x;
		
		//The abs from target to hand in y-direction
		float yLeftVal = leftHand.getPosition().y - target.y;
		float yRightVal = rightHand.getPosition().y - target.y;
		
		Vector2 movement = new Vector2(0,0);
		
		if(string == "left") {
			if(home) {
				if(Math.abs(xLeftVal) <= .2f && Math.abs(yLeftVal) <= .2f)
					home = false;
				if (Math.abs(xLeftVal) <= .2f){
					movement.x = 0;
				}
				else if(xLeftVal > 0) {
					movement.x = -speed;
				} else {
					movement.x = speed;
				}
				if (Math.abs(yLeftVal) <= .2f){
					movement.y = 0;
				}
				else if(xLeftVal > 0) {
					movement.y = -speed;
				} else {
					movement.y = speed;
				}
			}
			else if (Math.abs(xLeftVal) <= .5f){
				movement.x = 0;
				smash = true;
			}
			else if(xLeftVal > 0) {
				movement.x = -speed;
				smash = false;
			} else {
				movement.x = speed;
				smash = false;
			}	
			leftHand.setLinearVelocity(movement);
		}
		if(string == "right") {
			if(home) {
				if(Math.abs(xRightVal) <= .2f && Math.abs(yRightVal) <= .2f)
					home = false;
				if (Math.abs(xRightVal) <= .2f){
					movement.x = 0;
				}
				else if(xRightVal > 0) {
					movement.x = -speed;
				} else {
					movement.x = speed;
				}
				if (Math.abs(yRightVal) <= .2f){
					movement.y = 0;
				}
				else if(yRightVal > 0) {
					movement.y = -speed;
				} else {
					movement.y = speed;
				}
			}
			else if (Math.abs(xRightVal) <= .5f){
				movement.x = 0;
				smash = true;
			}
			else if(xRightVal > 0) {
				movement.x = -speed;
				smash = false;
			} else {
				movement.x = speed;
				smash = false;
			}	
			rightHand.setLinearVelocity(movement);
		}
	}
	
	/**
	 * Moves the hand to the ground.
	 * @param string "left" or "right" for which hand to use
	 */
	public void smashHand(String string) {
		smash = true;
		Player player = getClosestPlayer(100f);
		
		float playerAbs = Math.abs(player.getPosition().x - position.x);
		if(playerAbs < width + 1.7f) {
			
			if(string == "left") {
				leftHand.setLinearVelocity(new Vector2(0, 0f));
				
				smash = false;
				returnHand = true;
			}
			if(string == "right") {
				rightHand.setLinearVelocity(new Vector2(0, 0f));
				
				smash = false;
				returnHand = true;
			}
		}
		int speed = 20;
		if(health == 2)
			speed = 25;
		if(health == 1)
			speed = 30;
		
		if(string == "left") {
			leftHand.getFixtureList().get(0).setSensor(false);;
			leftHand.setLinearVelocity(new Vector2(0, -speed));
		}
		if(string == "right") {
			rightHand.getFixtureList().get(0).setSensor(false);;
			rightHand.setLinearVelocity(new Vector2(0, -speed));
		}

	}
	
	public Body getHand(String hand) {
		if(hand == "left")
			return leftHand;
		else
			return rightHand;
	}
	/**
	 * Returns the hands from whatever position to the desired position
	 * @param string - "left" or "right" depending on which hand you want to use
	 *
	 */
	public void returnHand(String string) {
		returnHand = true;
		smash = false;
		
		if(string == "left") {
			leftHand.getFixtureList().get(0).setSensor(true);
			moveHandTo(string, leftDesired, true, 20);
		}
		if(string == "right") {
			rightHand.getFixtureList().get(0).setSensor(true);
			moveHandTo(string, rightDesired, true, 20);
		}
		if(isHandsHome()) {
			returnHand = false;
			quickReturn = false;
		}
	}
	/**
	 * checks if theTower's hands are in the desired position, if so return true
	 * else return false
	 * @return
	 */
	public boolean isHandsHome() {
		boolean leftcorrect = (Math.abs(leftHand.getPosition().x - leftDesired.x) < .5f) && (Math.abs(leftHand.getPosition().y - leftDesired.y) < .5f);
		boolean rightcorrect = (Math.abs(rightHand.getPosition().x - rightDesired.x) < .5f) && (Math.abs(rightHand.getPosition().y - rightDesired.y) < .5f);
		return leftcorrect && rightcorrect;
	}
	
	/**
	 * changes the frozen boolean
	 * @param active
	 */
	public void freezeHands(boolean active) {
		frozen = active;
	}
	
	/**
	 * sends a shockwave out form the boss, used when landing from jump.
	 */
	public void shockWave() {
		shockWave = true;

		leftWave.setLinearVelocity(new Vector2(-waveSpeed, 0));
		rightWave.setLinearVelocity(new Vector2(waveSpeed, 0));
		
		GameSound.playSoundEffect(1, 1);
	}
	public void checkShockWave() {
		if(leftWave.getTransform().getPosition().x < (leftWaveStart.x - waveLength)) {
			leftWave.setActive(false);
			shockWave = false;
		}
		if(rightWave.getTransform().getPosition().x > rightWaveStart.x + waveLength) {
			rightWave.setActive(false);
			shockWave = false;
		}
	}
	/*
	 * Resets the shockwave hitboxes.
	 */
	public void resetShockWave() {
		shockWave = false;
		leftWave.setTransform(leftWaveStart, 0);
		leftWave.setLinearVelocity(Vector2.Zero);
		rightWave.setTransform(rightWaveStart, 0);
		rightWave.setLinearVelocity(Vector2.Zero);
	}
	
	@Override
	public void updateState(Batch batch) {
		currentState.Update();
		if(!HUD.bossBar && !dormant)
			HUD.enableBossHP("Sauron  the  Tower");
		
		leftDesired = new Vector2(position.x - (1.7f+width), position.y + 3);
		rightDesired = new Vector2(position.x + (1.7f+width), position.y + 3);
		
		//Makes it so that the hands dont spin
		leftHand.setAngularVelocity(0);
		rightHand.setAngularVelocity(0);
		if(!dormant) {
			bodyAnimation.changeSheet(sprite);
		}
		if(health == 2) {
			bodyAnimation.changeSheet(sprite2);
			waveSpeed = 25;
		}
		if(health == 1) {
			bodyAnimation.changeSheet(sprite3);
			waveSpeed = 30;
		}
		
		keepWithinBounds();
		position = myBody.getPosition();
		if(batch != null) {
			//Main sprite
			bodyAnimation.render(batch, this.position.x-width, this.position.y-height, true);	
			
			if(leftWave.isActive()) {
				waveAnimation.render(batch, leftWave.getPosition().x -.5f, leftWave.getPosition().y -1f, true);
			}
			if(rightWave.isActive()) {
				waveAnimation.render(batch, rightWave.getPosition().x - .5f, leftWave.getPosition().y -1f, true);
			}
			
			if(currentState != smashState && !frozen) {
				leftHand.setTransform(leftDesired.x, leftDesired.y, 0);
				leftHand.setLinearVelocity(Vector2.Zero);
				rightHand.setTransform(rightDesired.x, rightDesired.y, 0);
				rightHand.setLinearVelocity(Vector2.Zero);
			}
			//Hand sprites
			leftHandsprite.setSize(3, 3);
			leftHandsprite.setPosition(leftHand.getPosition().x - (leftHandsprite.getWidth()/2) , leftHand.getPosition().y - leftHandsprite.getHeight()/2);
			leftHandsprite.draw(batch);	
			
			rightHandsprite.setSize(3, 3);
			rightHandsprite.setPosition(rightHand.getPosition().x - (rightHandsprite.getWidth()/2) , rightHand.getPosition().y - rightHandsprite.getHeight()/2);
			rightHandsprite.draw(batch);
			
			if(shockWave) {
				leftWave.setActive(true);
				rightWave.setActive(true);
			} else {
				leftWave.setActive(false);
				rightWave.setActive(false);
				resetShockWave();
			}
		}
		if(health <= 0)
			kill();
		
		if(System.currentTimeMillis() < hitTime + 100) {
			//sprite.setColor(Color.RED);
			attack = 0;
		} else {
			//sprite.setColor(Color.WHITE);
			attack = 1;
		}
	}

	@Override
	public void kill() {
		
		//Opens the doors for the boss room
		if(entityManager.doorMap.containsKey(0)) {
			Door door = entityManager.doorMap.get(0);
			door.doorState();
		}
		if(entityManager.doorMap.containsKey(1)) {
			Door door = entityManager.doorMap.get(1);
			door.doorState();
		}
		entityManager.removeEntity(this);
		leftHand.setActive(false);
		rightHand.setActive(false);
		
		GameSound.stopMusic();
		GameSound.playMusic(5);
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
	public void changeState(TheTowerState state) {
		currentState = state;
		currentState.Enter();
	}

	@Override
	public IState getCurrentState() {
		return currentState;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}
}
