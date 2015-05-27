package com.ewench.kittykapers.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ewench.kittykapers.handlers.BoundedCamera;
import com.ewench.kittykapers.handlers.GameStateManager;
import com.ewench.kittykapers.main.Game;

/* I don't want to make any instances of this, only to extend from it.
 * Which is why this is an abstract class. */

public abstract class GameState {

	protected GameStateManager gsm;
	protected Game game;

	protected SpriteBatch sb;
	protected BoundedCamera cam;
	protected OrthographicCamera hudCam;

	protected GameState(GameStateManager gsm) {
		this.gsm = gsm;
		game = gsm.game();
		sb = game.getSpriteBatch();
		cam = game.getCam();
		hudCam = game.getHudCam();

	}

	public abstract void handleInput();

	public abstract void update(float dt);

	public abstract void render();

	public abstract void dispose();

}
