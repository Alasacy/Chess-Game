package chessgame.entities.pawnstates;

import com.badlogic.gdx.math.Vector2;

import chessgame.entities.Pawn;

public class PawnIdle extends PawnState {
	Pawn pawn;
	int counter;
	int initialTime = 600;	
	
	public PawnIdle(Pawn pawn) {
		this.pawn = pawn;
	}
		
		
	@Override
	public void Enter() {
		pawn.getBody().setLinearVelocity(Vector2.Zero);
		counter = initialTime;
	}

	@Override
	public void Update() {
		if(pawn.getClosestPlayer(pawn.aggroRange) != null) {
			pawn.changeState(pawn.chaseState);
		}
		if(counter <= 0) {
			counter = initialTime;
			float rand = (float) Math.random();
			if(rand > 0.3f) //30% chance
				pawn.changeState(pawn.homeState);
			else
				pawn.changeState(pawn.moveState);
				
		}
		
		counter -= 1;
	}
}
