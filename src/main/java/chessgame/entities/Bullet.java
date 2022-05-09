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

import chessgame.app.Game;
import chessgame.utils.Direction;
import chessgame.utils.EntityManager;

public class Bullet implements IEntities{
	
	Vector2 position;
	World world;
	Body myBody;
	Sprite sprite;
	EntityManager entityManager;
	
	float width = .5f;
	float height = .5f;
	float bulletSpeed = 8;
	float degrees;
	
	float bulletDeathTime = 0;
	
	float homingControl;
	
	String userData;
	
	IEntities target;
	
	double spawnTime;
	
	boolean isPlayer;
	
	//direction of the bullet
	Direction direction = null;
	
	public Bullet (Vector2 position, World world, EntityManager entityManager, Direction direction, boolean isPlayer) {
		this.position = position;
		this.world = world;
		this.entityManager = entityManager;
		this.direction = direction;
		this.isPlayer = isPlayer;
	}
	public Bullet (Vector2 position, World world, EntityManager entityManager, float degrees, boolean isPlayer) {
		this.position = position;
		this.world = world;
		this.entityManager = entityManager;
		this.degrees = degrees;
		this.isPlayer = isPlayer;
	}
	public Bullet (Vector2 position, World world, EntityManager entityManager, Direction direction, Player target) {
		this.position = position;
		this.isPlayer = false;
		this.world = world;
		this.entityManager = entityManager;
		this.direction = direction;
		this.target = target;
		if(target != null)
			homingControl = position.x - target.getPosition().x;
	}
	
	@Override
	public void initialize() {
		if(isPlayer) {
			Texture bullet = new Texture (Gdx.files.internal("assets/bullet.png").file().getAbsolutePath());
			sprite = new Sprite(bullet);
			userData = "Bullet";
		} else {
			Texture eBullet = new Texture (Gdx.files.internal("assets/ebullet.png").file().getAbsolutePath());
			sprite = new Sprite(eBullet);
			userData = "EnemyBullet";
		}
		if (!Game.gameStart)
			entityManager.addEntity(this);
		else
			entityManager.addRuntimeEntity(this);
		createBody();
		spawnTime = System.currentTimeMillis();
	}
	
	@Override
	public void createBody() {
		BodyDef bodyDef = new BodyDef();
		
		//creating a fixture that will serve as the players groundCheck-platter.
		FixtureDef fixDef = new FixtureDef();
		
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(position.x, position.y));
		
		myBody = world.createBody(bodyDef);
		myBody.setGravityScale(0);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width, height);
		fixDef.shape = shape;
		fixDef.density = 10f;
		fixDef.isSensor = true;
		
		myBody.createFixture(fixDef).setUserData(userData);
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
	
	public void bulletMove(Direction dir) {
		Vector2 movement = new Vector2(0, 0);
		if(dir == Direction.LEFT || dir == Direction.DOWNLEFT || dir == Direction.UPLEFT)
			movement.x = -bulletSpeed;
		else if(dir == Direction.RIGHT || dir == Direction.DOWNRIGHT || dir == Direction.UPRIGHT)
			movement.x = bulletSpeed;
		if(dir == Direction.UP || dir == Direction.UPLEFT || dir == Direction.UPRIGHT)
			movement.y = bulletSpeed;
		else if (dir == Direction.DOWN || dir == Direction.DOWNLEFT || dir == Direction.DOWNRIGHT)
			movement.y = -bulletSpeed;
		
		myBody.setLinearVelocity(movement);
	}
	
	public void bulletMove(float degree) {
		Vector2 movement = new Vector2(0, 0);
		movement.x = (float) (Math.cos(degree) * bulletSpeed);
		movement.y = (float) (Math.sin(degree) * bulletSpeed);
		
		myBody.setLinearVelocity(movement);
	}
	
	public void bulletMoveTo(Vector2 target) {
		float xVal = position.x - target.x;
		float yVal = position.y - target.y;
		
		if(homingControl > 0) {
			bulletDeathTime = 0;
			if(xVal > 0) {
				if(yVal < 0) {
					myBody.setLinearVelocity(myBody.getLinearVelocity().x, 4);
				} else {
					myBody.setLinearVelocity(myBody.getLinearVelocity().x, (-4));
				}
				myBody.setLinearVelocity(-1 * Math.abs(xVal) - 5, myBody.getLinearVelocity().y);
			} else {
				myBody.setLinearVelocity(myBody.getLinearVelocity());
				if(bulletDeathTime >= 3) {
					kill();
				}
			}
		} else {
			if(xVal < 0) {
				bulletDeathTime = 0;
				if(yVal < 0) {
					myBody.setLinearVelocity(myBody.getLinearVelocity().x, 4);
				} else {
					myBody.setLinearVelocity(myBody.getLinearVelocity().x, (-4));
				}
				myBody.setLinearVelocity(1 * Math.abs(xVal)+ 5, myBody.getLinearVelocity().y);
			} else {
				myBody.setLinearVelocity(myBody.getLinearVelocity());
				if(bulletDeathTime >= 3) {
					kill();
				}
			}
		}
	}
	
	public void setBulletSpeed(float speed) {
		bulletSpeed = speed;
	}
	
	public void setBulletDirection(Direction dir) {
		direction = dir;
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
		if(System.currentTimeMillis() > spawnTime + 3000 && target == null)
			kill();
		bulletDeathTime += Gdx.graphics.getDeltaTime();
		position = myBody.getPosition();
		if(target != null) {
			bulletMoveTo(target.getPosition());
		}
		else if(direction != null){
			bulletMove(direction);
		} else {
			bulletMove(degrees);
		}
		
		sprite.setPosition(position.x - sprite.getWidth()/2 , position.y - sprite.getHeight()/2);
		sprite.setSize(1, 1);
		sprite.draw(batch);
	}
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
