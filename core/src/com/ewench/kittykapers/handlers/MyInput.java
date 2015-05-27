package com.ewench.kittykapers.handlers;

// Handles input into the game.
public class MyInput {
	
	public static int x;
	public static int y;
	public static boolean down;
	public static boolean pdown;
	
	public static boolean[] keys;
	public static boolean[] pkeys;
	
	public static final int NUM_KEYS = 2;
	public static final int BUTTON1 = 0;
	public static final int BUTTON2 = 1;
	
	// Store keys when pressed and not pressed.
	static {
		keys = new boolean[NUM_KEYS];
		pkeys = new boolean[NUM_KEYS];
	}
	
	public static void update(){
		pdown = down;
		
		for(int i = 0; i < NUM_KEYS; i++){
			pkeys[i] = keys[i];
		}
	}
	
	// These are used elsewhere in my game in if statements etc.
	public static boolean isDown() { return down; }
	public static boolean isPressed() { return down && !pdown; }
	public static boolean isReleased() { return !down && pdown; }
	
	public static void setKey(int i, boolean b){ keys[i] = b; }
	public static boolean isDown(int i){ return keys[i]; }
	public static boolean isPressed(int i){ return keys[i] && !pkeys[i]; }

}
