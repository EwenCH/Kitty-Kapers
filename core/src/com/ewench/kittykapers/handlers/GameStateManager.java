package com.ewench.kittykapers.handlers;

import java.util.Stack;

import com.ewench.kittykapers.main.Game;
import com.ewench.kittykapers.states.CharacterSelection;
import com.ewench.kittykapers.states.Credits;
import com.ewench.kittykapers.states.GameOver;
import com.ewench.kittykapers.states.GameState;
import com.ewench.kittykapers.states.Help;
import com.ewench.kittykapers.states.HelpPrompt;
import com.ewench.kittykapers.states.Infinite;
import com.ewench.kittykapers.states.LevelComplete;
import com.ewench.kittykapers.states.MapSelect;
import com.ewench.kittykapers.states.Menu;
import com.ewench.kittykapers.states.Play;
import com.ewench.kittykapers.states.Settings;

public class GameStateManager {
	
	// Used to handle all of the game states.
	// Handles 'screen' logic.

	private Game game;
	
	private Stack<GameState> gameStates; 
	
	//The number doesn't matter so long as it doesn't clash with other state numbers.
	public static final int PLAY = 12345;
	public static final int MENU = 696969;
	public static final int MAP_SELECT = 9001;
	public static final int SETTINGS = 80085;
	public static final int CREDITS = 1337;
	public static final int GAME_OVER = 257;
	public static final int LEVEL_COMPLETE = 666;
	public static final int CHARACTER_SELECTION = 7;
	public static final int HELP = 54321;
	public static final int HELP_PROMPT = 272727;
	public static final int INFINITE = 1001;
	
	// Constructor method, pushes the Menu state when it first loads.
	public GameStateManager(Game game){
		this.game = game;
		gameStates = new Stack<GameState>();
		pushState(MENU);
	}
	
	public Game game() { return game; }
	
	// Updates the current state.
	public void update(float dt) {
		gameStates.peek().update(dt);
	}
	
	// Renders the current state. 
	public void render() {
		gameStates.peek().render();
	}
	
	// Used to retrieve a state, and create it.
	private GameState getState(int state) {
		if(state == PLAY) return new Play(this);
		if(state == MENU) return new Menu(this);
		if(state == MAP_SELECT) return new MapSelect(this);
		if(state == SETTINGS) return new Settings(this);
		if(state == CREDITS) return new Credits(this);
		if(state == GAME_OVER) return new GameOver(this);
		if(state == LEVEL_COMPLETE) return new LevelComplete(this);
		if(state == CHARACTER_SELECTION) return new CharacterSelection(this);
		if(state == HELP) return new Help(this);
		if(state == HELP_PROMPT) return new HelpPrompt(this);
		if(state == INFINITE) return new Infinite(this);
		return null;
	}

	// Sets the given state as the current state.
	public void setState(int state) {
		popState();
		pushState(state);
	}
	
	// Pushes the given state.
	public void pushState(int state) {
		gameStates.push(getState(state));
	}
	
	// Disposes of the current state.
	public void popState() {
		GameState g = gameStates.pop();
		g.dispose();
	}
	
}
