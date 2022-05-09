package chessgame.entities.bishopStates;

import com.badlogic.gdx.Gdx;

import chessgame.entities.Bishop;
import chessgame.entities.Player;

public class BishopIdleState extends BishopState{
	
	Bishop bishop;
	float waitTime;
	
	public BishopIdleState(Bishop bishop){
		this.bishop = bishop;
	}
	
	@Override
	public void Enter() {
		waitTime = 0;
		bishop.getSprite().setTexture(bishop.bishopTex);
	}

	@Override
	public void Update() {
		waitTime += Gdx.graphics.getDeltaTime();
		Player player = bishop.getClosestPlayer(10f);
		if(player != null) {
			
			if(Math.abs(player.getPosition().x - bishop.getPosition().x) < 3.5f && player.getPosition().y < bishop.getPosition().y -.3f)
				bishop.changeState(bishop.bishopRetreatState);
			else if(waitTime > 1f) {
				bishop.changeState(bishop.bishopFireState);
			}
		}
	}

}
