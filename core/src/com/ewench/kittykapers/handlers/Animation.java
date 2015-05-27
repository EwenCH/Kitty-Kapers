package com.ewench.kittykapers.handlers;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animation {

	// This stores each frame.
	private TextureRegion[] frames;
	
	// These variables store the time and delay.
	private float time;
	private float delay;
	
	// This stores the current frame, which is then used by sprite classes to
	// render the current frame.
	private int currentFrame;
	
	// Creates the object.
	public Animation() {}
	
	// Ths creates the object with the passed in information.
	public Animation(TextureRegion[] frames, float delay){}
	
	// This sets the object variabes with the passed in data.
	public void setFrames(TextureRegion[] frames, float delay){
		this.frames = frames;
		this.delay = delay;
		time = 0;
		currentFrame = 0;
	}
	
	// This updates the object frame.
	public void update(float dt){
		
		// If there is no delay then return.
		if(delay <= 0) return;
		
		// Otherwise the time updates.
		time += dt;
		
		// While the time has a higher value than the delay, it steps.
		while(time >= delay) {
			step();
		}
	}
	
	// This sets the current frame as the next frame only when it should.
	private void step(){
		
		// This levels out the time and delay, essentially reducing lag, or a 
		// faster/slower animation because of processing speed.
		time -= delay;
		
		// Increments the current frame, or returns to 0 if it is on the
		// last frame in the texture region.
		currentFrame++;
		if(currentFrame == frames.length){
			currentFrame = 0;
		}
	}
	
	// This is used to get the curret frame for other classes.
	public TextureRegion getFrame() { return frames[currentFrame]; }
	
}
