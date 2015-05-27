package com.ewench.kittykapers.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ewench.kittykapers.entities.Button;
import com.ewench.kittykapers.handlers.GameStateManager;
import com.ewench.kittykapers.main.Game;

public class Credits extends GameState {

	private Button backButton;
	private Button prenticeButton;

	// User must tap tile 3 times to unlock.
	private int prenticeNumber;
	private Sound secret;

	// Storing strings so they can be easily changed.
	private String createdBy = "Created by : EwenCH";
	private String menuMusicBy = "Menu Music - 'CopyCat' by Syncopika";
	private String gameMusicBy = "Game Music - 'Awake' by cynicmusic.com / pixelsphere.org";
	private String coinSpriteBy = "Coin Artwork - Morgan3D";
	private String explosionSoundBy = "Explosion / Jump Sound FX - Dklon";
	private String backgroundBy = "Background Artwork - Zeyu Ren";

	public Credits(GameStateManager gsm) {
		super(gsm);

		// Load Button Texture
		TextureRegion buttonReg = new TextureRegion(
				Game.res.getTexture("buttons"), 64, 0, 270, 64);

		// Back Button
		backButton = new Button(buttonReg, (Game.GAME_WIDTH / 2), 50, cam);
		backButton.setText("Back");

		// Secret Prentice Button + Number
		prenticeButton = new Button(buttonReg, (Game.GAME_WIDTH / 2), 300, cam);
		prenticeNumber = 0;
		secret = Gdx.audio.newSound(Gdx.files.internal("sfx/coin3.wav"));

	}

	public void update(float dt) {

		// Back button logic.
		backButton.update(dt);
		if (backButton.isClicked()) {
			if (Game.prefs.getBoolean("soundEnabled")) {
				Menu.backSound.play();
			}
			gsm.setState(GameStateManager.SETTINGS);
		}

		// Prentice Button logic
		prenticeButton.update(dt);
		if (prenticeButton.isClicked()) {

			prenticeNumber++;

			if (prenticeNumber == 3) {
				if (Game.prefs.getBoolean("soundEnabled")) {
					secret.play();
				}
				Game.prefs.putBoolean("isPrenticeUnlocked", true);
				Game.prefs.flush();
				prenticeNumber = 0;
			}

		}

	}

	public void render() {

		// Draw Background.
		sb.setProjectionMatrix(hudCam.combined);
		sb.begin();
		sb.draw(Game.background, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

		// Setting body font as black.
		Menu.bodyFont.setColor(Color.BLACK);

		// Draw Title
		float titleWidth = Menu.titleFont.getBounds("Credits").width;
		Menu.titleFont.draw(sb, "Credits",
				((Game.GAME_WIDTH - titleWidth) / 2), 325);

		// Draw Credits
		float createdByWidth = Menu.bodyFont.getBounds(createdBy).width;
		Menu.bodyFont.draw(sb, createdBy,
				((Game.GAME_WIDTH - createdByWidth) / 2), 260);

		float assetTextWidth = Menu.bodyFont.getBounds("ASSETS").width;
		Menu.bodyFont.draw(sb, "ASSETS",
				((Game.GAME_WIDTH - assetTextWidth) / 2), 230);

		float menuMusicByWidth = Menu.bodyFont.getBounds(menuMusicBy).width;
		Menu.bodyFont.draw(sb, menuMusicBy,
				((Game.GAME_WIDTH - menuMusicByWidth) / 2), 210);

		float gameMusicByWidth = Menu.bodyFont.getBounds(gameMusicBy).width;
		Menu.bodyFont.draw(sb, gameMusicBy,
				((Game.GAME_WIDTH - gameMusicByWidth) / 2), 190);

		float coinSpriteByWidth = Menu.bodyFont.getBounds(coinSpriteBy).width;
		Menu.bodyFont.draw(sb, coinSpriteBy,
				((Game.GAME_WIDTH - coinSpriteByWidth) / 2), 170);

		float explosionSoundByWidth = Menu.bodyFont.getBounds(explosionSoundBy).width;
		Menu.bodyFont.draw(sb, explosionSoundBy,
				((Game.GAME_WIDTH - explosionSoundByWidth) / 2), 150);

		float backgroundByWidth = Menu.bodyFont.getBounds(backgroundBy).width;
		Menu.bodyFont.draw(sb, backgroundBy,
				((Game.GAME_WIDTH - backgroundByWidth) / 2), 130);

		sb.end();

		// Draws the back button.
		Menu.bodyFont.setColor(Color.WHITE);
		backButton.render(sb, Menu.bodyFont);

	}

	public void dispose() {

		// Resetting font colour.
		Menu.bodyFont.setColor(Color.WHITE);

	}

	public void handleInput() {
	}

}
