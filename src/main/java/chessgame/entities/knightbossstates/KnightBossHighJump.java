package chessgame.entities.knightbossstates;

import chessgame.entities.KnightBoss;

public class KnightBossHighJump extends KnightBossState{
KnightBoss knight;
	
	public KnightBossHighJump(KnightBoss knight) {
		this.knight = knight;
	}
	
	@Override
	public void Enter() {
		knight.superJump(50f,true);
	}
	
	@Override
	public void Update() {
		if (knight.isGrounded()) {
			knight.changeState(knight.stunnedState);
		}
	}
}
