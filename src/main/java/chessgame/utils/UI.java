package chessgame.utils;

import chessgame.menues.*;

import java.io.IOException;

import chessgame.server.GameHost;
import chessgame.server.GameServer;
import chessgame.server.pings.FinishedPing;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Null;
import chessgame.app.ChessGame;
import chessgame.app.Game;
import chessgame.menues.HowToPlayScreen;
import chessgame.menues.LevelSelectScreen;
import chessgame.menues.MenuScreen;
import chessgame.menues.OptionScreen;


public class UI {
	
	//Scalable units for size and placements of UI
    static int rowHeight = Gdx.graphics.getHeight() / 16;
    static int colWidth = Gdx.graphics.getWidth() / 24;
    //Imported skin for UI
    static Skin skin = new Skin(Gdx.files.internal("assets/skin/chess/chess.json"));
    static Skin tempskin = new Skin(Gdx.files.internal("assets/skin/goldenspiralui/golden-ui-skin.json"));

    public static void updateScreenSize(int x, int y, boolean fullscreen) {
    	if (fullscreen)
    		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
    	else
    		Gdx.graphics.setWindowedMode(x,y);
    	rowHeight = Gdx.graphics.getHeight() / 16;
    	colWidth = Gdx.graphics.getWidth() / 24;
    }
    
    /**
     * Creates a "blank" button without a InputListener
     * 
     * @param size of the button
     * @param position on the screen by 16 rows and 24 columns
     * @param text of what the button is saying
     * @return a new TextButton
     * 
     * @author Åsmund
     */
    public static TextButton button(Vector2 size, Vector2 position, String text) {
		TextButton button = new TextButton(text, skin, "default");
		button.setSize(colWidth*size.x, rowHeight*size.y);
		button.setPosition(colWidth*position.x, rowHeight*position.y);
		return button;
	}
    
    /**
     * Creates a new TextButton that changes to a new screen on activation
     * 
     * @param size of the button
     * @param position on the screen by 16 rows and 24 columns
     * @param text of what the button is saying
     * @param screen to switch to
     * @param game 
     * @param Variable if set screen needs an extra variable
     * @return a new TextButton
     * 
     * @author Åsmund
     */
	public static TextButton newScreenButton(Vector2 size, Vector2 position, String text, ScreenType screen, ChessGame game,@Null int Variable) {
		TextButton button = button(size, position, text);
		button.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            	switch (screen) {
            	case MenuScreen:
            		game.setScreen(new MenuScreen(game));
            		break;
				case MultiPlayerScreen:
            		game.setScreen(new MultiPlayerScreen(game));
            		break;
					case HostScreen:
						game.setScreen(new HostScreen(game));
					break;
					case ClientScreen:
						game.setScreen(new ClientScreen(game));
					break;
            	case OptionScreen:
            		game.setScreen(new OptionScreen(game));
            		break;
				case Game:
					System.out.println("entersGame");
					try {
						if(Game.isMultiplayer == null) {
							System.out.println("creates new multiplayer game");
							game.setScreen(new Game(game, Variable, false, false, null));
						}
						else if(!Game.isMultiplayer) {
							System.out.println("creates new singleplayer game");
							game.setScreen(new Game(game, Variable, false, false, null));
						}
						if((Game.isMultiplayer != null) && (Game.isHost != null)) {
							if (Game.isMultiplayer) {
								Game.getClient().getClient().sendTCP(new FinishedPing(Variable));
							}

						}

					} catch (IOException e) {
						e.printStackTrace();
					}

					break;
				case Host:
					try {
						game.setScreen(new Game(game, Variable, true, true, null));
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				case Client:
					try {
						game.setScreen(new Game(game, Variable, true, false, null));
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				case HowToPlay:
					game.setScreen(new HowToPlayScreen(game));
					break;
				default:
					break;
            	}	
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            	GameSound.buttonSound();
                return true;
            }
        });
		return button;
	}

