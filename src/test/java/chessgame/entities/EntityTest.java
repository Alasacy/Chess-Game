package chessgame.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import chessgame.utils.EntityManager;
import chessgame.world.PhysicsWorld;

@ExtendWith(MockitoExtension.class)
public class EntityTest {
	
	PhysicsWorld pworld;
	EntityManager manager;
	
	@BeforeEach
	void setUpBeforeEach() {
		pworld = new PhysicsWorld();
		manager = new EntityManager(pworld);
	}

	/**
	 * Checks that an entity can take damage
	 */
	@Test
	void entityDamage() {
		Pawn pawn = new Pawn(Vector2.Zero, pworld.world, manager, 0);
		int Health = pawn.getHealth();
		pawn.takeDamage(1);
		assertEquals(Health-1, pawn.getHealth());
	}
	
	/**
	 * Checks that the entity dies when its health reaches 0
	 */
	@Test
	void entityDeath() {
		Pawn pawn = new Pawn(Vector2.Zero, pworld.world, manager, 1);
		manager.addEntity(pawn);
		pawn.createBody();
		pawn.sprite = new Sprite();
		
		//Before death, 1 entity
		assertEquals(1, manager.entityList.size());
		
		pawn.takeDamage(2* pawn.getHealth());
		manager.updateLists();
		//After death, 0 entities
		assertEquals(0, manager.entityList.size());
	}
}