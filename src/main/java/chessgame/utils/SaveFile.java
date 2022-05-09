package chessgame.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;

public class SaveFile {
	
	/**
	 * Changes the controls in the settings file back to default (W,A,D,shift).
	 */
	public static void defaultControls() {
		int[] currentSettings = readSettings();
		int audio = currentSettings[4];
		int res = currentSettings[5];
		int fullScreen = currentSettings[6];
		FileHandle file = Gdx.files.local("savefiles/settings.txt");
		file.writeBytes(new byte[] {Keys.W, Keys.A, Keys.D, Keys.SHIFT_LEFT, (byte) audio, (byte) res, (byte) fullScreen, Keys.C, Keys.V}, false);
	}
	
	/**
	 * Reads the settings file and returns its data.
	 * 
	 * @return
	 * 
	 * [0]=up input,
	 * [1]=left input,
	 * [2]=right input,
	 * [3]=sprint,
	 * [4]=audiolvl
	 * [5]=resolution
	 * [6]=fullscreen
	 * 
	 * @author Åsmund
	 */
	public static int[] readSettings() {
		FileHandle file = Gdx.files.local("savefiles/settings.txt");
		byte[] input = file.readBytes();
		int[] output = {0,1,2,3,4,5,6,7,8};
		int i = 0;
		//Some key values are larger than the byte limit of 2^7-1 and needs to be converted back to their original int.
		for (byte value : input) {
			if (value < 0)
				output[i] = value+256;
			else
				output[i] = value;
			i++;
		}
		return output;
	}
	
	public static Vector2 getResolution() {
		int resIndex = readSettings()[5];
		return Constants.resolutions[resIndex];
	}
	
	/**
	 * Writes the input data to the settings file.
	 * 
	 * @param data Data to be saved.
	 * @see
	 * [0]=up input,
	 * [1]=left input,
	 * [2]=right input,
	 * [3]=sprint,
	 * [4]=audiolvl
	 * [5]=resolution
	 * [6]=fullscreen
	 * 
	 * @author Åsmund
	 */
	public static void writeSettings(byte[] data) {
		FileHandle file = Gdx.files.local("savefiles/settings.txt");
		file.writeBytes(data, false);
	}
	
	/**
	 * Writes the input data to the settings file.
	 * 
	 * @param data Data to be saved.
	 * @see
	 * [0]=up input,
	 * [1]=left input,
	 * [2]=right input,
	 * [3]=sprint,
	 * [4]=audiolvl
	 * [5]=resolution
	 * [6]=fullscreen
	 * [7]=useAbilty input.
	 * [8]=changeAbilty input.
	 * 
	 * @author Åsmund
	 */
	public static void writeSettings(int[] data) {
		byte[] output = {0,1,2,3,4,5,6,7,8};
		for (int i = 0; i<output.length; i++) {
			output[i] = (byte) data[i];
		}
		writeSettings(output);
	}
	/**
	 * Writes the input data to the specified location in the settings file.
	 * 
	 * @param data New data to be saved
	 * @param index Index of data in settings file.
	 * @see
	 * [0]=up input,
	 * [1]=left input,
	 * [2]=right input,
	 * [3]=sprint,
	 * [4]=audiolvl
	 * [5]=resolution
	 * [6]=fullscreen
	 * 
	 * @author Åsmund
	 */
	public static void writeSingleSetting(byte data, int index) {
		FileHandle file = Gdx.files.local("savefiles/settings.txt");
		byte[] controls = file.readBytes();
		controls[index] = data;
		file.writeBytes(controls, false);
	}
	
	/**
	 * Reads the progress file and returns data about player progression.
	 * 
	 * @return
	 * 
	 * [0]=level progress
	 * 
	 * @author Åsmund
	 */
	public static byte[] readProgress() {
		FileHandle file = Gdx.files.local("savefiles/progress.txt");
		return file.readBytes();
	}
	
	/**
	 * Writes level progress to the progression file. Will only update if the level is a new one.
	 * 
	 * @param data Unlocked level
	 * 
	 * @author Åsmund
	 */
	public static void writeProgress(int data) {
		FileHandle file = Gdx.files.local("savefiles/progress.txt");
		if (file.readBytes()[0] < data)
			file.writeBytes(new byte[] {(byte) data}, false);
	}
	
	public static void writeScore(int data) {
		FileHandle file = Gdx.files.local("savefiles/score.txt");
		int firstByte = data % 128;
		int secByte = Math.floorDiv(data, 128);
		file.writeBytes(new byte[] {(byte) firstByte, (byte) secByte}, false);
	}
	
	public static int readScore() {
		FileHandle file = Gdx.files.local("savefiles/score.txt");
		byte[] data = file.readBytes();
		return data[0]+data[1]*128;
	}
	
	/**
	 * Resets the settingsfile to its default state
	 */
	public static void totalReset() {
		FileHandle setting = Gdx.files.local("savefiles/settings.txt");
		setting.writeBytes(new byte[] {Keys.W, Keys.A, Keys.D, Keys.SHIFT_LEFT, 50, 1, 0, Keys.C, Keys.V}, false);
		FileHandle progress = Gdx.files.local("savefiles/progress.txt");
		progress.writeBytes(new byte[] {0}, false);
		FileHandle score = Gdx.files.local("savefiles/score.txt");
		score.writeBytes(new byte[] {0, 0}, false);
	}
}
