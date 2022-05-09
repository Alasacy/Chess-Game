package chessgame.entities;

/**
 * Interface for the interactable objects such as chess-points
 * @author Thorgal,Mikal
 *
 */
public interface IObjects extends IEntities {
	
	/**
	 * Function to describe the implicit behaviour of the item
	 */
	abstract void itemFunction(Player player);
	
}