// quit button that checks if the player is host or not. If host, the server should stop, else nothing will happen to the server.
	public static TextButton quitButton(Vector2 size, Vector2 position, String text, ChessGame game, GameServer server, Boolean isHost){
		TextButton button = button(size, position, text);
		button.addListener(new InputListener() {
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new MenuScreen(game));
				if(Game.isMultiplayer) {
					if (isHost)
						server.stopServer();

					Game.isMultiplayer = false;
				}
			}
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				//click.play(volume);
				return true;
			}
		});

		return button;
	}

	public static Button connectButton(Vector2 size, Vector2 position, String text, ChessGame game, int Level, TextField ipField) {
		Button button = button(size, position, text);
		button.addListener(new InputListener() {
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				try {
					game.setScreen(new Game(game, Level, true, false, ipField.getText()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				//click.play(volume);
				return true;
			}
		});
		return button;
	}
	
	/**
	 * Creates a new Label with the given size, position, text and style
	 * 
	 * @param size of the text
	 * @param position on the screen by 16 rows and 24 columns
	 * @param text 
	 * @param style of the text defined the skin
	 * @return a new Label
	 * 
	 * @author Åsmund
	 */
	public static Label label(Vector2 size, Vector2 position, String text, String style) {
		Label label = new Label(text, skin, style);
		label.setSize(colWidth*size.x, rowHeight*size.y);
		label.setPosition(colWidth*position.x, rowHeight*position.y);
		label.setAlignment(Align.center);
		return label;
	}
	
	public static Image image(Vector2 size, Vector2 position, String imageLocation){
		Image image = new Image(new Texture(imageLocation));
		image.setSize(colWidth*size.x, rowHeight*size.y);
		image.setPosition(colWidth*position.x, rowHeight*position.y);
		image.setAlign(Align.center);
		return image;
	}
	
	public static Image image(Vector2 size, Vector2 position, Texture texture){
		Image image = new Image(texture);
		image.setSize(colWidth*size.x, rowHeight*size.y);
		image.setPosition(colWidth*position.x, rowHeight*position.y);
		image.setAlign(Align.center);
		return image;
	}
	
	public static Slider audioSlider(Vector2 size, Vector2 position, float audiolvl, int[] controls) {
		Slider audioSlider = new Slider(0, 100, 1, false, skin, "default-horizontal");
        audioSlider.setSize(colWidth*size.x,rowHeight*size.y);
        audioSlider.setPosition(colWidth*position.x,rowHeight*position.y);
        audioSlider.setValue(audiolvl);
        audioSlider.setSnapToValues(new float[] {0, 50, 100}, 3);
        audioSlider.addListener(new InputListener() {
        	@Override
        	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
        		GameSound.newAudiolvl((int) audioSlider.getValue(), controls);
        	}
        	@Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
        		GameSound.buttonSound();
                return true;
            }
        });
		return audioSlider;
	}
	
	
	public static TextButton playButton(Vector2 size, Vector2 position, ChessGame game) {
		TextButton button = button(size, position, "Play");
		button.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            	//If the player hasnt completed the first level yet, they will skip the levelSelectScreen and begin at lvl 1
            	if (SaveFile.readProgress()[0] == 0) {
            		try {
						game.setScreen(new Game(game, 0, false, false, null));
					} catch (IOException e) {
						e.printStackTrace();
					}
            	}	
            	else {
            		game.setScreen(new LevelSelectScreen(game));
            	}		
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            	GameSound.buttonSound();
                return true;
            }
        });
		return button;
	}
	
	/**
	 * Creates a TextButton with the given parameters that closes the application on activation.
	 * 
	 * @param size of the button
	 * @param position on the screen by 16 rows and 24 columns
	 * @return a new TextButton
	 * 
	 * @author Åsmund
	 */
	public static TextButton exitButton(Vector2 size, Vector2 position) {
		TextButton button = button(size, position, "Quit");
		button.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            	Gdx.app.exit();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            	GameSound.buttonSound();
                return true;
            }
        });
		return button;
	}
	
	public static TextButton changeButton(Vector2 size, Vector2 position, String buttonText, String promptText, int index, Stage stage, int[] controls) {
		TextButton button = button(size, position, buttonText);
		button.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int buttona) {
            	changeButtonInput(button, promptText, index, stage, controls);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            	GameSound.buttonSound();
                return true;
            }
        });
		return button;
	}
	
	static Label prompt = UI.label(new Vector2(24,2), new Vector2(0,10),"", "default-light");
	static Label warning = UI.label(new Vector2(12,2), new Vector2(6,8), "Please select a button that is not already assigned", "default-light");
	
	private static int[] changeButtonInput(TextButton button, String text, int index, Stage stage, int[] controls) {
		
		OptionScreen.startTimer(7f);
		
		//Text prompting you to type new input for key
        prompt.setText("Press the button for the new " + text + " key.");
        stage.addActor(prompt);

        
        Gdx.input.setInputProcessor(new InputAdapter () {
     	   @Override
     	   public boolean keyDown (int keycode) {
     		   if (keycode == Keys.ESCAPE) {
     			  stage.addActor(warning);
				  return false;
     		   }   
     		   for (int i : controls) {
     			   if (i == keycode) {
     				   stage.addActor(warning);
     				   return false;
     			   }
     		   }
     		   controls[index] = keycode;
     		   button.setText(Keys.toString(keycode));
     		   stage.addAction(Actions.removeActor(prompt));
     		   stage.addAction(Actions.removeActor(warning));
     		   Gdx.input.setInputProcessor(stage);
     		   return true;
     	   }
     	});
        
        return controls;
	}
	
	public static void timesUp(Stage stage) {
		stage.addAction(Actions.removeActor(prompt));
		stage.addAction(Actions.removeActor(warning));
		Gdx.input.setInputProcessor(stage);
	}
	
	
	public static TextButton defaultControlsButton(Vector2 size, Vector2 position, ChessGame game) {
		TextButton button = UI.button(size, position, "Default controls");
        button.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            	OptionScreen.defaultControls();
            	game.setScreen(new OptionScreen(game));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            	GameSound.buttonSound();
                return true;
            }
        });
		return button;
	}

	public static TextButton resumeButton(Vector2 size, Vector2 position) {
		TextButton button = button(size, position, "Resume");
		button.addListener(new InputListener() {
    		@Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
    			Game.pauseGame();
    		}
    		
    		@Override
    		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
    			GameSound.buttonSound();
    			return true;
    		}	
    	});
		return button;
	}

	/**
	 * Temporary button for easier putting the game in an untouched state
	 * 
	 * @param size
	 * @param position
	 * @return
	 */
	public static Button resetButton(Vector2 size, Vector2 position) {
		Button resetButton = UI.button(size, position, "RESET");
        resetButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                SaveFile.totalReset();
                Gdx.app.exit();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            	GameSound.buttonSound();
                return true;
            }
        });
		return resetButton;
	}
	
	public static SelectBox<String> selectBox(Vector2 size, Vector2 position, String[] items) {
		SelectBox<String> selectbox = new SelectBox<String>(skin, "default");
		selectbox.setSize(size.x*colWidth, size.y*rowHeight);
		selectbox.setPosition(position.x*colWidth, position.y*rowHeight);
		selectbox.setItems(items);
		return selectbox;
	}
	
	public static SelectBox<String> resolutionBox(Vector2 size, Vector2 position, String[] items, int selected, int[] controls, ChessGame game){
		SelectBox<String> selectBox = selectBox(size, position, items);
		selectBox.setSelectedIndex(selected);
		selectBox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				GameSound.buttonSound();
				controls[5] = selectBox.getSelectedIndex();
				Vector2 newRes = Constants.resolutions[controls[5]];
				boolean fullscreen = false;
				if (controls[6] == 1)
					fullscreen = true;
        		updateScreenSize((int)newRes.x,(int)newRes.y, fullscreen);
        		SaveFile.writeSettings(controls);
        		game.setScreen(new OptionScreen(game));
			}
		});
		return selectBox;
	}
	
	public static CheckBox checkBox(Vector2 size, Vector2 position, String text) {
		CheckBox checkBox = new CheckBox(text, skin, "default");
		checkBox.setSize(size.x*colWidth, size.y*rowHeight);
		checkBox.setPosition(position.x*colWidth, position.y*rowHeight);
		return checkBox;
	}
	
	public static CheckBox fullScreenBox(Vector2 size, Vector2 position, int[] controls, ChessGame game) {
		CheckBox checkBox = checkBox(size, position, "");
		if (controls[6] == 1)
			checkBox.setChecked(true);
		checkBox.addListener(new InputListener() {
			@Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if (checkBox.isChecked()) { 
                	controls[6] = 1;
                	updateScreenSize(0, 0, true);
                	SaveFile.writeSettings(controls);
            		game.setScreen(new OptionScreen(game));
                }
                else {
                	controls[6] = 0;
                	Vector2 newRes = Constants.resolutions[controls[5]];
                	updateScreenSize((int)newRes.x, (int)newRes.y, false);
                	SaveFile.writeSettings(controls);
            		game.setScreen(new OptionScreen(game));
                }
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            	GameSound.buttonSound();
                return true;
            }
        });
		return checkBox;
	}
	
	public static TextField textField(String text, Vector2 size, Vector2 position ) {
        TextField textField = new TextField(text, tempskin, "default");
        textField.setSize(size.x*colWidth, size.y*rowHeight);
        textField.setPosition(position.x*colWidth, position.y*rowHeight);
        return textField;
    }
}

