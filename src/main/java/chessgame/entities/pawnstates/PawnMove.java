package chessgame.entities.pawnstates;

import chessgame.entities.Pawn;

public class PawnMove extends PawnState{
	Pawn pawn;
	
	public PawnMove(Pawn pawn) {
		this.pawn = pawn;
	}
		
		
	@Override
	public void Enter() {
	}

	@Override
	public void Update() {
		pawn.changeState(pawn.idleState);
	}
}
