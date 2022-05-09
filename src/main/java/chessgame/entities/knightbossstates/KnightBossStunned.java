package chessgame.entities.knightbossstates;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import chessgame.app.Game;
import chessgame.entities.Bullet;
import chessgame.entities.IEntities;
import chessgame.entities.Knight;
import chessgame.entities.KnightBoss;
import chessgame.utils.Direction;
import chessgame.utils.EntityManager;
import chessgame.utils.EntityType;
import chessgame.world.PhysicsWorld;

public class KnightBossStunned extends KnightBossState {
	KnightBoss knight;
	int counter;
	int initialTime = 2500;
	
	public KnightBossStunned(KnightBoss knight) {
		this.knight = knight;
	}
	
	@Override
	public void Enter() {
		counter = initialTime;
		
		knight.minions.add(PhysicsWorld.spawnEntity(EntityType.Knight, new Vector2(knight.getPosition().x-7f,knight.getPosition().y+20f), knight.world, knight.entityManager));
		knight.minions.add(PhysicsWorld.spawnEntity(EntityType.Knight, new Vector2(knight.getPosition().x+5f,knight.getPosition().y+20f), knight.world, knight.entityManager));
		
		Bullet bulletL = new Bullet(new Vector2(knight.getPosition().x-0.5f,knight.getPosition().y-knight.height+0.6f), knight.world, knight.entityManager, Direction.LEFT, false);
		bulletL.initialize();
		bulletL.setBulletSpeed(40f);
		Bullet bulletR = new Bullet(new Vector2(knight.getPosition().x+0.6f,knight.getPosition().y-knight.height+0.6f), knight.world, knight.entityManager, Direction.RIGHT, false);
		bulletR.initialize();
		bulletR.setBulletSpeed(40f);
		
	}
	
	@Override
	public void Update() {
		knight.getBody().setLinearVelocity(0f,0f);
		if (counter <= 0 || knight.hit) {
			if(!knight.minions.isEmpty()) {
				for (IEntities entity : knight.minions) {
					entity.kill();
				}
				knight.minions.clear();
			}
			knight.changeState(knight.idleState);
		}
		counter -= 1;
	}
	
}
