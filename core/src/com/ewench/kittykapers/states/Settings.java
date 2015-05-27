package com.ewench.kittykapers.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ewench.kittykapers.entities.Button;
import com.ewench.kittykapers.handlers.GameStateManager;
import com.ewench.kittykapers.main.Game;

public class Settings extends GameState {

	private Button musicButton;
	private Button soundButton;
	private Button helpButton;
	private Button creditsButton;

	private Button backButton;

	public Settings(GameStateManager gsm) {
		super(gsm);

		// Load in Button Texture
		TextureRegion buttonReg = new TextureRegion(
				Game.res.getTexture("buttons"), 64, 0, 270, 64);

		// Back Button
		backButton = new Button(buttonReg, (Game.GAME_WIDTH / 2), 50, cam);
		backButton.setText("Back");

		// Music Button
		musicButton = new Button(buttonReg, (Game.GAME_WIDTH / 4), 210, cam);
		if (Game.prefs.getBoolean("musicEnabled")) {
			musicButton.setText("Music: ON"); // ADD LOGIC FOR PREFS
		} else {
			musicButton.setText("Music: OFF");
		}

		// Sound Button
		soundButton = new Button(buttonReg, (Game.GAME_WIDTH / 4) * 3, 210, cam);
		if (Game.prefs.getBoolean("soundEnabled")) {
			soundButton.setText("Sound: ON");
		} else {
			soundButton.setText("Sound: OFF");
		}

		// Help Button
		helpButton = new Button(buttonReg, (Game.GAME_WIDTH / 4), 130, cam);
		helpButton.setText("Help");

		// Credits Button
		creditsButton = new Button(buttonReg, (Game.GAME_WIDTH / 4) * 3, 130,
				cam);
		creditsButton.setText("Credits");

	}

	public void update(float dt) {

		// Back button logic.
		backButton.update(dt);
		if (backButton.isClicked()) {
			if (Game.prefs.getBoolean("soundEnabled")) {
				Menu.backSound.play();
			}
			gsm.setState(GameStateManager.MENU);
		}

		// Music button logic.
		musicButton.update(dt);
		if (musicButton.isClicked()) {

			if (Game.prefs.getBoolean("soundEnabled")) {
				Menu.menuSound.play();
			}

			if (Game.prefs.getBoolean("musicEnabled")) {
				if (Menu.music.isPlaying()) {
					musicButton.setText("Music: OFF");
					Game.prefs.putBoolean("musicEnabled", false);
					Menu.music.dispose();
					Menu.isMenuMusicPlaying = false;
				} else {
					Menu.music = Gdx.audio.newMusic(Gdx.files
							.internal("music/copycat.ogg"));
					Menu.music.setLooping(true);
					Menu.music.play();
					Menu.isMenuMusicPlaying = true;
				}
			} else if (!Game.prefs.getBoolean("musicEnabled")) {
				musicButton.setText("Music: ON");
				Game.prefs.putBoolean("musicEnabled", true);
				Menu.music = Gdx.audio.newMusic(Gdx.files
						.internal("music/copycat.ogg"));
				Menu.music.setLooping(true);
				Menu.music.play();
				Menu.isMenuMusicPlaying = true;
			}
			Game.prefs.flush();
		}

		// Sound button logic.
		soundButton.update(dt);
		if (soundButton.isClicked()) {

			if (Game.prefs.getBoolean("soundEnabled")) {
				Menu.menuSound.play();
			}

			if (Game.prefs.getBoolean("soundEnabled")) {
				soundButton.setText("Sound: OFF");
				Game.prefs.putBoolean("soundEnabled", false);
			} else if (!Game.prefs.getBoolean("soundEnabled")) {
				soundButton.setText("Sound: ON");
				Game.prefs.putBoolean("soundEnabled", true);
			}
			Game.prefs.flush();
		}

		// Help button logic.
		helpButton.update(dt);
		if (helpButton.isClicked()) {
			if (Game.prefs.getBoolean("soundEnabled")) {
				Menu.menuSound.play();
			}
			gsm.setState(GameStateManager.HELP);
		}

		// Credits button logic.
		creditsButton.update(dt);
		if (creditsButton.isClicked()) {
			if (Game.prefs.getBoolean("soundEnabled")) {
				Menu.menuSound.play();
			}
			gsm.setState(GameStateManager.CREDITS);
		}

	}

	public void render() {

		// Draw Background.
		sb.setProjectionMatrix(hudCam.combined);
		sb.begin();
		sb.draw(Game.background, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

		// Draw Title
		float width = Menu.titleFont.getBounds("Settings").width;
		Menu.titleFont.draw(sb, "Settings", ((Game.GAME_WIDTH - width) / 2),
				325);

		sb.end();

		// Draws the buttons.
		backButton.render(sb, Menu.bodyFont);
		musicButton.render(sb, Menu.bodyFont);
		soundButton.render(sb, Menu.bodyFont);
		helpButton.render(sb, Menu.bodyFont);
		creditsButton.render(sb, Menu.bodyFont);

	}

	public void dispose() {
	}

	public void handleInput() {
	}

}
