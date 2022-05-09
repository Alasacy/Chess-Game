package chessgame.server.pings;

import java.util.HashMap;
import java.util.List;

import chessgame.entities.IEntities;
import chessgame.entities.Player;
import chessgame.server.DataTypes.PlayerData;
import chessgame.utils.EntityManager;

public class PlayerAction {
	public HashMap<String, PlayerData> playerList= new HashMap<>();
	public List<IEntities> removeList;
	
	public PlayerAction(EntityManager entityManager) {
		for(IEntities entity : entityManager.playerList) {
			if (entity instanceof Player) {
				if(((Player) entity).getPlayerId().equals("player2"))
					playerList.put(((Player) entity).getPlayerId(), new PlayerData(((Player) entity).getHealth(), entity.getPosition()));
			}
		}
		this.removeList = entityManager.entityRemoveList;
	}
}
