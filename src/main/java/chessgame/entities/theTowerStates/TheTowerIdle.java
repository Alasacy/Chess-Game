package chessgame.entities.theTowerStates;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;

import chessgame.entities.TheTower;

public class TheTowerIdle extends TheTowerState{
	TheTower tower;
	float pauseTime;
	List<TheTowerState> attacks = new ArrayList<TheTowerState>();
	
	public TheTowerIdle(TheTower tower){
		this.tower = tower;
	}
	@Override
	public void Enter() {
		pauseTime = 0;
		attacks.add(tower.smashState);
		attacks.add(tower.jumpState);
		tower.jumpCounter = (int) Math.ceil(3f / tower.getHealth()) - 1;
	}

	@Override
	public void Update() {
		pauseTime += Gdx.graphics.getDeltaTime();
		if(tower.getClosestPlayer(100f) != null && pauseTime >= 1) {
			Random rand = new Random();	
			tower.changeState(attacks.get(rand.nextInt(attacks.size())));
		}
	}
}
