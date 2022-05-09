package chessgame.entities;

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
import chessgame.app.PlayerController;
import chessgame.entities.playerstates.PlayerBishopState;
import chessgame.entities.playerstates.PlayerKnightState;
import chessgame.entities.playerstates.PlayerPawnState;
import chessgame.entities.playerstates.PlayerState;
import chessgame.entities.playerstates.PlayerTowerState;
import chessgame.utils.SaveFile;
import chessgame.utils.Constants;
import chessgame.utils.EntityAnimation;
import chessgame.utils.EntityManager;
import chessgame.utils.GameSound;
import chessgame.utils.HUD;

public class Player implements IEntities{
	public String playerName;
	Vector2 position;
	public World world;
	public Body myBody;
	public PlayerController controller;
	String playerId;
	//PlayerStats
	int health = 3;
	int attack = 1;
	
	//PlayerVisuals
	Sprite sprite;
	EntityAnimation dustAnim;
	EntityAnimation leftRunDust;
	EntityAnimation rightRunDust;
	EntityAnimation towerDash;
	EntityAnimation lTowerDash;
	EntityAnimation changeForm;
	Color playerColor;
	
	boolean hasTakenDamage = false;
	float dmgTime = 0;
	
	public EntityManager manager;
	
	public boolean changingForm;
	boolean jumpDust = true;
	public boolean facing = true;
	
	public boolean dash;
	public boolean sprint = false;
	public boolean dead = false;
	public int ratingScore = 0;
	private int progress;
	
	
	//PlayerStates
	PlayerPawnState playerPawnState = new PlayerPawnState(this);
	PlayerTowerState playerTowerState = new PlayerTowerState(this);
	PlayerKnightState playerKnightState = new PlayerKnightState(this);
	PlayerBishopState playerBishopState = new PlayerBishopState(this);
	public PlayerState currentState;
	
	//Player size
	float width = .5f;

	float height = .95f;
	int entityId;
	
	public Player (Vector2 position, World world, String id) {

		this.position = new Vector2(position.x/Constants.PixelPerMeter+width, position.y/Constants.PixelPerMeter+height);
		this.world = world;
		playerId = id;

		currentState = playerPawnState;
		this.manager = manager;
		progress =(int) SaveFile.readProgress()[0];
	}
	
	public void initialize() {
		sprite = new Sprite(new Texture (Gdx.files.internal("assets/player/player.png").file().getAbsolutePath()));
		Texture dust = new Texture (Gdx.files.internal("assets/player/landingDust.png").file().getAbsolutePath());
		dustAnim =  new EntityAnimation(dust, 4, 16f, this, new Vector2(64,32), true);
		Texture lRunDust = new Texture (Gdx.files.internal("assets/player/runDust.png").file().getAbsolutePath());
		leftRunDust =  new EntityAnimation(lRunDust, 4, 8f, this, new Vector2(64,32), true);
		Texture rRunDust = new Texture (Gdx.files.internal("assets/player/runDustR.png").file().getAbsolutePath());
		rightRunDust =  new EntityAnimation(rRunDust, 4, 8f, this, new Vector2(64,32), true);
		Texture towerDashTexture = new Texture (Gdx.files.internal("assets/player/rookDash.png").file().getAbsolutePath());
		towerDash =  new EntityAnimation(towerDashTexture, 3, 8f, this, new Vector2(128,128), true);
		Texture lTowerDashTexture = new Texture (Gdx.files.internal("assets/player/lRookDash.png").file().getAbsolutePath());
		lTowerDash =  new EntityAnimation(lTowerDashTexture, 3, 8f, this, new Vector2(128,128), true);
		Texture changeFormTexture = new Texture (Gdx.files.internal("assets/player/transformCloud.png").file().getAbsolutePath());
		changeForm =  new EntityAnimation(changeFormTexture, 7, 16f, this, new Vector2(128,128), true);
		
		createBody();

		//Load rating from saveFile
		ratingScore = SaveFile.readScore();
    	
    	if(controller == null) {
    		playerColor = Color.valueOf("ffeba5");
    	}
    	else {
    		playerColor = Color.WHITE;
    	}
    	sprite.setColor(playerColor);
	}

