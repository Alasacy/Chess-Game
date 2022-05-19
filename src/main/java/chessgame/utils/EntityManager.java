package chessgame.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import chessgame.entities.Door;
import chessgame.entities.IEntities;
import chessgame.entities.Pawn;
import chessgame.entities.Player;
import chessgame.server.pings.Packet;
import chessgame.world.PhysicsWorld;


/**
 * A class that keep track of the entities (enemies) that exists in the world.
 * The EntityManager also helps remove enemies from the world.
 */
public class EntityManager {
	//Lists to keep track of the entity / enemy creation and removal.
	//TODO is linkedlist here more effective??
    public List<IEntities> entityList = new ArrayList<IEntities>();
    public List<IEntities> entityRemoveList = new ArrayList<IEntities>();
    public List<Player> playerList = new ArrayList<Player>();
    public List<Vector2> playerSpawns = new ArrayList<Vector2>();
    public HashMap<Integer, Door> doorMap = new HashMap<Integer, Door>();
    public List<IEntities> entityWaitingList = new ArrayList<IEntities>();
    
    private PhysicsWorld pworld;
    
    private LinkedList<String> playerIdList = new LinkedList<String>();
    
    public EntityManager(PhysicsWorld gameWorld) {
    	this.pworld = gameWorld;
    	//adding names for 2 players
    	playerIdList.add("player1");
    	playerIdList.add("player2");
    } 
    
    /**
     * Adds the entity to the removeList.
     * @param entity
     */
    public void removeEntity(IEntities entity) {
    	if(!entityRemoveList.contains(entity))
    		entityRemoveList.add(entity);
    	
    }
    /**
     * Adds the enemy to the enemyList.
     * @param enemy
     */
    public void addEntity(IEntities entity) {
    	if(!entityList.contains(entity))
    		entityList.add(entity);
    }
    
    /**
     * updates the enemyAddList and enemyRemoveList to add and remove the enemies from the world
     * 
     */
    public void updateLists() {
    	updateLists(entityRemoveList);
    }
    
    public void updateLists(List<IEntities> removeList) {
    	for(IEntities entity : removeList) {
    		if(entityList.contains(entity)) {
    			entityList.remove(entity);
	    		pworld.world.destroyBody(entity.getBody());
    		}
    	}
    	removeList.clear();
    }
    
    /**
     * Updates the sprites and renders of the current acting entities in the world.
     * @param batch
     */
    public void updateEntities(Batch batch) {
    	if (!entityWaitingList.isEmpty()) {
    		for (IEntities entity : entityWaitingList) {
    			addEntity(entity);
    		}
    		entityWaitingList.clear();
    	}
    	for(IEntities entity : entityList) {
    		entity.updateState(batch);
    	}
    }
    
    public void addRuntimeEntity(IEntities entity) {
    	entityWaitingList.add(entity);
    }

    public void updateRemoveList(List<Integer> removeList) {
    	if(removeList != null) {
	    	List<IEntities> myRemoveList = idListToEntities(removeList);
	    	if(!myRemoveList.isEmpty())
	    		System.out.println("updatingRemoveList");
	    	updateLists(myRemoveList);
	    	}
    	
	    }
    
    
    public List<Integer> RemoveListToId(){
    	List<Integer> newIdList = new ArrayList<Integer>();
    	for(IEntities entity : entityRemoveList) {
    		newIdList.add(entity.getId());
    	}
		return newIdList;
    }
    //Ikkje serlig efferktiv, kan muligens effektiviseres
    private List<IEntities> idListToEntities(List<Integer> removeList){
    	List<IEntities> newRemoveList = new ArrayList<IEntities>();
    	
    		for(IEntities entity : entityList) {
    			/////////////tenk not id should be entity
    			for(int id : removeList) {
    				if(entity.getId() == id) {
    					newRemoveList.add(entity);
    				}
    			}
    		
    	}
    	
		return newRemoveList;
    }
    
    /**
     * Adds a spawn location toS the spawnLocation list
     * @param pos
     */
    public void addPlayerSpawn(Vector2 pos) {
    	playerSpawns.add(pos);
    }
    
    /**
     * creates a player with location equal to one of the designated spawnpoints.
     */
    public Player addPlayer() {
    	Player player;
    	
    	/* Spawn player at spawn location, if there are no location, places player at
    	 * (200, 200)
    	 */
    	if(playerSpawns.size() > 0)
    		player = new Player(playerSpawns.remove(0), pworld.world, nextPlayer());
    	else
    		player = new Player(new Vector2(200,200), pworld.world, nextPlayer());

    	
        player.initialize();
        playerList.add(player);
        return player;
    }
    private String nextPlayer() {
		String currentPlayer = this.playerIdList.peek();
		this.playerIdList.remove();
		return currentPlayer;
	}

	/**
     * Updates the sprites and renders of the current acting players in the world.
     * @param batch
     */
    public void updatePlayers(Batch batch) {
    	for(Player player : playerList) {
    		player.updateState(batch);
    	}
    }
    /**
     * Fetches the player from the player list at input index
     * @param index
     * @return
     */
    public Player getPlayer(int index) {
    	return playerList.get(index);
    }
    
    public void removePlayer(Player player) {
    	playerList.remove(player);
    }
    
    
    
}
