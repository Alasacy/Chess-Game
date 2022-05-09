package chessgame.entities.knightbossstates;

import chessgame.entities.KnightBoss;
import chessgame.utils.GameSound;
import chessgame.utils.HUD;

public class KnightBossIdle extends KnightBossState {
	KnightBoss knight;
	int idleTime = 300;
	int counter;
	int dec;
	boolean lastStand;
	
	public KnightBossIdle(KnightBoss knight) {
		this.knight = knight;
		dec = 4;
	}
	
	@Override
	public void Enter() {
		if(!HUD.bossBar && knight.activated)
			HUD.enableBossHP("Don  Quixote  the  Knight");
		if (knight.getHealth() == 1) {
			if (!lastStand) {
				dec = 5;
				lastStand = true;
				counter = idleTime *2;
			} else {
				dec -= 1;
				counter = idleTime/2;
			}
		} else {
			dec -= 1;
			counter = idleTime;
		}
	}
	
	@Override
	public void Update() {
		if (counter <= 0) {	
			if (dec == 0 && !knight.prevState.equals(knight.stunnedState)) {
				if (knight.getHealth() > 1) {
				knight.changeState(knight.superJumpState);
				dec = 4;
				} else
					knight.changeState(knight.omegaState);
			} else {
				int r = (int) (Math.random()*3);
				if (r == 0) {
					knight.changeState(knight.chaseState);
				}
				if (r > 1) {
					knight.changeState(knight.randState);
				}
			}	
		} else if (knight.isGrounded())
				counter -= 1;
	}
}
