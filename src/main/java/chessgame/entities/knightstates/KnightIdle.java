package chessgame.entities.knightstates;

import chessgame.entities.Knight;

public class KnightIdle extends KnightState {
	Knight knight;
	int counter;
	int initialTime = 600;	
	
	public KnightIdle(Knight knight) {
		this.knight = knight;
	}
	
	@Override
	public void Enter() {
		counter = initialTime;
	}
	
	@Override
	public void Update() {
		if(knight.getClosestPlayer(knight.aggroRange) != null) {
			knight.changeState(knight.chaseState);
		}
		if(counter <= 0) {
			knight.changeState(knight.homeState);				
		}
		
		counter -= 1;
	}
}
