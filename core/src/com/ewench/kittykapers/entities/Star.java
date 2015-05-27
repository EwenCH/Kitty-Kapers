package com.ewench.kittykapers.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Star {

	private float x;
	private float y;
	private final float WIDTH = 32;
	private final float HEIGHT = 32;

	private TextureRegion reg;

	// Creates a star oject with the passed in data.
	public Star(TextureRegion reg, float x, float y) {
		// Sets the object with the passed in data.
		this.reg = reg;
		this.x = x;
		this.y = y;
	}

	// What to do when render is called.
	public void render(SpriteBatch sb) {
		sb.begin();
		sb.draw(reg, x-WIDTH/2, y-HEIGHT/2);
		sb.end();
	}

}
