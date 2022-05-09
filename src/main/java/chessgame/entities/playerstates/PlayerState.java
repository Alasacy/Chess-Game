package chessgame.entities.playerstates;

import chessgame.entities.IState;
import chessgame.entities.pawnstates.PawnState;
import chessgame.utils.playerForm;

public abstract class PlayerState implements IState {
	public playerForm form;
	static PawnState PlayerPawnState, PlayerKnightState, PlayerBishopState, PlayerTowerState, current;
	
	public abstract void Enter();

	public abstract void Update();

	public abstract void stateAbility();
}
