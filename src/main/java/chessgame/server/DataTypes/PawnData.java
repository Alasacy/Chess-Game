package chessgame.server.DataTypes;

import com.badlogic.gdx.math.Vector2;

/**
 * This class decides what data ,considering each pawn entity, should be sent.
 *
 */
public class PawnData{
	int health;
	Vector2 position;
	
	public PawnData() {
	}
	
	public PawnData(int health, Vector2 position){
		this.health = health;
		this.position = position;
	}
	
	public int getHealth() {
		return this.health;
	}
	
	public Vector2 getPosition() {
		return this.position;
	}
}
