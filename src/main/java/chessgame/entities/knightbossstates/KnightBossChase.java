package chessgame.entities.knightbossstates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import chessgame.entities.KnightBoss;
import chessgame.entities.Player;
import chessgame.utils.EntityAnimation;
import chessgame.utils.GameSound;

public class KnightBossChase extends KnightBossState{
	KnightBoss knight;
	int jumps;
	int counter;
	
	public KnightBossChase(KnightBoss knight) {
		this.knight = knight;
	}
	
	@Override
	public void Enter() {
		jumps = (int) Math.random()*2+3 + knight.allJumps;
		counter = 125 + knight.getHealth()*5;
		if (knight.getHealth() == 1)
			counter = 50;
		knight.telegrafChase = true;
		//GameSound.playSoundEffect(0,1);
	}
	
	@Override
	public void Update() {
		if (counter <= 0) {
			if (jumps >= knight.allJumps) {
			Player target = knight.getClosestPlayer(10000f);
			if (target != null) {
				knight.moveTo(target.getPosition());
				if(target.getPosition().x-0.1f < knight.getPosition().x && knight.getPosition().x < target.getPosition().x+0.1f) {
					knight.getBody().setLinearVelocity(0, knight.getBody().getLinearVelocity().y);
					}
				}
			} else {
				knight.changeState(knight.idleState);
			}
		} else if (knight.isGrounded()){
			counter -= 1;
		}
	}
}
