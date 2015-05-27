package com.ewench.kittykapers.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.ewench.kittykapers.handlers.Animation;
import com.ewench.kittykapers.handlers.Box2DVariables;

public class Box2DSprite {
	
	protected Body body;
	protected Animation animation;
	protected float width;
	protected float height;
	
	// Creates a new sprite and animation object and attaches it to a body.
	public Box2DSprite(Body body) {
		this.body = body;
		animation = new Animation();
	}
	
	// Sets the animation and height/width.
	public void setAnimation(TextureRegion[] region, float delay) {
		animation.setFrames(region, delay);
		width = region[0].getRegionWidth();
		height = region[0].getRegionHeight();
	}
	
	// Updates the Sprite.
	public void update(float dt){
		animation.update(dt);
	}
	
	// Draws the current sprite frame in the location of the body.
	public void render(SpriteBatch sb){
		sb.begin();
		sb.draw(
				animation.getFrame(),
				body.getPosition().x * Box2DVariables.PPM - width / 2,
				body.getPosition().y * Box2DVariables.PPM - height / 2
				);
		sb.end();
	}
	
	// Getters for the body + position.
	public Body getBody() { return body; }
	public Vector2 getPosition() { return body.getPosition(); }

}
