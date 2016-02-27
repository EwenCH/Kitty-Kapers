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

public class GameOver extends GameState {

	private Button retryButton;
	private Button mapsButton;
	private Button menuButton;

	private BitmapFont tempFont;
	private FreeTypeFontGenerator tempFontGenerator;
	private FreeTypeFontParameter tempFontParameter;

	// Storing strings so they can be easily changed.
	private String highScoreBeat = "At least you beat your high score...";
	private String noHighScore = "It's a good thing cats have nine lives...";
	private String infiniteHighScore = "You beat your high score!";
	private String message = "";
	private String tipMessage = "Remember: Tap the left half to Jump";
	private String tipMessage2 = "and Tap the right half to select your colour.";
	
	private int highScore;
	private int score;

	public GameOver(GameStateManager gsm) {
		super(gsm);

		// Creating a temporary font
		tempFontGenerator = new FreeTypeFontGenerator(
				Gdx.files.internal("fonts/body.ttf"));
		tempFontParameter = new FreeTypeFontParameter();
		tempFontParameter.size = 20;
		tempFont = tempFontGenerator.generateFont(tempFontParameter);
		tempFont.setColor(Color.BLACK);
		
		if (Play.level >= 1) {
			highScore = Game.prefs.getInteger("highscore" + Play.level);
		} else {
			highScore = Game.prefs.getInteger("highscoreInfinite", score);
		}
		
		if (Play.level >= 1) {
			score = Play.score;
		} else {
			score = Infinite.score;
		}
			
		// Load in Button Texture
		TextureRegion buttonReg = new TextureRegion(
				Game.res.getTexture("buttons"), 64, 0, 270, 64);

		// Retry Button
		retryButton = new Button(buttonReg, (Game.GAME_WIDTH / 4), 50, cam);
		retryButton.setText("Retry");

		// Maps Button
		mapsButton = new Button(buttonReg, ((Game.GAME_WIDTH) / 4) * 3, 50, cam);
		mapsButton.setText("Maps");
		
		// Menu Button
		menuButton = new Button(buttonReg, ((Game.GAME_WIDTH) / 4) * 3, 50, cam);
		menuButton.setText("Menu");

		// Sets the message string dependendent on hether the player beat their
		// high score.
		if (Play.score > Play.originalHighScore) {
			if (Play.level == 0) {
				message = highScoreBeat;
			} else {
				message = infiniteHighScore;
			}
		} else {
			message = noHighScore;
		}

		// Needed so the buttons work after the play state.
		cam.setToOrtho(false, Game.GAME_WIDTH, Game.GAME_HEIGHT);

	}

	public void update(float dt) {

		// Retry button logic.
		retryButton.update(dt);
		if (retryButton.isClicked()) {
			if (Game.prefs.getBoolean("soundEnabled")) {
				Menu.menuSound.play();
			}
			if (Play.level == 0) {
				gsm.setState(GameStateManager.INFINITE);
			} else {
				gsm.setState(GameStateManager.PLAY);
			}
		}

		// Maps button logic.
		if (Play.level >= 1) {
			mapsButton.update(dt);
			if (mapsButton.isClicked()) {
				if (Game.prefs.getBoolean("soundEnabled")) {
					Menu.backSound.play();
				}
				gsm.setState(GameStateManager.MAP_SELECT);
			}
		} else {
			menuButton.update(dt);
			if (menuButton.isClicked()) {
				if (Game.prefs.getBoolean("soundEnabled")) {
					Menu.backSound.play();
				}
				gsm.setState(GameStateManager.MENU);
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
		float titleWidth = Menu.titleFont.getBounds("Game Over!").width;
		Menu.titleFont.draw(sb, "Game Over!",
				((Game.GAME_WIDTH - titleWidth) / 2), 325);

		// Draw score
		float scoreWidth = tempFont.getBounds("Score: "
				+ Integer.toString(score)).width;
		tempFont.draw(sb, "Score: " + Integer.toString(score),
				((Game.GAME_WIDTH - scoreWidth) / 2), 250);

		// Draw highScore
		float highScoreWidth = tempFont.getBounds("High score: "
				+ Integer.toString(highScore)).width;
		tempFont.draw(sb, "High score: " + Integer.toString(highScore),
				((Game.GAME_WIDTH - highScoreWidth) / 2), 220);

		// Draw message
		float messageWidth = tempFont.getBounds(message).width;
		tempFont.draw(sb, message, ((Game.GAME_WIDTH - messageWidth) / 2), 180);

		// Draw control reminders
		float tipMessageWidth = Menu.bodyFont.getBounds(tipMessage).width;
		Menu.bodyFont.draw(sb, tipMessage,
				((Game.GAME_WIDTH - tipMessageWidth) / 2), 140);
		float tipMessage2Width = Menu.bodyFont.getBounds(tipMessage2).width;
		Menu.bodyFont.draw(sb, tipMessage2,
				((Game.GAME_WIDTH - tipMessage2Width) / 2), 120);

		sb.end();

		// Draws buttons.
		Menu.bodyFont.setColor(Color.WHITE);
		retryButton.render(sb, Menu.bodyFont);
		
		if (Play.level >= 1) {
			mapsButton.render(sb, Menu.bodyFont);
		} else {
			menuButton.render(sb, Menu.bodyFont);
		}

	}

	public void dispose() {

		// Disposing the temporary font
		tempFont.dispose();
		
		// Resetting font color.
		Menu.bodyFont.setColor(Color.WHITE);

	}

	public void handleInput() {

	}
}
