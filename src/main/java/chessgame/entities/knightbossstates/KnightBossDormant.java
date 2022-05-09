package chessgame.entities.knightbossstates;

import com.badlogic.gdx.math.Vector2;

import chessgame.entities.Door;
import chessgame.entities.KnightBoss;
import chessgame.utils.EntityManager;
import chessgame.utils.GameSound;

public class KnightBossDormant extends KnightBossState {
	KnightBoss knight;
	int counter;
	boolean playingMusic;
	
	public KnightBossDormant(KnightBoss knight) {
		this.knight = knight;
		playingMusic = false;
		counter = 250;
	}
	
	@Override
	public void Enter() {
		knight.getBody().setLinearVelocity(Vector2.Zero);
		EntityManager entityManager = knight.entityManager;
		if(entityManager.doorMap.containsKey(0)) {
			Door door = entityManager.doorMap.get(0);
			door.doorState();
		}
	}
	
	@Override
	public void Update() {
		if (knight.getClosestPlayer(knight.aggroRange) != null)
			knight.activated = true;
		if (knight.activated) {
			if (!playingMusic) {
				if (counter <= 0) {
					GameSound.playMusic(2);
					playingMusic = true;
					EntityManager entityManager = knight.entityManager;
					if(entityManager.doorMap.containsKey(0)) {
						Door door = entityManager.doorMap.get(0);
						door.doorState();
					}
				} else 
					counter -= 1;
			}
			if (knight.isGrounded()) 
					knight.changeState(knight.idleState);
		} else
			knight.getBody().setLinearVelocity(Vector2.Zero);
	}
}
