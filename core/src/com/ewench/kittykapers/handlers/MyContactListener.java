package com.ewench.kittykapers.handlers;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

public class MyContactListener implements ContactListener {

	// Used to determine if the player can jump or not.
	private int numFootContacts;

	// This stores any bodies to be removed as they shouldn't be removed
	// *while* the world is updating.
	private Array<Body> bodiesToRemove;

	public MyContactListener() {
		super();
		bodiesToRemove = new Array<Body>();
	}

	// Called when two fixtures collide
	public void beginContact(Contact contact) {

		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();

		// Check to see if the foot sensor is on the ground.
		if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
			numFootContacts++;
		}
		if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
			numFootContacts++;
		}

		// Check to see if a coin has been collided with.
		if (fa.getUserData() != null && fa.getUserData().equals("coin")) {
			bodiesToRemove.add(fa.getBody());
		}
		if (fb.getUserData() != null && fb.getUserData().equals("coin")) {
			bodiesToRemove.add(fb.getBody());
		}

	}

	// Called when two fixtures stop colliding
	public void endContact(Contact contact) {

		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();

		if (fa == null || fb == null) {
			return;
		}

		// Check to see if foot sensor is no longer on ground.
		if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
			numFootContacts--;
		}
		if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
			numFootContacts--;
		}

	}

	public boolean isPlayerOnGround() {
		//The player is on the ground if this is equal to one.
		return numFootContacts > 0;
	}

	// Returns an array of any bodies that need to be removed once the world
	// has updated.
	public Array<Body> getBodiesToRemove() {
		return bodiesToRemove;
	}

	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

}
