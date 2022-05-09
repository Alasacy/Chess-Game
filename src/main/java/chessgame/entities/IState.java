package chessgame.entities;

public interface IState {
	/**
	 * What happens when the entity enters the state.
	 */
	public void Enter();
	/**
	 * What happens in the state, and what triggers it to change state
	 */
	public void Update();
}
