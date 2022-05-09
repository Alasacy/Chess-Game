package chessgame.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector2;

import chessgame.entities.Pawn;
import chessgame.entities.Player;
import chessgame.utils.EntityManager;
import chessgame.world.PhysicsWorld;

public class EnemyTest {
	PhysicsWorld pworld;
	EntityManager manager;
	
	@BeforeEach
	void setUpBeforeEach() {
		pworld = new PhysicsWorld();
		manager = new EntityManager(pworld);
	}
	
	
	/**
	 * Tests the getClosestPlayerfunction, which gets the player closest to the
	 * enemy.
	 */
	@Test	
	void getClosestPlayerTest(){
		Pawn pawn = new Pawn(Vector2.Zero, pworld.world, manager, 0);
		
		Player target = pawn.getClosestPlayer(1000f);
		assertEquals(null, target);
		
		Player mockPlayer = mock(Player.class);
		when(mockPlayer.getPosition()).thenReturn(new Vector2(10,5));
		Player mockPlayer2 = mock(Player.class);
		when(mockPlayer2.getPosition()).thenReturn(new Vector2(50,3));
		
		manager.playerList.add(mockPlayer);
		manager.playerList.add(mockPlayer2);
		
		//Should find the closest player, player1
		target = pawn.getClosestPlayer(1000f);
		assertEquals(mockPlayer, target);
		
		Player mockPlayer3 = mock(Player.class);
		manager.playerList.add(mockPlayer3);
		when(mockPlayer3.getPosition()).thenReturn(new Vector2(1,0));
		
		//Should find the closest player, player1
		target = pawn.getClosestPlayer(100f);
		assertEquals(mockPlayer3, target);
		
		//No range for search, therefore no players
		target = pawn.getClosestPlayer(0f);
		assertEquals(null, target);
	}
	
	@Test
	void changeStateTest() {
		Pawn pawn = new Pawn(new Vector2(2,2), pworld.world, manager, 0);
		//Checks that the pawn starts in idleState
		assertEquals(pawn.idleState, pawn.getCurrentState());
		
		pawn.changeState(pawn.chaseState);
		assertEquals(pawn.chaseState, pawn.getCurrentState());
		pawn.changeState(pawn.moveState);
		assertEquals(pawn.moveState, pawn.getCurrentState());
		pawn.changeState(pawn.homeState);
		assertEquals(pawn.homeState, pawn.getCurrentState());
	}
}
