package com.ewench.kittykapers.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.ewench.kittykapers.main.Game;

public class Coin extends Box2DSprite {

	public Coin(Body body){
		
		super(body);
		
		// Ties the coin object to a spritesheet and animates it.
		Texture texture = Game.res.getTexture("coin");
		TextureRegion[] sprites = TextureRegion.split(texture, 32, 32)[0];
		setAnimation(sprites, 1/12f);
		
	}
	
}
