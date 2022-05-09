package chessgame.entities.theTowerStates;

import chessgame.entities.Player;
import chessgame.entities.TheTower;

public class TheTowerJump extends TheTowerState{
	TheTower tower;
	
	public TheTowerJump(TheTower tower) {
		this.tower = tower;
	}
	
	@Override
	public void Enter() {
		tower.jumpCounter --;
	}

	@Override
	public void Update() {
		Player player = tower.getClosestPlayer(100f);

		if(!tower.jump && !tower.shockWave) {
			tower.jump();
		}
		tower.checkShockWave();
	}
}
