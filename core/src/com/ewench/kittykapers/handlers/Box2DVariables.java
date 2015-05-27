package com.ewench.kittykapers.handlers;

public class Box2DVariables {
	
	// Box2D needs to be scaled up to fit the camera size as it starts out too small.
	// This variables provides an easy way to scale everything up to the same size.
	public static final float PPM = 100;
	
	// Category bits - These are needed to define what can collide with what.
	public static final short BIT_PLAYER = 2;
	public static final short BIT_RED = 4;
	public static final short BIT_GREEN = 8;
	public static final short BIT_BLUE = 16;
	public static final short BIT_GROUND = 32;
	public static final short BIT_COIN = 64;

}
