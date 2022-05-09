package chessgame.entities;

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

import chessgame.utils.Constants;
import chessgame.utils.EntityManager;

public class Button implements IObjects{
	
	Vector2 position;
	Sprite sprite;
	Body myBody;
	World world;
	EntityManager entityManager;
	int myId;
	
	boolean active = false;
	Sprite spriteOn;
	Sprite spriteOff;
	
	int activationCode;
	
	float width = 1f;
	float height = 0.5f;

	
	public Button(Vector2 position, World world, EntityManager entityManager, int code, int myId) {
		this.position = new Vector2(position.x/Constants.PixelPerMeter+width, position.y/Constants.PixelPerMeter+height);
		this.world = world;
		this.entityManager = entityManager;
		this.myId = myId;
		activationCode = code;
	}
	
	public void initialize() {

		spriteOff = new Sprite(new Texture (Gdx.files.internal("assets/objects/buttonTestOn.png").file().getAbsolutePath()));
		spriteOn = new Sprite(new Texture (Gdx.files.internal("assets/objects/buttonTestOff.png").file().getAbsolutePath()));

		sprite = spriteOff;
		
		entityManager.addEntity(this);
		
		createBody();
	}
	
	@Override
	public void createBody() {
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixDef = new FixtureDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.position.set(new Vector2(position.x, position.y-0.344f));
		
		myBody = world.createBody(bodyDef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width, height*0.18f);
		fixDef.shape = shape;
		fixDef.friction = 0f;
		
		myBody.createFixture(fixDef).setUserData("ground");;
		myBody.setFixedRotation(true);
		myBody.setUserData(this);
		
		//creating a fixture that will serve as the players groundCheck-platter.
		fixDef.isSensor = true;
		//the shape should be lower than the players width and height
		shape.setAsBox(width * 0.95f, height / 10, new Vector2(0f, height*0.344f), 0);
		fixDef.shape = shape;
		
		myBody.createFixture(fixDef).setUserData("Object");
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

	}

	@Override
	public void removeBody() {

	}

	@Override
	public void updateState(Batch batch) {
		position = myBody.getPosition();
		
		if(batch != null) {
			sprite.setPosition(position.x - sprite.getWidth()/2 , position.y- 0.19f);
			sprite.setSize(2, 1);
			sprite.draw(batch);	
		}
	}

	public void getActivationCode(int code) {
		activationCode = code;
	}
	
	
	@Override
	public void itemFunction(Player player) {
		if(!active) {
			active = true;
			sprite = spriteOn;
		} else {
			active = false;
			sprite = spriteOff;
		}
		if(entityManager.doorMap.containsKey(activationCode)) {
			Door door = entityManager.doorMap.get(activationCode);
			door.doorState();
		}
	}

	public void itemFunction() {
        itemFunction(null);
    }

    public int getId() {
        return this.myId;
    }
    public Boolean isActive() {
        return active;
    }
}
