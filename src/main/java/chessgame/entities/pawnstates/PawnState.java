package chessgame.entities.pawnstates;

import chessgame.entities.IState;

/**
 * An abstract class to keep track of the different states, the Enemy "Pawn" can be in
 * 
 * @author Thorgal, Mikal
 *
 */
public abstract class PawnState implements IState{
	
	static PawnState PawnIdle, PawnMove, PawnChase, PawnHome, current;
	
	public abstract void Enter();

	public abstract void Update();
}
	
