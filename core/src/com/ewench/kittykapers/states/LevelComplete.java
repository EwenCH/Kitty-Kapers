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

public class LevelComplete extends GameState {

	private Button nextLevelButton;
	private Button mapsButton;

	private BitmapFont tempFont;
	private FreeTypeFontGenerator tempFontGenerator;
	private FreeTypeFontParameter tempFontParameter;

	// Storing strings so they can be easily changed later.
	private String highScoreBeat = "Well done, you beat your high score!";
	private String noHighScore = "Well done!";
	private String message = "";

	public LevelComplete(GameStateManager gsm) {
		super(gsm);

		// Creating a temporary font
		tempFontGenerator = new FreeTypeFontGenerator(
				Gdx.files.internal("fonts/body.ttf"));
		tempFontParameter = new FreeTypeFontParameter();
		tempFontParameter.size = 20;
		tempFont = tempFontGenerator.generateFont(tempFontParameter);
		tempFont.setColor(Color.BLACK);

		// Load in Button Texture
		TextureRegion buttonReg = new TextureRegion(
				Game.res.getTexture("buttons"), 64, 0, 270, 64);

		// Retry Button
		nextLevelButton = new Button(buttonReg, (Game.GAME_WIDTH / 4), 50, cam);
		nextLevelButton.setText("Next Level");

		// Maps Button
		mapsButton = new Button(buttonReg, ((Game.GAME_WIDTH) / 4) * 3, 50, cam);
		mapsButton.setText("Maps");

		// Sets the message dpendent on whether the user beat their high score.
		if (Play.score > Play.originalHighScore) {
			message = highScoreBeat;
		} else {
			message = noHighScore;
		}

		// Needed so the buttons work after the play state.
		cam.setToOrtho(false, Game.GAME_WIDTH, Game.GAME_HEIGHT);

	}

	public void update(float dt) {

		// Next level button logic.
		if (Play.level != 12) {
			nextLevelButton.update(dt);
			if (nextLevelButton.isClicked()) {
				Play.level++;
				if (Game.prefs.getBoolean("soundEnabled")) {
					Menu.menuSound.play();
				}
				gsm.setState(GameStateManager.PLAY);
			}
		}

		// Maps button logic.
		mapsButton.update(dt);
		if (mapsButton.isClicked()) {
			if (Game.prefs.getBoolean("soundEnabled")) {
				Menu.backSound.play();
			}
			if (Game.prefs.getBoolean("musicEnabled")) {
				Play.music.dispose();
				Play.isGameMusicPlaying = false;
			}
			gsm.setState(GameStateManager.MAP_SELECT);
		}

	}

	public void render() {

		// Draw Background.
		sb.setProjectionMatrix(hudCam.combined);
		sb.begin();
		sb.draw(Game.background, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

		// Draw Title
		float titleWidth = Menu.titleFont.getBounds("Level  " + Play.level
				+ "  Complete!").width;
		Menu.titleFont.draw(sb, "Level  " + Play.level + "  Complete!",
				((Game.GAME_WIDTH - titleWidth) / 2), 325);

		// Draw score
		float scoreWidth = tempFont.getBounds("Score: "
				+ Integer.toString(Play.score)).width;
		tempFont.draw(sb, "Score: " + Integer.toString(Play.score),
				((Game.GAME_WIDTH - scoreWidth) / 2), 230);

		// Draw highScore
		float highScoreWidth = tempFont.getBounds("High score: "
				+ Integer.toString(Play.highScore)).width;
		tempFont.draw(sb, "High score: " + Integer.toString(Play.highScore),
				((Game.GAME_WIDTH - highScoreWidth) / 2), 200);

		// Draw message
		float messageWidth = tempFont.getBounds(message).width;
		tempFont.draw(sb, message, ((Game.GAME_WIDTH - messageWidth) / 2), 150);

		sb.end();
		
		// Draws the buttons.
		if (Play.level != 12) {
			nextLevelButton.render(sb, Menu.bodyFont);
		}

		mapsButton.render(sb, Menu.bodyFont);

	}

	public void dispose() {

		// Disposing the temporary font
		tempFont.dispose();

	}

	public void handleInput() {

	}
}
