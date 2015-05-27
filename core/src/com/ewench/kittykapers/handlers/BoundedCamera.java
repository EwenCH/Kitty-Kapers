package com.ewench.kittykapers.handlers;

import com.badlogic.gdx.graphics.OrthographicCamera;

// Modified camera object that dooes not move out of specific coordinates.
public class BoundedCamera extends OrthographicCamera {

	private float xmin;
	private float xmax;
	private float ymin;
	private float ymax;

	// Creates the object.
	public BoundedCamera() {
		this(0, 0, 0, 0);
	}

	// Creates the object with the given variables.
	public BoundedCamera(float xmin, float xmax, float ymin, float ymax) {
		super();
		setBounds(xmin, xmax, ymin, ymax);
	}

	// Sets the camera bounds - the maximum and minimum coordinates it can
	// move between.
	public void setBounds(float xmin, float xmax, float ymin, float ymax) {
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
	}

	// Sets the camera position with given variables.
	public void setPosition(float x, float y) {
		setPosition(x, y, 0);
	}

	// Uses camera code to set - needs a Z coordinate for some reason.
	public void setPosition(float x, float y, float z) {
		position.set(x, y, z);
		fixBounds();
	}

	// Stops the camera from travelling outside the bounds.
	private void fixBounds() {
		if (position.x < xmin + viewportWidth / 2) {
			position.x = xmin + viewportWidth / 2;
		}
		if (position.x > xmax - viewportWidth / 2) {
			position.x = xmax - viewportWidth / 2;
		}
		if (position.y < ymin + viewportHeight / 2) {
			position.y = ymin + viewportHeight / 2;
		}
		if (position.y > ymax - viewportHeight / 2) {
			position.y = ymax - viewportHeight / 2;
		}
	}

}