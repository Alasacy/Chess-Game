package chessgame.entities.knightstates;

import chessgame.entities.IState;

public abstract class KnightState implements IState {

	static KnightState KnightIdle, KnightChase, KnightHome;
	
	public void Enter() {}

	public void Update() {}

}
