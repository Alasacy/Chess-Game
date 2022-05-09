package chessgame.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class GameSound {

	
	static float volume = ((float)SaveFile.readSettings()[4])/100;
	
	static Sound click = Gdx.audio.newSound(Gdx.files.internal("assets/sound/menuClick.mp3"));
	
	static Sound overworld = Gdx.audio.newSound(Gdx.files.internal("assets/sound/overworld.mp3"));
	static Sound boss1 = Gdx.audio.newSound(Gdx.files.internal("assets/sound/boss1.mp3"));
	static Sound boss2 = Gdx.audio.newSound(Gdx.files.internal("assets/sound/boss2.mp3"));
	static Sound castle =  Gdx.audio.newSound(Gdx.files.internal("assets/sound/castle.mp3"));
	static Sound boss3 = Gdx.audio.newSound(Gdx.files.internal("assets/sound/boss3.mp3"));
	static Sound afterBoss = Gdx.audio.newSound(Gdx.files.internal("assets/sound/afterBoss.mp3"));
	
	static Sound[] musicList = new Sound[] {
			overworld,
			boss1,
			boss2,
			castle,
			boss3,
			afterBoss
	};
	
	static Sound bell = Gdx.audio.newSound(Gdx.files.internal("assets/sound/snd_bell.wav"));
	static Sound lowimpact = Gdx.audio.newSound(Gdx.files.internal("assets/sound/snd_impact_low.mp3"));
	static Sound impact = Gdx.audio.newSound(Gdx.files.internal("assets/sound/snd_impact.wav"));
	static Sound coin = Gdx.audio.newSound(Gdx.files.internal("assets/sound/snd_coin.wav"));
	static Sound highJump = Gdx.audio.newSound(Gdx.files.internal("assets/sound/snd_highjump.wav"));
	static Sound jump = Gdx.audio.newSound(Gdx.files.internal("assets/sound/jump.mp3"));
	
	static Sound[] soundEffectList = new Sound[] {
		bell,
		lowimpact,
		impact,
		coin,
		highJump,
		jump,
	};
	
	static Sound currentMusic = click;
	static long currentMusicID ;
	static int currentMusicIndex;
	
	
	public static void buttonSound() {
		click.play(volume);
	}
	
	public static int[] newAudiolvl(int value, int[] controls) {
		controls[4] = value;
		volume = (float)value/100;
		return controls;
	}
	
	public static void playMusic(int index) {
		currentMusic = musicList[index];
		currentMusicID = currentMusic.play(volume);
		currentMusicIndex = index;
		currentMusic.setLooping(currentMusicID, true);
	}
	
	public static void stopMusic() {
		currentMusic.stop();
	}
	
	public static void playSoundEffect(int index, float volumeMOD) {
		soundEffectList[index].play(volume*volumeMOD);
	}
	
}
