package com.ewench.kittykapers.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ewench.kittykapers.main.Game;

public class DesktopLauncher {
	
	public static void main (String[] arg) {
		
		boolean fullscreen = false;
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title = Game.GAME_TITLE;
		
		if(fullscreen){
			config.width = 1920;
			config.height = 1080;
			config.fullscreen = true;
		} else {
			config.width = Game.GAME_WIDTH*2;
			config.height = Game.GAME_HEIGHT*2;
		}
		
		new LwjglApplication(new Game(), config);
		
	}
}
