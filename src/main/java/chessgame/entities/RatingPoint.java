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
import chessgame.utils.EntityAnimation;
import chessgame.utils.EntityManager;
import chessgame.utils.GameSound;

public class RatingPoint implements IObjects{
	Vector2 position;
	Body myBody;
	World world;
	EntityManager entityManager;
	EntityAnimation animation;
	Texture sprite;
	
	int id;
	
	public RatingPoint(Vector2 position, World world, EntityManager entityManager, int id){
		this.position = new Vector2(position.x/Constants.PixelPerMeter, position.y/Constants.PixelPerMeter);
		this.world = world;
		this.entityManager = entityManager;
		this.id = id;
	}
	
	public void initialize() {
		sprite = new Texture (Gdx.files.internal("assets/objects/coin-Sheet.png").file().getAbsolutePath());
		animation = new EntityAnimation(sprite, 4, 7f, this, new Vector2(32,32));
		createBody();
		
		//Adds the pawn to the entityManager
    	entityManager.addEntity(this);
	}
	
	@Override
	public void createBody() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.position.set(new Vector2(position.x, position.y));
		
		myBody = world.createBody(bodyDef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.5f, 0.5f);
		
		FixtureDef fixDef = new FixtureDef();
		fixDef.isSensor = true;
		shape.setAsBox(0.5f, 0.5f, new Vector2(0.5f, 0.5f), 0);
		fixDef.shape = shape;
		
		myBody.createFixture(fixDef).setUserData("Object");
		myBody.setUserData(this);
		
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
		return null;
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
		position = myBody.getPosition();
		if(batch != null) {
			animation.render(batch);	
		}
	}

	@Override
	public void itemFunction(Player player) {
		GameSound.playSoundEffect(3, 1);
		player.ratingScore += 7;
		kill();
	}
	
	public int getId() {
		return id;
	}


}
