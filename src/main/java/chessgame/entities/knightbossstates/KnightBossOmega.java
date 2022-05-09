package chessgame.entities.knightbossstates;

import com.badlogic.gdx.math.Vector2;

import chessgame.entities.Bullet;
import chessgame.entities.KnightBoss;
import chessgame.utils.Direction;

public class KnightBossOmega extends KnightBossState{
	KnightBoss knight;
	int jumps;
	float power;
	
	public KnightBossOmega(KnightBoss knight) {
		this.knight = knight;
	}
	
	@Override
	public void Enter() {
		jumps = 8 + knight.allJumps;
	}
	
	@Override
	public void Update() {
		if (jumps >= knight.allJumps) {
			power = 10f+(jumps-knight.allJumps)*5f;
			knight.superJump(power, true);
		} else
			knight.changeState(knight.superJumpState);
		if (knight.isGrounded() && !(jumps-8 >= knight.allJumps)) {
			Bullet bulletL = new Bullet(new Vector2(knight.getPosition().x-0.5f,knight.getPosition().y-knight.height+0.6f), knight.world, knight.entityManager, Direction.LEFT, false);
			bulletL.initialize();
			bulletL.setBulletSpeed(40f);
			Bullet bulletR = new Bullet(new Vector2(knight.getPosition().x+0.6f,knight.getPosition().y-knight.height+0.6f), knight.world, knight.entityManager, Direction.RIGHT, false);
			bulletR.initialize();
			bulletR.setBulletSpeed(40f);
		}
	}
}
