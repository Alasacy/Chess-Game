package chessgame.entities.pawnstates;

import chessgame.entities.IState;

/**
 * An abstract class to keep track of the different states, the Enemy "Pawn" can be in
 * 
 *
 */
public abstract class PawnState implements IState{
	
	static PawnState PawnIdle, PawnMove, PawnChase, PawnHome, current;
	
	public abstract void Enter();

	public abstract void Update();
}
	
