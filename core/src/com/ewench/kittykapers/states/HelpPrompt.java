package com.ewench.kittykapers.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.ewench.kittykapers.entities.Button;
import com.ewench.kittykapers.handlers.GameStateManager;
import com.ewench.kittykapers.main.Game;

public class HelpPrompt extends GameState {

	private Button yesButton;
	private Button noButton;
	
	private BitmapFont tempFont;
	private FreeTypeFontGenerator tempFontGenerator;
	private FreeTypeFontParameter tempFontParameter;

	private String helpPrompt = "Would you like to know how to play?";
	private String helpPrompt2 = "Help can always be found in Settings > Help.";

	public HelpPrompt(GameStateManager gsm) {
		super(gsm);

		// Creating a temporary font
		tempFontGenerator = new FreeTypeFontGenerator(
				Gdx.files.internal("fonts/body.ttf"));
		tempFontParameter = new FreeTypeFontParameter();
		tempFontParameter.size = 16;
		tempFont = tempFontGenerator.generateFont(tempFontParameter);
		tempFont.setColor(Color.BLACK);

		// Load in Button Texture
		TextureRegion buttonReg = new TextureRegion(
				Game.res.getTexture("buttons"), 64, 0, 270, 64);

		// Retry Button
		yesButton = new Button(buttonReg, (Game.GAME_WIDTH / 4), 50, cam);
		yesButton.setText("Yes");

		// Maps Button
		noButton = new Button(buttonReg, ((Game.GAME_WIDTH) / 4) * 3, 50, cam);
		noButton.setText("No");

	}

	public void handleInput() {

	}

	public void update(float dt) {

		yesButton.update(dt);

		if (yesButton.isClicked()) {

			if (Game.prefs.getBoolean("soundEnabled")) {
				Menu.menuSound.play();
			}

			Game.prefs.putBoolean("firstTimePlaying", false);
			Game.prefs.flush();

			gsm.setState(GameStateManager.HELP);
		}

		noButton.update(dt);

		if (noButton.isClicked()) {

			if (Game.prefs.getBoolean("soundEnabled")) {
				Menu.menuSound.play();
			}

			Game.prefs.putBoolean("firstTimePlaying", false);
			Game.prefs.flush();

			gsm.setState(GameStateManager.MAP_SELECT);
		}

	}

	public void render() {

		// Draw Background.
		sb.setProjectionMatrix(hudCam.combined);
		sb.begin();
		sb.draw(Game.background, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

		// Draw Title
		float width = Menu.titleFont.getBounds("First Time?").width;
		Menu.titleFont.draw(sb, "First Time?", ((Game.GAME_WIDTH - width) / 2),
				325);

		float helpWidth = tempFont.getBounds(helpPrompt).width;
		tempFont.draw(sb, helpPrompt, ((Game.GAME_WIDTH - helpWidth) / 2),
				200);

		float helpWidth2 = tempFont.getBounds(helpPrompt2).width;
		tempFont.draw(sb, helpPrompt2,
				((Game.GAME_WIDTH - helpWidth2) / 2), 170);

		sb.end();

		yesButton.render(sb, Menu.bodyFont);
		noButton.render(sb, Menu.bodyFont);

	}

	public void dispose() {

		tempFont.dispose();

	}

}
