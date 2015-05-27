package com.ewench.kittykapers.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.ewench.kittykapers.handlers.MyInput;

public class Button {

	private float x;
	private float y;
	private float width;
	private float height;

	private TextureRegion region;

	private Vector3 mouseLocation;
	private OrthographicCamera cam;

	private boolean clicked;

	private String text;

	// Creates a button with the texture region, x/y coordinates and camera.
	public Button(TextureRegion region, float x, float y,
			OrthographicCamera cam) {

		//Sets the new object with the passed variables.
		this.region = region;
		this.x = x;
		this.y = y;
		this.cam = cam;

		width = region.getRegionWidth();
		height = region.getRegionHeight();
		mouseLocation = new Vector3();
		
	}

	// Returns whether or not the button has been clicked.
	public boolean isClicked() {
		return clicked;
	}

	// Sets the text for the button.
	public void setText(String string) {
		text = string;
	}

	// Updates the button + checks if it is clicked.
	public void update(float dt) {

		mouseLocation.set(MyInput.x, MyInput.y, 0);
		cam.unproject(mouseLocation);

		// If the mouse is over the button and clicks, then it is clicked.
		if (MyInput.isPressed() && mouseLocation.x > x - width / 2
				&& mouseLocation.x < x + width / 2 && mouseLocation.y > y - height / 2
				&& mouseLocation.y < y + height / 2) {
			clicked = true;
		} else {
			clicked = false;
		}
		
	}

	// Renders the button with the text + using a font object.
	public void render(SpriteBatch sb, BitmapFont font) {

		sb.begin();

		// Draws the button texture.
		sb.draw(region, x - width / 2, y - height / 2);
		
		// If there is no passed in string, don't attempt to draw it.
		if (text != null) {
		drawString(sb, text, x, y, font);
		}
		
		sb.end();

	}

	// Draws the string passed in earlier.
	private void drawString(SpriteBatch sb, String s, float x, float y, BitmapFont font) {
		
		float stringWidth = font.getBounds(s).width / 2;
		font.draw(sb, s, x - stringWidth, y+5);
		
	}

}
