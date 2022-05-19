package chessgame.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Interface for all entities in the game, such as player, items, enemies and bosses
 *
 */
public interface IEntities {
	/**
	 * Creates the entity body
	 *
	 * 
	 */
	
	abstract void createBody();
	
	/**
	 * Gets the position of the entity according to the gameMap.
	 * @return the position.
	 */
	public abstract Vector2 getPosition();
	
	/**
	 * moves the entity on the gameMap.
	 */
	public abstract void move(Vector2 newPos);
	
	/**
	 * Gets the current sprite of the entity
	 * @return the sprite.
	 */
	public abstract Sprite getSprite();
	
	/**
	 * gets the entities body
	 * @return - Body
	 */
	public abstract Body getBody();
	
	
	/**
	 * Kills the entity.
	 */
	public abstract void kill();
	
	
	/**
	 * removes the Box2D body of the entity
	 */
	public void removeBody();
	
	/**
	 * updates the current state of the Enemy.
	 */
	public abstract void updateState(Batch batchs);
	
	/**
	 * initializes values that needs to be fetched from files.
	 * And runs the methods needed before the object is ready for use.
	 */
	public abstract void initialize();
	
	public abstract int getId();
}
