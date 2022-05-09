package chessgame.entities.playerstates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import chessgame.entities.Player;
import chessgame.utils.GameSound;
import chessgame.utils.playerForm;

public class PlayerKnightState extends PlayerState {
	
	Player player;
	boolean hasJumped = false;
	boolean hasCharged = false;
	boolean charging = false;
	float charge = 0;
	float jumpTime = 0;
	
	float colorTime = 0;
	
	public PlayerKnightState(Player player){
		this.player = player;
		form = playerForm.KNIGHT;
	}
	
	@Override
	public void Enter() {
		player.controller.coolDown = 0;
		Texture knightSprite = new Texture (Gdx.files.internal("assets/player/Knight.png").file().getAbsolutePath());
		player.getSprite().setTexture(knightSprite);
		player.controller.holdAbility = true;
	}

	@Override
	public void Update() {
		if(player.controller.coolDown < player.controller.maxCoolDown) {
			player.controller.coolDown += Gdx.graphics.getDeltaTime();
		}
		if(player.controller.formCoolDown < 0.15f) {
			player.controller.formCoolDown += Gdx.graphics.getDeltaTime();
		}
		if(!player.controller.lock && hasCharged) {
			jumpTime += Gdx.graphics.getDeltaTime();
			player.jump(500f);
			player.controller.isGrounded = false;
			player.getSprite().setColor(player.getPlayerColor());
			GameSound.playSoundEffect(4, 1);
		}
		if(jumpTime > 0.05f * charge) {
			hasCharged = false;
			jumpTime = 0;
			charge = 0;
		}
	}

	@Override
	public void stateAbility() {
		if(charge < 3) {
			if(charge*5 % 2 > 1f) {
				player.getSprite().setColor(Color.YELLOW);
			} else {
				player.getSprite().setColor(Color.WHITE);
			}
			charge += Gdx.graphics.getDeltaTime()*2;
			player.controller.lock = true;
			player.myBody.setLinearVelocity(Vector2.Zero);
			hasCharged = true;
		} else {
			charge = 3;
			hasCharged = true;
		}
	}
}
