package chessgame.entities.theTowerStates;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import chessgame.entities.Door;
import chessgame.entities.TheTower;
import chessgame.utils.EntityManager;
import chessgame.utils.GameSound;

public class TheTowerDormant extends TheTowerState{
	TheTower tower;
	public TheTowerDormant(TheTower tower){
		this.tower = tower;
	}
	@Override
	public void Enter() {
		tower.dormant = true;
		EntityManager entityManager = tower.entityManager;
		if(entityManager.doorMap.containsKey(0)) {
			Door door = entityManager.doorMap.get(0);
			door.doorState();
		}
		if(entityManager.doorMap.containsKey(1)) {
			Door door = entityManager.doorMap.get(1);
			door.doorState();
		}
	}

	@Override
	public void Update() {
		if(tower.getClosestPlayer(3.5f) != null) {
			EntityManager entityManager = tower.entityManager;
			if(entityManager.doorMap.containsKey(0)) {
				Door door = entityManager.doorMap.get(0);
				door.doorState();
			}
			tower.dormant = false;
			GameSound.playMusic(1);
			tower.changeState(tower.idleState);
		}
	}
}
