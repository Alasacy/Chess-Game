package chessgame.entities.playerstates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import chessgame.entities.Player;
import chessgame.utils.playerForm;

public class PlayerPawnState extends PlayerState {
	Player player;
	
	public PlayerPawnState(Player player){
		this.player = player;
		form = playerForm.PAWN;
	}
	
	@Override
	public void Enter() {
		player.controller.coolDown = 0;
		Texture pawnSprite = new Texture (Gdx.files.internal("assets/player/player.png").file().getAbsolutePath());
		player.getSprite().setTexture(pawnSprite);
		player.controller.holdAbility = false;
	}

	@Override
	public void Update() {
		if(player.controller != null) {
			if(player.controller.coolDown < player.controller.maxCoolDown) {
				player.controller.coolDown += Gdx.graphics.getDeltaTime();
			}
			if(player.controller.formCoolDown < 0.15f) {
				player.controller.formCoolDown += Gdx.graphics.getDeltaTime();
			}
		}
	}

	@Override
	public void stateAbility() {
		// TODO Auto-generated method stub
		
	}

}
