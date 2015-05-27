package com.ewench.kittykapers.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.ewench.kittykapers.handlers.Box2DVariables;
import com.ewench.kittykapers.main.Game;
import com.ewench.kittykapers.states.Play;

public class Player extends Box2DSprite {
	
	// This string is changed from the character selection screen, and
	// is then used to load the corresponding texture region.
	public static String playerChoice;

	public Player(Body body) {

		super(body);
		
		// Sets the texture to the texture selected in the character
		// selection screen.
		playerChoice = Game.prefs.getString("playerChoice");

		// Attaches a sprite to the player object and animates it.
		Texture texture = Game.res.getTexture(playerChoice);
		TextureRegion[] sprites = TextureRegion.split(texture, 32, 32)[0];
		setAnimation(sprites, 1 / 16f);

	}

	// Returns whether the player is dead or not.
	public boolean isDead() {
		
		// Checks to see if the player has fallen out of the world.
		if (getBody().getPosition().y < 0) {
			return true;
		}
		
		// Checks to see if the player has collieded with something.
		if (getBody().getLinearVelocity().x < 0.001f) {
			return true;
		}
		
		// Otherwise the player is alive.
		return false;
	}

	// Returns whether the player has won or not.
	public boolean hasWon() {
		
		// Checks if the player has reached the end of the current loaded map.
		if (getBody().getPosition().x * Box2DVariables.PPM > (Play.mapWidth) * 32) {
			return true;
		}
		
		// Otherwise the player has not won yet.
		return false;
	}

}
