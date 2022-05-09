package chessgame.entities.knightbossstates;

import com.badlogic.gdx.math.Vector2;

import chessgame.entities.KnightBoss;
import chessgame.utils.GameSound;

public class KnightBossRandom extends KnightBossState {
	KnightBoss knight;
	float xVal;
	int jumps;
	int counter;
	
	public KnightBossRandom(KnightBoss knight) {
		this.knight = knight;	
	}
	
	@Override
	public void Enter() {
		jumps = (int) Math.random()*2+4 + knight.allJumps;
		counter = 150;
		if (knight.getHealth() == 1)
			counter = 0;
	}
	
	@Override
	public void Update() {
		if (counter <= 0) {
			if (jumps >= knight.allJumps) {
				if (knight.lookingRight)
					xVal = (float) (Math.random()*1.5f+10f+knight.getPosition().x);
				else
					xVal = (float) (-Math.random()*1.5f-10f+knight.getPosition().x);
				
				Vector2 target = new Vector2(xVal,-100f);
				knight.moveTo(target);
			} else
				knight.changeState(knight.idleState);
		} else {
			counter -= 1;
		}
	}
}
