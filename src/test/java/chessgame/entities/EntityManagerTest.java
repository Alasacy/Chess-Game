package chessgame.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector2;

import chessgame.entities.Pawn;
import chessgame.utils.EntityManager;
import chessgame.world.PhysicsWorld;

/**
 * Test for entityManageren, teste at funksjonaliteten fungerer som den skal
 * @author thorg
 *
 */
class EntityManagerTest {
	
	static PhysicsWorld pWorld;
	static EntityManager entityManager;
	static Pawn pawn1;
	static Pawn pawn2;
	@BeforeEach
	void setup() {
		pWorld = new PhysicsWorld();
		entityManager = new EntityManager(pWorld);
		pawn1 = new Pawn(new Vector2(0,0), pWorld.world, entityManager, 0);
		pawn1.createBody();
		pawn2 = new Pawn(new Vector2(0,0), pWorld.world, entityManager, 1);
		pawn2.createBody();
	}
	
	/**
	 * Test that an entity gets added when addEntity() is called
	 */
	@Test
	void addEntityTest() {
		assertFalse(entityManager.entityList.contains(pawn1));
		entityManager.addEntity(pawn1);
		assertTrue(entityManager.entityList.contains(pawn1));
	}
	
	/**
	 * Tests that the entities gets added to entityRemoveList when removeEntity is called
	 * and removed from removeList when updateLists() is called
	 */
	@Test
	void entityRemoveListTest() {
		entityManager.addEntity(pawn2);
		entityManager.updateLists();
		assertEquals(entityManager.entityRemoveList.size(), 0);
		entityManager.removeEntity(pawn2);
		assertEquals(entityManager.entityRemoveList.size(), 1);
		entityManager.updateLists();
		assertEquals(entityManager.entityRemoveList.size(), 0);
	}
	
	/**
	 * Tests that the entities in the remove list gets removed from the list and world
	 */
	@Test
	void removeEntityFromWorld() {
		entityManager.addEntity(pawn1);
		entityManager.addEntity(pawn2);
		assertEquals(pWorld.world.getBodyCount(), 2);
		entityManager.removeEntity(pawn1);
		entityManager.removeEntity(pawn2);
		assertEquals(pWorld.world.getBodyCount(), 2);
		entityManager.updateLists();
		assertEquals(pWorld.world.getBodyCount(), 0);
	}

}
