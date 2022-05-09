package chessgame.entities.playerstates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import chessgame.entities.Bullet;
import chessgame.utils.Direction;
import chessgame.utils.playerForm;
import chessgame.entities.Player;

public class PlayerBishopState extends PlayerState {
	Player player;
	
	public PlayerBishopState(Player player) {
		this.player = player;
		form = playerForm.BISHOP;
	}
	
	@Override
	public void Enter() {
		player.controller.coolDown = 0;
		Texture bishopSprite = new Texture (Gdx.files.internal("assets/player/Bishop.png").file().getAbsolutePath());
		player.getSprite().setTexture(bishopSprite);
		player.controller.holdAbility = false;
	}

	@Override
	public void Update() {
		if(player.controller.coolDown < player.controller.maxCoolDown) {
			player.controller.coolDown += Gdx.graphics.getDeltaTime();
		}
		if(player.controller.formCoolDown < 0.15f) {
			player.controller.formCoolDown += Gdx.graphics.getDeltaTime();
		}
	}

	@Override
	public void stateAbility() {
		Vector2 spawnPosition;
		Direction dir;
		if(player.facing) {
			spawnPosition = new Vector2(player.getPosition().x + 1, player.getPosition().y);
			dir = Direction.RIGHT;
		}else {
			spawnPosition = new Vector2(player.getPosition().x - 1, player.getPosition().y);
			dir = Direction.LEFT;
		}
		
		Bullet b = new Bullet(spawnPosition, player.world, player.manager, dir, true);
		b.initialize();
		b.setBulletSpeed(20f);
	}

}
