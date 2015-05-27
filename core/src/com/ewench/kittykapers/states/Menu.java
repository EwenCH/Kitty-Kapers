package com.ewench.kittykapers.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.ewench.kittykapers.entities.Button;
import com.ewench.kittykapers.handlers.GameStateManager;
import com.ewench.kittykapers.main.Game;

public class Menu extends GameState {

	public static Music music;
	public static boolean isMenuMusicPlaying = false;

	public static Sound backSound;
	public static Sound menuSound;

	// Title Font
	public static BitmapFont titleFont;
	public static FreeTypeFontGenerator titleFontGenerator;
	public static FreeTypeFontParameter titleFontParameter;

	// Body Font - for buttons etc
	public static BitmapFont bodyFont;
	public static FreeTypeFontGenerator bodyFontGenerator;
	public static FreeTypeFontParameter bodyFontParameter;
	private boolean fontsCreated = false;

	private Button mapButton;
	private Button infiniteButton;
	private Button charSelecButton;
	private Button settingsButton;

	public Menu(GameStateManager gsm) {
		super(gsm);

		// Makes sure that the preferences have been set.
		Game.setPrefs();

		// Checks if music is playing/if it should be and acts accordingly.
		if (Game.prefs.getBoolean("musicEnabled")) {
			if (isMenuMusicPlaying == false) {
				music = Gdx.audio.newMusic(Gdx.files
						.internal("music/copycat.ogg"));
				music.setLooping(true);
				music.play();
				isMenuMusicPlaying = true;
			}
		}

		// Creates Menu sounds for buttons.
		backSound = Gdx.audio.newSound(Gdx.files.internal("sfx/menu2.wav"));
		menuSound = Gdx.audio.newSound(Gdx.files.internal("sfx/menu1.wav"));

		// Generates fonts to be used throughout the game if they haven't
		// already been created.
		if (!fontsCreated) {
			titleFontGenerator = new FreeTypeFontGenerator(
					Gdx.files.internal("fonts/title.ttf"));
			titleFontParameter = new FreeTypeFontParameter();
			titleFontParameter.size = 64;
			titleFont = titleFontGenerator.generateFont(titleFontParameter);
			titleFont.setColor(Color.BLACK);

			bodyFontGenerator = new FreeTypeFontGenerator(
					Gdx.files.internal("fonts/body.ttf"));
			bodyFontParameter = new FreeTypeFontParameter();
			bodyFontParameter.size = 13;
			bodyFont = bodyFontGenerator.generateFont(bodyFontParameter);

			fontsCreated = true;
		}

		// Texture for buttons
		TextureRegion buttonReg = new TextureRegion(
				Game.res.getTexture("buttons"), 64, 0, 270, 64);

		// Maps Button
		mapButton = new Button(buttonReg, (Game.GAME_WIDTH / 4), 180, cam);
		mapButton.setText("Maps");

		// Infinite Button
		infiniteButton = new Button(buttonReg, (Game.GAME_WIDTH / 4) * 3,
				180, cam);
		infiniteButton.setText("Infinite");

		// Character Selection Button
		charSelecButton = new Button(buttonReg, (Game.GAME_WIDTH / 4), 100, cam);
		charSelecButton.setText("Character Selection");

		// Settings Button
		settingsButton = new Button(buttonReg, (Game.GAME_WIDTH / 4) * 3, 100,
				cam);
		settingsButton.setText("Settings");
		
		// Needed so the buttons work after the play state.
		cam.setToOrtho(false, Game.GAME_WIDTH, Game.GAME_HEIGHT);

	}

	public void handleInput() {
	}

	public void update(float dt) {

		// Map button logic.
		mapButton.update(dt);
		if (mapButton.isClicked()) {
			if (Game.prefs.getBoolean("soundEnabled")) {
				menuSound.play();
			}

			if (Game.prefs.getBoolean("firstTimePlaying")) {
				gsm.setState(GameStateManager.HELP_PROMPT);
			} else {
				gsm.setState(GameStateManager.MAP_SELECT);
			}
		}

		// Infinite button logic.
		infiniteButton.update(dt);
		if (infiniteButton.isClicked()) {
			gsm.setState(GameStateManager.INFINITE);
		}

		// Character selection button logic.
		charSelecButton.update(dt);
		if (charSelecButton.isClicked()) {
			menuSound.play();
			gsm.setState(GameStateManager.CHARACTER_SELECTION);
		}

		// Settings button logic.
		settingsButton.update(dt);
		if (settingsButton.isClicked()) {
			if (Game.prefs.getBoolean("soundEnabled")) {
				menuSound.play();
			}
			gsm.setState(GameStateManager.SETTINGS);
		}

	}

	public void render() {

		// Draw Background.
		sb.setProjectionMatrix(hudCam.combined);
		sb.begin();
		sb.draw(Game.background, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		sb.end();

		sb.begin();

		// Draw Title
		float width = titleFont.getBounds(Game.GAME_TITLE).width;
		titleFont.draw(sb, Game.GAME_TITLE, ((Game.GAME_WIDTH - width) / 2),
				325);

		// Draw Version Info
		float versionWidth = bodyFont.getBounds(Game.GAME_VERSION).width + 7;
		bodyFont.draw(sb, Game.GAME_VERSION, (Game.GAME_WIDTH - versionWidth),
				20);

		sb.end();

		// Drws the buttons.
		mapButton.render(sb, bodyFont);
		infiniteButton.render(sb, bodyFont);
		charSelecButton.render(sb, bodyFont);
		settingsButton.render(sb, bodyFont);

	}

	public void dispose() {

		// Disposes the font generators.
		titleFontGenerator.dispose();
		bodyFontGenerator.dispose();

	}

}
