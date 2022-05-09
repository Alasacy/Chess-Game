package chessgame.entities.theTowerStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import chessgame.entities.Player;
import chessgame.entities.TheTower;

public class TheTowerSmash extends TheTowerState{
	
	TheTower tower;
	boolean smash;
	boolean hasRun;
	float returnTime = 0;
	float waitTime = 0;
	
	String hand = "";
	
	public TheTowerSmash(TheTower tower){
		this.tower = tower;
	}
	
	@Override
	public void Enter() {
		hasRun = false;
	}

	@Override
	public void Update() {
		this.smash = tower.smash;
		
		Player player = tower.getClosestPlayer(100f);
		if(player != null) {
		float playerXDif = player.getPosition().x - tower.getPosition().x;
		float playerAbs = Math.abs(playerXDif);
		
		if(hasRun && !tower.returnHand)
			tower.changeState(tower.idleState);
		
		if((tower.isHandsHome() || hand == "") && !smash && !tower.returnHand) {
			if(playerXDif <= 0) {
				hand = "left";
				tower.getHand("right").setLinearVelocity(Vector2.Zero);
				tower.getHand("right").getFixtureList().get(0).setSensor(false);
			} else {
				hand = "right";
				tower.getHand("left").setLinearVelocity(Vector2.Zero);
				tower.getHand("left").getFixtureList().get(0).setSensor(false);
			}
		}
		if(tower.isHandsHome() && !tower.returnHand)
			returnTime = 0;
		
		if(!smash && !tower.returnHand) {
			int speed = 6;
			if(tower.getHealth() == 2)
				speed = 10;
			if(tower.getHealth() == 1)
				speed = 13;
			if(tower.getClosestPlayer(100f) != null && playerAbs > 3.8f) {
				waitTime = 0;
				tower.moveHandTo(hand, tower.getClosestPlayer(100f).getPosition(), false, speed);
			}
			else if(tower.isHandsHome()){
				tower.changeState(tower.jumpState);
			}
		} 
		else {
			waitTime += Gdx.graphics.getDeltaTime();
			//Makes hand wait before smashing down on player.
			if(waitTime >= 1) {
				if(!tower.returnHand) {
					tower.smashHand(hand);
				}
				else if (tower.quickReturn && tower.returnHand) {
					tower.freezeHands(false);
					tower.returnHand(hand);
					hasRun = true;
				}
				//Makes tower wait before lifting hand
				else if (tower.returnHand) {
					returnTime += Gdx.graphics.getDeltaTime();
					if(returnTime >= 3) {
						tower.freezeHands(false);
						tower.returnHand(hand);
						hasRun = true;
					}
				}
			}
		}
	}
	}
}
