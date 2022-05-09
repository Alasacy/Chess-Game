package chessgame.server.DataTypes;

import com.badlogic.gdx.math.Vector2;
/**
* This class decides what data ,considering each player, should be sent.
* @author thorg
*/

public class PlayerData{
	int health;
	Vector2 position;
	
	public PlayerData() {
	}
	
	public PlayerData(int health, Vector2 position){
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
