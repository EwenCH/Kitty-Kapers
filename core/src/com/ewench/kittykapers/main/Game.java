package com.ewench.kittykapers.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ewench.kittykapers.handlers.BoundedCamera;
import com.ewench.kittykapers.handlers.Content;
import com.ewench.kittykapers.handlers.GameStateManager;
import com.ewench.kittykapers.handlers.MyInput;
import com.ewench.kittykapers.handlers.MyInputProcessor;

public class Game extends ApplicationAdapter {

	/*
	 * Setting values that modify the game window size + details so they can be
	 * easily modified later.
	 */
	public final static String GAME_TITLE = "Kitty Kapers!";
	public final static int GAME_HEIGHT = 360;
	public final static int GAME_WIDTH = 640;
	public static final String GAME_VERSION = "1.1";
	public static final float STEP = 1 / 60f;

	// This will need to be updated if more maps are added.
	public static final int GAME_MAP_COUNT = 12;

	private SpriteBatch sb;
	private BoundedCamera cam; // Used to bound the camera.
	private OrthographicCamera hudCam; // Used to display information on screen.
	public static Texture background;

	// Saves things like high scores + user choices.
	public static Preferences prefs;

	public SpriteBatch getSpriteBatch() {
		return sb;
	}

	public BoundedCamera getCam() {
		return cam;
	}

	public OrthographicCamera getHudCam() {
		return hudCam;
	}

	private GameStateManager gsm;

	public static Content res;

	public void create() {

		// Loading in sprites to be used in the game
		res = new Content();
		res.loadTexture("images/blackcat.png", "blackCat");
		res.loadTexture("images/gingercat.png", "gingerCat");
		res.loadTexture("images/rainbowcat.png", "rainbowCat");
		res.loadTexture("images/prentice.png", "prentice");
		res.loadTexture("images/goldcoin.png", "coin");
		res.loadTexture("images/colourblocks.png", "blocks");
		res.loadTexture("images/button.png", "buttons");
		res.loadTexture("images/button_old.png", "oldButtons");
		res.loadTexture("images/stars2.png", "stars");
		
		// Background set up
		background = new Texture(Gdx.files.internal("images/background.png"));

		setPrefs();
		
		// NEED ONLY BE CALLED FOR TESTING
		//prefs.clear();

		// Setting the input processor as the modified one
		Gdx.input.setInputProcessor(new MyInputProcessor());

		// Initialising the cameras etc
		sb = new SpriteBatch();
		cam = new BoundedCamera();
		cam.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);

		// Starting the game by loading the GameStateManager
		gsm = new GameStateManager(this);

	}

	public void render() {

		// Shows the FPS in the desktop application
		Gdx.graphics.setTitle(Game.GAME_TITLE + " -- FPS: "
				+ Gdx.graphics.getFramesPerSecond());

		// Update the game
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render();
		MyInput.update();

	}

	public void dispose() {

		// Disposes of textures when the game closes.
		res.disposeTexture("blackCat");
		res.disposeTexture("gingerCat");
		res.disposeTexture("rainbowCat");
		res.disposeTexture("coin");
		res.disposeTexture("blocks");
		res.disposeTexture("buttons");
		res.disposeTexture("stars");

	}

	// Sets the preferences with defaults if they don't exist.
	public static void setPrefs() {
		prefs = Gdx.app.getPreferences("KittyKapers");

		// Creates a High Score preference object for each map
		for (int i = 1; i < GAME_MAP_COUNT + 1; i++) {
			if (!prefs.contains("highscore" + Integer.toString(i))) {
				prefs.putInteger("highscore" + Integer.toString(i), 0);
				prefs.flush();
			}
		}
		
		// Creates high score preference for infinite.
		if(!prefs.contains("highscoreInfinite")) {
			prefs.putInteger("highscoreInfinite", 0);
			prefs.flush();
		}

		// Creates a preference storing whether or not the player has completed
		// the level.
		for (int i = 1; i < GAME_MAP_COUNT + 1; i++) {
			if (!prefs.contains("playerCompletedLevel" + Integer.toString(i))) {
				prefs.putBoolean("playerCompletedLevel" + Integer.toString(i), false);
				prefs.flush();
			}
		}

		// Creates a preference storing whether or not the player has collected
		// all the coins in a map.
		for (int i = 1; i < GAME_MAP_COUNT + 1; i++) {
			if (!prefs.contains("allCoinsOnLevel" + Integer.toString(i))) {
				prefs.putBoolean("allCoinsOnLevel" + Integer.toString(i), false);
				prefs.flush();
			}
		}

		// Character preference - default is black cat
		if (!prefs.contains("playerChoice")) {
			prefs.putString("playerChoice", "blackCat");
			prefs.flush();
		}

		// Sound preference - default is true
		if (!prefs.contains("soundEnabled")) {
			prefs.putBoolean("soundEnabled", true);
			prefs.flush();
		}

		// Music preference - default is true
		if (!prefs.contains("musicEnabled")) {
			prefs.putBoolean("musicEnabled", true);
			prefs.flush();
		}

		// Used in the character selection logic
		if (!prefs.contains("currentArrayChoice")) {
			prefs.putInteger("currentArrayChoice", 0);
			prefs.flush();
		}
		
		// Used to determine if prentice player is unlocked.
		if (!prefs.contains("isPrenticeUnlocked")) {
			prefs.putBoolean("isPrenticeUnlocked", false);
			prefs.flush();
		}
		
		// Used to display help prompt if first time playing.
		if (!prefs.contains("firstTimePlaying")) {
			prefs.putBoolean("firstTimePlaying", true);
			prefs.flush();
		}
	}

}
