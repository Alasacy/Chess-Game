package chessgame.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector2;

import chessgame.utils.EntityManager;
import chessgame.world.PhysicsWorld;

public class PlayerTest {
	PhysicsWorld pworld;
	EntityManager manager;
	Player player;
	
	@BeforeEach
	void setUpBeforeEach() {
		pworld = new PhysicsWorld();
		manager = new EntityManager(pworld);
		/**
		 * Player position at Vector2.zero, is (0.5f, 0.5f)
		 */
		player = new Player(Vector2.Zero, pworld.world, "player1");
		player.createBody();
	}
	
	@Test
	void playerMovementTest() {
		/* As the player moves with force, it does move irregularly
		 * and not perfectly 1 pixel every call of player.move().
		 */
		
		assertEquals(0.5f ,player.position.x);
		player.move(new Vector2(1, 0));
		pworld.logicStep(10);

		player.updatePosition();
		assertTrue(player.position.x > 0.5f);
		player.move(new Vector2(-1, 0));
		player.move(new Vector2(-1, 0));
		pworld.logicStep(10);
		
		player.updatePosition();
		assertTrue(player.position.x < 0.5f);
	}
	
	@Test
	void playerJumpTest() {
		pworld.world.setGravity(Vector2.Zero);
		
		assertEquals(1f ,player.position.y);
		
		player.jump(100000f);
		pworld.logicStep(10);
		player.updatePosition();
		
		assertTrue(player.position.y > 0.5f);
	}
}
