package chessgame.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.InputMultiplexer;

import chessgame.entities.Player;
import chessgame.entities.playerstates.PlayerBishopState;
import chessgame.entities.playerstates.PlayerPawnState;
import chessgame.entities.playerstates.PlayerTowerState;
import chessgame.utils.HUD;
import chessgame.utils.SaveFile;
import chessgame.utils.playerForm;

public class PlayerController extends InputMultiplexer {
	
	//Movement
	private int playerspeed = 1;
	private float jumpForce = 500f;
	public boolean isGrounded = false;
	public boolean clearJump = true;
	//Ability
	public boolean lock = false;
	public boolean holdAbility = false;
	public float coolDown = 5;
	public float formCoolDown = 5;
	public float maxCoolDown = 2f;
	//KeyBinds
	private int up;
	private int left;
	private int right;
	private int sprint;
	private int useAbilty;
	private int changeform;
	private int progress;
	
	public PlayerController(int[] controls, ChessGame game){
		up = controls[0];
		left = controls[1];
		right = controls[2];
		sprint = controls[3];
		useAbilty = controls[7];
		changeform = controls[8];
		progress =(int) SaveFile.readProgress()[0];
	}
	
	public PlayerController(int[] controls){
		up = controls[0];
		left = controls[1];
		right = controls[2];
		sprint = controls[3];
		useAbilty = controls[7];
		changeform = controls[8];
		
		progress =(int) SaveFile.readProgress()[0];
	}
	
	public void myController(Player player) {
		if (!Game.paused) {
			
			if(player.currentState instanceof PlayerBishopState) {
				maxCoolDown = 5f;
			}
			if(player.currentState instanceof PlayerTowerState) {
				maxCoolDown = 1f;
			}
			
			if(Gdx.input.isKeyPressed(sprint))
				player.sprint = true;
			else 
			player.sprint = false;
			
			if(!lock) {
				//Movement inputs
				if(Gdx.input.isKeyPressed(right) && Gdx.input.isKeyPressed(left)) {
					player.move(Vector2.Zero);
				}
		    	if(Gdx.input.isKeyPressed(right)) {
			    	player.move(new Vector2(playerspeed, player.getVelocity().y));
			    	player.facing = true;
		    	}
		    	else if(Gdx.input.isKeyPressed(left)) {
		    		player.move(new Vector2(-playerspeed, player.getVelocity().y));
		    		player.facing = false;
		    	}
		    	else 
		    		player.move(new Vector2(0, player.getVelocity().y));
		
		    	if((Gdx.input.isKeyPressed(up))) {
		    		if(isGrounded && clearJump) {
		    			player.move(Vector2.Zero);
			    		player.jump(jumpForce);
			    		isGrounded = false;
		    		}
		    	}
		    	if(formCoolDown >= .15f) {
			    	if(Gdx.input.isKeyJustPressed(changeform)) {
			    		if(progress >= 3) {
				    		player.nextState();
				    		player.changingForm = true;
				    		HUD.setAbility(player.currentState.form);
				    		formCoolDown = 0;
				    		coolDown = 5;
			    		}
			    	}
		    	}
		    	if(coolDown >= maxCoolDown) {
		    		if(!(player.currentState instanceof PlayerPawnState)) {
			    		HUD.setCharge(true);
				    	if(!holdAbility) {
					    	if(Gdx.input.isKeyJustPressed(useAbilty)) {
					    		player.currentState.stateAbility();
					    		coolDown = 0;
					    		HUD.setCharge(false);
					    	}
				    	}
			    	}
		    	}
			}
	    	if(Gdx.input.isKeyPressed(useAbilty) && holdAbility && isGrounded) {
	    		player.currentState.stateAbility();
	    	} else if(holdAbility) {
	    		lock = false;
	    	}
		}
    	if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
    		Game.pauseGame();
    	}
	}
}
