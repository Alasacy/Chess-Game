package chessgame.entities.bishopStates;

import com.badlogic.gdx.Gdx;

import chessgame.entities.Bishop;

public class BishopFireState extends BishopState{
	
	Bishop bishop;
	float waitTime;
	boolean shoot;
	
	public BishopFireState(Bishop bishop) {
		this.bishop = bishop;
	}
	
	@Override
	public void Enter() {
		waitTime = 0;
		bishop.getSprite().setTexture(bishop.bishopAttackTex);
	}

	@Override
	public void Update() {
		waitTime += Gdx.graphics.getDeltaTime();
		if(!shoot) {
			bishop.shootBullet(bishop.getClosestPlayer(100f));
			shoot = true;
		}
		
		if(waitTime > 3f) {
			bishop.changeState(bishop.bishopIdleState);
			shoot = false;
		}
	}

}
