package com.ewench.kittykapers.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ewench.kittykapers.entities.Button;
import com.ewench.kittykapers.handlers.GameStateManager;
import com.ewench.kittykapers.handlers.MyInput;
import com.ewench.kittykapers.main.Game;

public class Help extends GameState {

	private Button backButton;
	
	private Texture helpscreen;

	public Help(GameStateManager gsm) {
		super(gsm);

		// Load Button Texture
		TextureRegion buttonReg = new TextureRegion(
				Game.res.getTexture("buttons"), 64, 0, 270, 64);

		// Back Button - MAKE THIS A TAP ANYWHERE SITU 
		backButton = new Button(buttonReg, (Game.GAME_WIDTH / 2), 50, cam);
		
		helpscreen = new Texture(Gdx.files.internal("images/helpScreen.png"));

	}

	public void handleInput() {
		
		if (MyInput.isPressed()) {
			if (Game.prefs.getBoolean("soundEnabled")) {
				Menu.backSound.play();
			}
			gsm.setState(GameStateManager.SETTINGS);
		}

	}

	public void update(float dt) {
		
		handleInput();

		// Back button logic.
		backButton.update(dt);
		if (backButton.isClicked()) {
			if (Game.prefs.getBoolean("soundEnabled")) {
				Menu.backSound.play();
			}
			gsm.setState(GameStateManager.SETTINGS);
		}

	}

	public void render() {

		// Draw Background.
		sb.setProjectionMatrix(hudCam.combined);
		sb.begin();
		sb.draw(Game.background, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		sb.draw(helpscreen, 0, 10, Game.GAME_WIDTH, Game.GAME_HEIGHT);

		// Draw Title
		float width = Menu.titleFont.getBounds("Help").width;
		Menu.titleFont.draw(sb, "Help", ((Game.GAME_WIDTH - width) / 2), 325);

		sb.end();

	}

	public void dispose() {

	}

}
