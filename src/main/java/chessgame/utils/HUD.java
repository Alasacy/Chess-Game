package chessgame.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Scaling;

import chessgame.entities.IEnemies;
/**
 * A visual representation of the players stats
 * and other attributes of the game
 *
 */
public class HUD {
	static int health;
	static boolean ability;
	int time;
	int score;
	static playerForm currentForm;
	static Stage stage;
	//Form icons
	static Image currentIcon;
	static Image pawnIcon;
	static Image rookIcon;
	static Image knightIcon;
	static Image bishopIcon;
	//PlayerHp
	static Image emptyheartIcon1;
	static Image emptyheartIcon2;
	static Image emptyheartIcon3;
	static Image heartIcon1;
	static Image heartIcon2;
	static Image heartIcon3;
	//PlayerAbility
	static Image abilityCrystal;
	static Image emptyAbilityCrystal;
	//Boss HP Bar
	static Image hpBar;
	static Image hpBarBorder;
	static Image bossSkull;
	public static boolean bossBar;
	
	public HUD(Stage stage) {
		bossBar = false;
		HUD.stage = stage;
		Image bg = UI.image(new Vector2(6.93f,2.1f), new Vector2(.5f, 13.8f), "assets/hud/hudBG.png");
		pawnIcon = UI.image(new Vector2(1.775f,1.8f), new Vector2(.7f, 13.95f), "assets/hud/PawnIcon.png"); 
		rookIcon = UI.image(new Vector2(1.775f,1.8f), new Vector2(.7f, 14f), "assets/hud/TowerIcon.png"); 
		knightIcon = UI.image(new Vector2(1.775f,1.8f), new Vector2(.7f, 14f), "assets/hud/horseIcon.png");
		bishopIcon = UI.image(new Vector2(1.775f,1.8f), new Vector2(.7f, 14f), "assets/hud/BishopIcon.png");
		currentIcon = pawnIcon;
		
		emptyheartIcon1 = UI.image(new Vector2(1.3f,1.3f), new Vector2(2.8f, 14.23f), "assets/hud/emptyPawnHeart.png");
		emptyheartIcon2 = UI.image(new Vector2(1.3f,1.3f), new Vector2(3.8f, 14.23f), "assets/hud/emptyPawnHeart.png");
		emptyheartIcon3 = UI.image(new Vector2(1.3f,1.3f), new Vector2(4.8f, 14.23f), "assets/hud/emptyPawnHeart.png");
		
		heartIcon1 = UI.image(new Vector2(1.3f,1.3f), new Vector2(2.8f, 14.23f), "assets/hud/pawnHeart.png");
		heartIcon2 = UI.image(new Vector2(1.3f,1.3f), new Vector2(3.8f, 14.23f), "assets/hud/pawnHeart.png");
		heartIcon3 = UI.image(new Vector2(1.3f,1.3f), new Vector2(4.8f, 14.23f), "assets/hud/pawnHeart.png");
		
		abilityCrystal = UI.image(new Vector2(1.3f,1.3f), new Vector2(6.205f, 14.23f), "assets/hud/AbilityGem2.png");
		emptyAbilityCrystal = UI.image(new Vector2(1.3f,1.3f), new Vector2(6.205f, 14.23f), "assets/hud/AbilityGem1.png");
		
		
		stage.addActor(bg);
		
		stage.addActor(emptyheartIcon1);
		stage.addActor(emptyheartIcon2);
		stage.addActor(emptyheartIcon3);
		stage.addActor(heartIcon1);
		stage.addActor(heartIcon2);
		stage.addActor(heartIcon3);
		
		stage.addActor(emptyAbilityCrystal);
		stage.addActor(abilityCrystal);
		
		stage.addActor(currentIcon);
	}
	
	public static void setHP(int hp) {
		if(hp <= 3 && hp >= 0)
			health = hp;
		if(hp == 3) {
			stage.addAction(Actions.targeting(heartIcon3, Actions.alpha(1)));
		}
		if(hp == 2) {
			stage.addAction(Actions.targeting(heartIcon3, Actions.alpha(0)));
			stage.addAction(Actions.targeting(heartIcon2, Actions.alpha(1)));
		}
		if(hp == 1) {
			stage.addAction(Actions.targeting(heartIcon3, Actions.alpha(0)));
			stage.addAction(Actions.targeting(heartIcon2, Actions.alpha(0)));
			stage.addAction(Actions.targeting(heartIcon1, Actions.alpha(1)));
		}
		if(hp == 0) {
			stage.addAction(Actions.targeting(heartIcon3, Actions.alpha(0)));
			stage.addAction(Actions.targeting(heartIcon2, Actions.alpha(0)));
			stage.addAction(Actions.targeting(heartIcon1, Actions.alpha(0)));
		}
	}
	
	
	public static void setAbility(playerForm state) {
		stage.addAction(Actions.removeActor(currentIcon));
		currentForm = state;
		if(currentForm == playerForm.PAWN)
			currentIcon = pawnIcon;
		if(currentForm == playerForm.ROOK)
			currentIcon = rookIcon;
		if(currentForm == playerForm.KNIGHT)
			currentIcon = knightIcon;
		if(currentForm == playerForm.BISHOP)
			currentIcon = bishopIcon;
		
		stage.addActor(currentIcon);
	}
	
	public static void setCharge(boolean active) {
		if(active)
			stage.addAction(Actions.targeting(abilityCrystal, Actions.alpha(1)));
		else
			stage.addAction(Actions.targeting(abilityCrystal, Actions.alpha(0)));
	}
	public static void enableBossHP(String name) {
		bossBar = true;
		Label bossName = UI.label(new Vector2(10,10), new Vector2(8.8f, 10.5f), name, "default");
		hpBar = UI.image(new Vector2(320/35f,1/1.3f), new Vector2(9.1f, 14.35f), "assets/hud/HpBar.png");
		hpBarBorder = UI.image(new Vector2(384/35f,64/35f), new Vector2(8f, 13.8f), "assets/hud/HpBarBorder.png");
		bossSkull = UI.image(new Vector2(384/35f,64/35f), new Vector2(8f, 13.8f), "assets/hud/HpBarSkull.png");
		stage.addActor(hpBarBorder);
		stage.addActor(hpBar);
		stage.addActor(bossSkull);
		stage.addActor(bossName);
	}
	
	public static void BossHp(float maxHp, float hpTaken) {
		hpBar.scaleBy(-(hpTaken/maxHp), 0);
	}
}