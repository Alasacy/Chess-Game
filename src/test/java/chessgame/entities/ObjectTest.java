package chessgame.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector2;

import chessgame.utils.EntityManager;
import chessgame.world.PhysicsWorld;

public class ObjectTest {
	PhysicsWorld pworld;
	
	@BeforeEach
	void setup() {
		pworld = new PhysicsWorld();
	}
	
	@Test
	//Checks that a button alters the state of the door.
	void buttonDoorTest() {
		EntityManager manager = new EntityManager(pworld);
		
		int code = 1;
		Button button = new Button(Vector2.Zero, pworld.world, manager, code, 0);
		Door door = new Door(Vector2.Zero, pworld.world, manager, code, 1);
		manager.doorMap.put(code, door);
		
		assertEquals(false, door.isOpen());
		button.itemFunction(null);
		assertEquals(true, door.isOpen());
		
		Button falseButton = new Button(Vector2.Zero, pworld.world, manager, 2, 2);
		falseButton.itemFunction(null);
		assertEquals(true, door.isOpen());
	}
	
	@Test
	void playerIncreaseRatingTest() {
		Player player = new Player(new Vector2(0,0), pworld.world, "player1");
		EntityManager mockManager = mock(EntityManager.class);
		RatingPoint rating = new RatingPoint(new Vector2(0,0), pworld.world, mockManager, 0);
		
		assertEquals(player.getScore(), 0);
		rating.itemFunction(player);
		assertTrue(player.getScore() > 0);
	}
}
