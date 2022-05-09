package chessgame.entities.pawnstates;

import chessgame.entities.Pawn;

public class PawnHome extends PawnState{
	Pawn pawn;
	
	public PawnHome(Pawn pawn) {
		this.pawn = pawn;
	}
		
		
	@Override
	public void Enter() {
		
	}

	@Override
	public void Update() {
		if(pawn.getClosestPlayer(pawn.aggroRange) != null) {
			pawn.changeState(pawn.chaseState);
		}
		else if(Math.abs(pawn.getPosition().x - pawn.homePosition.x) < 1f && Math.abs(pawn.getPosition().y - pawn.homePosition.y) < 1f) {
			pawn.changeState(pawn.idleState);
		}
		else{
			pawn.moveTo(pawn.homePosition);
		}
	}
}
