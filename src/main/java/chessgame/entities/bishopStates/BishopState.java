package chessgame.entities.bishopStates;

import chessgame.entities.IState;

public abstract class BishopState implements IState {

	@Override
	abstract public void Enter();

	@Override
	abstract public void Update();

}
