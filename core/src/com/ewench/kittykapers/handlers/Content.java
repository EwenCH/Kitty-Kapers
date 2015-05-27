package com.ewench.kittykapers.handlers;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Content {

	// A HashMap provides an easy way to reference to objects.
	private HashMap<String, Texture> textures;
	
	// Creates the content handler.
	public Content() {
		textures = new HashMap<String, Texture>();
	}
	
	// Methods to load/get/dispose of textures.
	public void loadTexture(String path, String key) {
		Texture tex = new Texture(Gdx.files.internal(path));
		textures.put(key, tex);
	}
	
	// Gets the texture using the hashmap key.
	public Texture getTexture(String key) {
		return textures.get(key);
	}
	
	// Disposes the texture assigned too the hashmap key.
	public void disposeTexture(String key) {
		Texture tex = textures.get(key);
		if(tex != null) tex.dispose();
	}
}
