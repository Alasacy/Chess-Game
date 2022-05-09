package chessgame.entities.bishopStates;

import com.badlogic.gdx.math.Vector2;

import chessgame.entities.Bishop;
import chessgame.entities.Player;

public class BishopRetreatState extends BishopState {
	
	Bishop bishop;
	boolean retreat;
	
	public BishopRetreatState(Bishop bishop){
		this.bishop = bishop;
	}
	
	@Override
	public void Enter() {
		retreat = false;
	}

	@Override
	public void Update() {
		Player player = bishop.getClosestPlayer(100f);
		float xVal = player.getPosition().x - bishop.getPosition().x;
		
		if(!retreat) {
			if(xVal < 0) {
				bishop.getBody().applyLinearImpulse(new Vector2(50f,50f), bishop.getPosition(), true);
			} else {
				bishop.getBody().applyLinearImpulse(new Vector2(-50f,50f), bishop.getPosition(), true);
			}
			retreat = true;
		}
		if(bishop.getBody().getLinearVelocity().y == 0f && retreat) {
			bishop.getBody().setLinearVelocity(Vector2.Zero);
			bishop.changeState(bishop.bishopIdleState);
		}
	}

}
