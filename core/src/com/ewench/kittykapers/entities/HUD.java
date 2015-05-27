package com.ewench.kittykapers.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ewench.kittykapers.handlers.Box2DVariables;
import com.ewench.kittykapers.main.Game;

public class HUD {
	
	private Player player;
	private TextureRegion[] colourBlocks;
	
	public HUD(Player player){
		
		// Sets the player as the passed in player.
		this.player = player;
		
		// Loading the blocks texture.
		Texture texture = Game.res.getTexture("blocks");
		
		// For loop to load in the three different block regions.
		colourBlocks = new TextureRegion[3];
		for(int i = 0; i < colourBlocks.length; i++) {
			colourBlocks[i] = new TextureRegion(texture, i*32, 0, 32, 32);
		}
		
	}
	
	public void render(SpriteBatch sb) {
		
		// Retrieves the players current Mask Bits.
		short bits = player.getBody().getFixtureList().first().getFilterData().maskBits;
		
		// Displays the selected colour block on the HUD.
		sb.begin();
		if((bits & Box2DVariables.BIT_BLUE) != 0){
			sb.draw(colourBlocks[0], Game.GAME_WIDTH - 50, Game.GAME_HEIGHT - 50);
		}
		if((bits & Box2DVariables.BIT_GREEN) != 0){
			sb.draw(colourBlocks[1], Game.GAME_WIDTH - 50, Game.GAME_HEIGHT - 50);
		}
		if((bits & Box2DVariables.BIT_RED) != 0){
			sb.draw(colourBlocks[2], Game.GAME_WIDTH - 50, Game.GAME_HEIGHT - 50);
		}
		sb.end();
		
	}

}
