package chessgame.server.pings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

import chessgame.server.DataTypes.*;
import chessgame.app.Game;
import chessgame.entities.*;
import chessgame.utils.EntityManager;









/**
 * this is the packet class, which keeps controll of the data that should be sent
 * @author thorg
 *
 */
public class Packet {
	private EntityManager entityManager;
	
	public HashMap<Integer, PawnData> pawnList = new HashMap<>();
	public HashMap<Integer, DoorData> doorList= new HashMap<>();
	public HashMap<Integer, ButtonData> buttonList= new HashMap<>();
	public HashMap<String, PlayerData> playerList= new HashMap<>();
	
	public List<Integer> removeList = new ArrayList<>();
	
	public Packet(EntityManager entityManager){
		this.entityManager = entityManager;
		removeList = entityManager.RemoveListToId();
		addAllEntities();
	}
	
	void addAllEntities() {
		for(IEntities entity : entityManager.entityList) {
			if (entity instanceof Pawn) {
				pawnList.put(((Pawn) entity).getId(), new PawnData(((Pawn) entity).getHealth(), entity.getPosition()));
			}
			if (entity instanceof Door) {
				doorList.put(((Door) entity).getId(), new DoorData(((Door) entity).isOpen()));
			}
			if (entity instanceof Button) {
				buttonList.put(((Button) entity).getId(), new ButtonData(((Button) entity).isActive()));
			}	
		}
		for(IEntities entity : entityManager.playerList) {
			if (entity instanceof Player) {
				if(((Player) entity).getPlayerId().equals("player1"))
					playerList.put(((Player) entity).getPlayerId(), new PlayerData(((Player) entity).getHealth(), entity.getPosition()));
			}
		}
	}
	
}
