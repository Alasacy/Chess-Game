package chessgame.entities.pawnstates;

import chessgame.entities.Pawn;
import chessgame.entities.Player;

public class PawnChase extends PawnState{
	Pawn pawn;
	
	public PawnChase(Pawn pawn) {
		this.pawn = pawn;
	}
		
		
	@Override
	public void Enter() {
	}

	@Override
	public void Update() {
		Player target = pawn.getClosestPlayer(pawn.aggroRange + 2f);
		
		//checks whether to stay in state or change state
		if(target == null) {
			pawn.changeState(pawn.idleState);
		}
		else {
			//A damper to make the enemy less erratic, when player is above him.
			float damper = target.getPosition().x - pawn.getPosition().x;
			damper = Math.abs(damper);
			
			if(damper > .5f)
				pawn.moveTo(target.getPosition());	
		}
	}
}