	@Override
	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
        myBody.setTransform(position, 0f);
    }
	/**
	 * Moves the entity based on input vector2
	 * @param Vector2 - movement on the axises
	 * @author Thorgal, Mikal
	 */
	@Override
	public void move(Vector2 movement) {
		float maxSpeed;
		float moveForce;
		
		if(sprint) {
			maxSpeed = 20f;
			moveForce = 400f;
		}
		else {
			maxSpeed = 10f;
			moveForce = 200f;
		}
		
		Vector2 playerVelocity = myBody.getLinearVelocity();
		
		if(movement.x > 0) {
			if (playerVelocity.x < 0) {
				myBody.applyForce(new Vector2(moveForce-playerVelocity.x*100, 0), this.position, true);
			}
			else if(playerVelocity.x < maxSpeed) {
				myBody.applyForce(new Vector2(moveForce, 0), this.position, true);
			} else {
				myBody.setLinearVelocity(new Vector2(maxSpeed, playerVelocity.y));
			}
		}
		else if(movement.x < 0) {
			if (playerVelocity.x > 0) {
				myBody.applyForce(new Vector2(-moveForce-playerVelocity.x*100, 0), this.position, true);
			}
			else if(-playerVelocity.x < maxSpeed) {
				myBody.applyForce(new Vector2(-moveForce, 0), this.position, true);
			} else {
				myBody.setLinearVelocity(new Vector2(-maxSpeed, playerVelocity.y));
			}
		}
		else if(movement.x == 0 && controller.isGrounded) {
			myBody.applyForce(new Vector2(-playerVelocity.x*50, 0), this.position, true);
		} 
		else if(movement.x == 0 && !controller.isGrounded) {
			myBody.applyForce(new Vector2(-playerVelocity.x*10, 0), this.position, true);
		}
	}
	/**
	 * Applies upward force to the entity, making it "jump"
	 * @param jumpForce
	 * @author Mikal, Thorgal
	 */
	public void jump(float jumpForce) {	
		myBody.setLinearVelocity(new Vector2(myBody.getLinearVelocity().x,0f));
		myBody.applyLinearImpulse(new Vector2(0, jumpForce),this.position ,true);
		GameSound.playSoundEffect(5, 2);

	}
	public Vector2 getVelocity() {
		return myBody.getLinearVelocity();
	}
	
	@Override
	public Sprite getSprite() {
		return sprite;
	}
	
	@Override
	public void createBody() {
		BodyDef bodyDef = new BodyDef();
		
		//creating a fixture that will serve as the players groundCheck-platter.
		FixtureDef fixDef = new FixtureDef();
		
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(position.x, position.y));
		
		myBody = world.createBody(bodyDef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width, height);
		fixDef.shape = shape;
		fixDef.density = 10f;
		
		myBody.createFixture(fixDef).setUserData("Player");
		myBody.setFixedRotation(true);
		myBody.setUserData(this);
		
		fixDef.isSensor = true;
		//the shape should be lower than the players width and height
		shape.setAsBox(width * 0.95f, height / 4, new Vector2(0f, -height), 0);
		fixDef.shape = shape;
		
		myBody.createFixture(fixDef).setUserData("foot");
		
		//creating a fixture that will serve as the players skyCheck
		fixDef = new FixtureDef();
		fixDef.isSensor = true;
		//the shape should be lower than the players width and height
		shape.setAsBox(width * 0.95f, height * 0.1f, new Vector2(0f, +height), 0);
		fixDef.shape = shape;
		myBody.createFixture(fixDef).setUserData("sky");
		
	}	

	public int getHealth() {
		return health;
	}
	
	public void takeDamage(int damage) {
		if(dmgTime > 0.5f) {
			dmgTime = 0;
			hasTakenDamage = true;
			if(damage < health)
				health -= damage;
			else {
				health = 0;
				kill();
			}
		}
		if(controller != null)
			HUD.setHP(health);
	}

	public int getAttack() {
		return attack;
	}
	
	public int getScore() {
		return getRatingScore();
	}

	@Override
	public void kill() {
		dead = true;
	}

	@Override
	public void removeBody() {
		
	}
	
	public void changeState(PlayerState state) {
		currentState = state;
		currentState.Enter();
	}
	
	public void nextState() {
		if(currentState == playerPawnState) {
			if(progress >= 6) {
				changeState(playerKnightState);
			} 
			if(progress >= 3) {
				changeState(playerTowerState);
			}
		}
		else if(currentState == playerKnightState)
			changeState(playerTowerState);
		else if(currentState == playerTowerState)
			if(progress >= 9) {
				changeState(playerBishopState);
			} else 
				changeState(playerPawnState);
		else if(currentState == playerBishopState)
			changeState(playerPawnState);
	}
	
	public void updatePosition() {
		position = myBody.getPosition();
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
	
	/**
	 * Updates some aspects of the players data, such as: 
	 * Sprite,
	 * Position
	 * @author Mikal, Thorgal
	 * @param batch
	 */
	@Override
	public void updateState(Batch batch) {
	//Sets the maximum speed upward of the player.
			dmgTime += Gdx.graphics.getDeltaTime();
			if(myBody.getLinearVelocity().y > 30)
				myBody.setLinearVelocity(new Vector2(myBody.getLinearVelocity().x, 20));
			//Updates position vector2
			updatePosition();

			currentState.Update();
			
	    	if (controller != null)
	    		controller.myController(this);

			keepWithinBounds();
	    	
			sprite.setFlip(!facing, false);
			dmgColorTime(Color.RED, 0.15f);
			sprite.setPosition(position.x - sprite.getWidth()/2 , position.y - sprite.getHeight()/2);
			sprite.setSize(2, 2);
			sprite.draw(batch);
			
			spriteAnimations(batch);
			
			if(health == 0)
				kill();
	}
	/**
	 * Changes the color of the player when he takes damage
	 * @param color
	 * @param time
	 */
	public void dmgColorTime(Color color, float time) {
		if(dmgTime > time && hasTakenDamage) {
			sprite.setColor(getPlayerColor());
			hasTakenDamage = false;
		} else if(hasTakenDamage) {
			sprite.setColor(color);
			dmgTime += Gdx.graphics.getDeltaTime();
		}
	}
	public void spriteAnimations(Batch batch) {
		if(jumpDust) {
			jumpDust = dustAnim.playOnce(batch, (position.x - 2*width), position.y-height);
		}
		if(Math.abs(myBody.getLinearVelocity().y) < 0.01f && !jumpDust) {
			if(myBody.getLinearVelocity().x > 2f) {
				leftRunDust.render(batch, (position.x - 3*width), position.y-height, false);
			} 
			if(myBody.getLinearVelocity().x < -2f) {
				rightRunDust.render(batch, (position.x - width), position.y-height, false);
			}
		}
		if(changingForm)
			changingForm = changeForm.playOnce(batch, position.x - 4*width, position.y - 2*height);
		
		if(dash) {
			if(facing)
				towerDash.render(batch, position.x - 4*width, position.y - 2*height, false);
			else
				lTowerDash.render(batch, position.x - 4*width, position.y - 2*height, false);
		}
	}

	@Override
	public Body getBody() {
		return myBody;
	}
	public Color getPlayerColor() {
		return playerColor;
	}
	public void playDust() {
		jumpDust = true;
	}

	public int getRatingScore() {
		return ratingScore;
	}

	public void setRatingScore(int ratingScore) {
		this.ratingScore = ratingScore;
	}

	public int getId() {
        return this.entityId;
    }

    public String getPlayerId() {
        return this.playerId;
    }

    public void setController() {
        //PlayerController
        int[] controls = SaveFile.readSettings();
        controller = new PlayerController(controls);
    }
}
