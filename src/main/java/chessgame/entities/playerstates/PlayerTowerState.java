package chessgame.entities.playerstates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import chessgame.entities.Player;
import chessgame.utils.playerForm;

public class PlayerTowerState extends PlayerState {
	double time = 0;
	Player player;
	boolean hasDash;
	
	public PlayerTowerState(Player player) {
		this.player = player;
		form = playerForm.ROOK;
	}
	
	@Override
	public void Enter() {
		player.controller.coolDown = 0;
		Texture towerSprite = new Texture (Gdx.files.internal("assets/player/Rook.png").file().getAbsolutePath());
		player.getSprite().setTexture(towerSprite);
		player.controller.holdAbility = false;
	}

	@Override
	public void Update() {
		
		player.dash = hasDash;
		
		if(player.controller.coolDown < player.controller.maxCoolDown) {
			player.controller.coolDown += Gdx.graphics.getDeltaTime();
		}
		if(player.controller.formCoolDown < 0.15f) {
			player.controller.formCoolDown += Gdx.graphics.getDeltaTime();
		}
		
		if(System.currentTimeMillis() > time + 250) {
			player.controller.lock = false;
			if(hasDash) {
				player.move(new Vector2(.0001f * player.myBody.getLinearVelocity().x, 0f));
				hasDash = false;
			}
		}
	}
	
	@Override
	public void stateAbility() {
		if(player.facing) {
			player.myBody.applyLinearImpulse(new Vector2(500, 0), player.getPosition(), true);
			player.controller.lock = true;
			time = System.currentTimeMillis();
			hasDash = true;
		}
		else {
			player.myBody.applyLinearImpulse(new Vector2(-500f, 0), player.getPosition(), true);
			player.controller.lock = true;
			time = System.currentTimeMillis();
			hasDash = true;
		}
	}

}
