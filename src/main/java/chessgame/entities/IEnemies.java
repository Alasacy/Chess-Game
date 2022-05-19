package chessgame.entities;

import com.badlogic.gdx.math.Vector2;

/**
 * IEnemies, interface for the enemy classes, extends IEntities
 *
 */
public interface 	IEnemies extends IEntities {
	
	/**
	 * Moves the entity towards a defined location
	 * @param target-location
	 */
	public abstract void moveTo(Vector2 target);
	
	/**
	 * Returns the amount of health of the entity
	 * @return - health
	 */
	public abstract int getHealth();
	
	/**
	 * Reduces the HP of the entity by the input value.
	 * @param damage
	 */
	public abstract void takeDamage(int damage);
	
	/**
	 * Gets the Attack-Damage of the entity.
	 * @return damage
	 */
	public abstract int getAttack();
	
	/**
	 * Keeps the entity within bounds of the Map.
	 */
	public void keepWithinBounds();

	/**
	 * Searches for players within the appointed distance, returns the closest player.
	 * if there are no players returns null.
	 */
	public Player getClosestPlayer(Float dist);
	
	public IState getCurrentState();

	public abstract void jump();
}
