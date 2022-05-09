package chessgame.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

import chessgame.app.Game;
import chessgame.entities.Button;
import chessgame.entities.IEntities;
import chessgame.entities.Pawn;
import chessgame.entities.Player;
import chessgame.server.DataTypes.ButtonData;
import chessgame.server.DataTypes.PawnData;
import chessgame.server.DataTypes.PlayerData;
import chessgame.server.pings.*;
import chessgame.utils.EntityManager;


/**
 * Class to handle what game is going to do when it resives packets
 * @author thorg
 *
 */
public class NetworkHandler {
	private Vector2 playerPosition;
	private HashMap<Integer, PawnData> pawnMap = new HashMap<>();
	private List<Integer> removeList = new ArrayList<>();
	private boolean playerFinished = false;
	private boolean p2Finished = false;
	private boolean bothFinished = false;
	
	public void alterP2Position(Game game) {
		if(playerPosition != null)
			game.player2.setPosition(playerPosition);
	}
	public void alterPawnPositions(Game game) {
		if(pawnMap != null) {
			for(IEntities entity: game.entityManager.entityList) {
				if (entity instanceof Pawn) {
					int pawnId = ((Pawn) entity).getId();
					if(pawnMap.containsKey(pawnId)) {
						entity.move(pawnMap.get(pawnId).getPosition());
					}
				}
			}
		}
	}
	
	public void syncDeaths(Game game) {
		if(removeList != null && !removeList.isEmpty())
			game.entityManager.updateRemoveList(removeList);
	}
	public void sendNextMapPing(){
		if(Game.isWaiting && p2Finished)
			Game.getClient().getClient().sendTCP(new NextMapPing());
	}

	public void ifBothFinished(Game game) {
		if(bothFinished)
			Game.victoryScreen();
	}
	
	//methods to be handled before step
	public void preStep(Game game) {
		alterP2Position(game);
		if(!playerFinished)
			alterPawnPositions(game);
		sendNextMapPing();
		ifBothFinished(game);

	}
	
	public void postStep(Game game) {
		syncDeaths(game);
	}
	
	
	
	//method to decide what to do 
	public void handlePacket(Object object, Game game) {
		if(object instanceof List) {
			if(!(game.entityManager == null)) {
				removeList = (List<Integer>) object;
				
			}
		}
		if (object instanceof P2WaitingPing){
			if(((P2WaitingPing) object).playerID != game.getPlayer().getPlayerId()){
				p2Finished = true;
			}
		}
		if(object instanceof FinishedPing){
				game.goNext = true;
				Game.levelIndex = ((FinishedPing) object).varaible;
		}
		if(object instanceof HashMap) {
			if (!(game.entityManager == null)) {
				if(!game.getIsHost()) {
					for(IEntities entity: game.entityManager.entityList) {
						
						//What to do with the pawn data
						if (entity instanceof Pawn) {
							int pawnId = ((Pawn) entity).getId();
							if(((HashMap) object).containsKey(pawnId)) {
								if(pawnMap.containsKey(pawnId))
									pawnMap.replace(pawnId, ((PawnData) ((HashMap) object).get(pawnId)));
								else
									pawnMap.put(pawnId, ((PawnData) ((HashMap) object).get(pawnId)));
								
								//((Pawn) entity).move(((PawnData) ((HashMap) object).get(pawnId)).getPosition());
								//((Pawn)entity).setHealth(((PawnData) ((HashMap) object).get(pawnId)).getHealth());
							}
						}
						if (entity instanceof Button) {
							int buttonId = ((Button) entity).getId();
							if(((HashMap) object).containsKey(buttonId)) {
								
								if(((Button) entity).isActive() != ((ButtonData) ((HashMap) object).get(buttonId)).getActive()) {
									((Button) entity).itemFunction();
								}
							}
						}	
					}
				}
			
			for(IEntities entity : game.entityManager.playerList) {
				if (entity instanceof Player) {
					String playerId = ((Player) entity).getPlayerId();
					if(((HashMap) object).containsKey(playerId)) {
						if (((Player) entity).getPlayerId().equals("player2") && game.getIsHost())
						{
							playerPosition = ((PlayerData) ((HashMap) object).get(playerId)).getPosition();
						}
						else if (((Player) entity).getPlayerId().equals("player1") && !game.getIsHost()) {
							playerPosition = ((PlayerData) ((HashMap) object).get(playerId)).getPosition();
						}
							
					}
				}
			}
				
			}
			
		}
		if(object instanceof PausePing) {
			if(Game.paused && !((PausePing) object).setToPaused) {
				Game.setPause = true;
			}
			else if(!Game.paused && ((PausePing) object).setToPaused) {
				Game.setPause = true;
			}
			else {
				Game.setPause = false;
			}
		}
		if(object instanceof FinishedPing) {
			if(!Game.isWaiting)
				
				playerFinished = true;
			
		}
		if(object instanceof NextMapPing) {
			bothFinished = true;
		}
		
	}
}
