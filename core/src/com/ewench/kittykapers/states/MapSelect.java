package com.ewench.kittykapers.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ewench.kittykapers.entities.Button;
import com.ewench.kittykapers.entities.Star;
import com.ewench.kittykapers.handlers.GameStateManager;
import com.ewench.kittykapers.main.Game;

public class MapSelect extends GameState {

	public Music music;
	private Sound explosion;

	private Button[][] buttons;
	private int rowLength;
	private int colLength;
	
	private Button backButton;

	private Star[][] stars;

	public MapSelect(GameStateManager gsm) {
		super(gsm);

		// Creates the explosion sound effect.
		explosion = Gdx.audio.newSound(Gdx.files.internal("sfx/explosion.wav"));

		// Checks if the music is already playing or not and if it should
		// be. Then acts accordingly.
		if (Game.prefs.getBoolean("musicEnabled")) {
			if (Menu.isMenuMusicPlaying == false) {
				Menu.music = Gdx.audio.newMusic(Gdx.files
						.internal("music/copycat.ogg"));
				Menu.music.setLooping(true);
				Menu.music.play();
				Menu.isMenuMusicPlaying = true;
			}
		}

		// Load small button textures.
		TextureRegion buttonReg = new TextureRegion(
				Game.res.getTexture("buttons"), 0, 0, 64, 64);

		// Load the gold star.
		TextureRegion goldStar = new TextureRegion(
				Game.res.getTexture("stars"), 0, 0, 32, 32);

		// Load the dull star.
		TextureRegion dullStar = new TextureRegion(
				Game.res.getTexture("stars"), 32, 0, 32, 32);

		// Setting the buttons array.
		buttons = new Button[2][6];

		// Setting the stars array.
		stars = new Star[2][6];

		// Storing the array length and checking against it is faster than
		// checking it in a for loop.
		rowLength = buttons.length;
		colLength = buttons[0].length;

		// Runs through the buttons array and creates buttons in the right
		// locations
		// with the right text.
		for (int row = 0; row < rowLength; row++) {
			for (int col = 0; col < colLength; col++) {

				buttons[row][col] = new Button(buttonReg, 112 + col * 84,
						215 - row * 80, cam);
				buttons[row][col].setText(row * colLength + col + 1 + "");

				// If this level has been completed with all coins collected
				// it create a star object with the gold texture, however
				// if it has ONLY been completed it uses a dull texture.
				if (Game.prefs.getBoolean("allCoinsOnLevel"
						+ (row * colLength + col + 1))) {

					stars[row][col] = new Star(goldStar, 143 + col * 84,
							246 - row * 80);

				} else if (Game.prefs.getBoolean("playerCompletedLevel"
						+ (row * colLength + col + 1))) {

					stars[row][col] = new Star(dullStar, 143 + col * 84,
							246 - row * 80);

				}
			}
		}

		// Loads the back button texture + sets the text.
		TextureRegion backButtonReg = new TextureRegion(
				Game.res.getTexture("buttons"), 64, 0, 270, 64);
		backButton = new Button(backButtonReg, (Game.GAME_WIDTH / 2), 50, cam);
		backButton.setText("Back");

	}

	public void handleInput() {

	}

	public void update(float dt) {

		// Updates every button object.
		for (int row = 0; row < rowLength; row++) {
			for (int col = 0; col < colLength; col++) {

				buttons[row][col].update(dt);
				if (buttons[row][col].isClicked()) {
					
					// Sets a static variable in play, that is then used to
					// load the right map.
					Play.level = row * buttons[0].length + col + 1;

					// Plays an explosion sound and disposes the menu
					// music if they are enabled.
					if (Game.prefs.getBoolean("musicEnabled")) {
						Menu.music.dispose();
						Menu.isMenuMusicPlaying = false;
					}
					if (Game.prefs.getBoolean("soundEnabled")) {
						explosion.play();
					}
					gsm.setState(GameStateManager.PLAY);

				}
			}
		}

		// Back button logic.
		backButton.update(dt);
		if (backButton.isClicked()) {
			if (Game.prefs.getBoolean("soundEnabled")) {
				Menu.backSound.play();
			}
			gsm.setState(GameStateManager.MENU);
		}

	}

	public void render() {

		// Draw Background.
		sb.setProjectionMatrix(hudCam.combined);
		sb.begin();
		sb.draw(Game.background, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

		// Draw title.
		float width = Menu.titleFont.getBounds("Maps").width;
		Menu.titleFont.draw(sb, "Maps", ((Game.GAME_WIDTH - width) / 2), 325);
		sb.end();

		// Draw the buttons.
		for (int row = 0; row < buttons.length; row++) {
			for (int col = 0; col < buttons[0].length; col++) {
				buttons[row][col].render(sb, Menu.bodyFont);

				// Renders the star if it exists.
				if (stars[row][col] != null) {
					stars[row][col].render(sb);
				}
			}
		}

		// Draw the back button.
		backButton.render(sb, Menu.bodyFont);

	}

	public void dispose() {
	}

}
