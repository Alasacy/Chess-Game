package chessgame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import chessgame.utils.Constants;
import chessgame.utils.EntityManager;

public class Door implements IEntities {
	
	Vector2 position;
	Sprite sprite;
	Body myBody;
	World world;
	EntityManager entityManager;
	
	Sprite spriteOpen;
	Sprite spriteClosed;
	
	Boolean open;
	int activationCode;
	
	int myId;
	float width = 0.5f;
	float height = 1.5f;
	
	
	public Door(Vector2 position, World world, EntityManager entityManager, int code, int myId){
		this.position = new Vector2(position.x/Constants.PixelPerMeter+width, position.y/Constants.PixelPerMeter+height);
		this.world = world;
		this.entityManager = entityManager;
		this.activationCode = code;
		this.myId = myId;
		open = false;
	}
	
	public void initialize() {
		spriteOpen = new Sprite(new Texture (Gdx.files.internal("assets/objects/IronGateOpen.png").file().getAbsolutePath()));
		spriteClosed = new Sprite(new Texture (Gdx.files.internal("assets/objects/IronGateClosed.png").file().getAbsolutePath()));
		sprite = spriteClosed;
		
		createBody();
		
		entityManager.addEntity(this);
		entityManager.doorMap.put(activationCode, this);
	}
	
	@Override
	public void createBody() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.position.set(new Vector2(position.x, position.y));
		
		myBody = world.createBody(bodyDef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width, height);
		
		myBody.createFixture(shape, 1000f).setUserData("door");;
		myBody.setFixedRotation(true);
		myBody.setUserData(this);
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
		
		if(open) {
			myBody.getFixtureList().get(0).setSensor(true);
			sprite = spriteOpen;
		} else {
			myBody.getFixtureList().get(0).setSensor(false);
			sprite = spriteClosed;
		}
		
		if(batch != null) {
			sprite.setPosition(position.x - sprite.getWidth()/2 , position.y-sprite.getHeight()/2);
			sprite.setSize(1, 3);
			sprite.draw(batch);	
		}
		
	}
	/**
	 * Changes the door state based on previous state.
	 * If this is called when door is open, it closes
	 * If this is called when door is closed, it opens.
	 */
	public void doorState() {
		if(!open) {
			open = true;
		} else {
			open = false;
		}
	}
	/**
	 * checks whether the door is open or not, return boolean
	 * @return open
	 */
	public boolean isOpen() {
		return open;
	}

	public int getId() {
        return this.myId;
    }
}
