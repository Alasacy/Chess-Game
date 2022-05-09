package chessgame.entities.knightstates;

import chessgame.entities.Knight;

public class KnightHome extends KnightState {
	Knight knight;
	boolean sleeping;
	
	public KnightHome(Knight knight) {
		this.knight = knight;
	}
	
	@Override
	public void Enter() {
		sleeping = false;
	}
	
	@Override
	public void Update() {
		if(knight.getClosestPlayer(knight.aggroRange) != null) {
			knight.changeState(knight.chaseState);
		} 
		else if(knight.homePosition.x-0.1f < knight.getPosition().x && knight.getPosition().x < knight.homePosition.x+0.1f && !sleeping) {
			knight.getBody().setLinearVelocity(0f,knight.getBody().getLinearVelocity().y );
			knight.setSprite("assets/enemies/BigKnightSleeping.png");
			if (knight.lookingRight)
				knight.getSprite().flip(true, false);
			sleeping = true;
		} 
		else if (!sleeping){
			knight.moveTo(knight.homePosition);
		}
	}
}
