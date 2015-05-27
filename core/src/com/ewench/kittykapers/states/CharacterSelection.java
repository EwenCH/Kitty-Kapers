package com.ewench.kittykapers.states;

import static com.ewench.kittykapers.handlers.Box2DVariables.PPM;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.ewench.kittykapers.entities.Button;
import com.ewench.kittykapers.entities.Player;
import com.ewench.kittykapers.handlers.GameStateManager;
import com.ewench.kittykapers.main.Game;

public class CharacterSelection extends GameState {

	private World world;
	private Player player;

	private Button backButton;
	private Button leftButton;
	private Button rightButton;

	// ArrayList is dynamic - can create unlockable characters.
	// List for res names.
	private ArrayList<String> characters;
	// List for character names.
	private ArrayList<String> characterNames;

	private static int characterArrayLength;
	private static int choice;

	public CharacterSelection(GameStateManager gsm) {

		super(gsm);

		world = new World(new Vector2(0, -12.5f), true);

		characters = new ArrayList<String>();
		characterNames = new ArrayList<String>();
		
		// Adding strings to array.
		characters.add("blackCat");
		characterNames.add("Toby");
		characters.add("gingerCat");
		characterNames.add("Finn");

		// Unlock rainbow cat if level 12 is completed.
		if (Game.prefs.getBoolean("playerCompletedLevel12")) {
			characters.add("rainbowCat");
			characterNames.add("Rainbow Cat");
		}

		// If Prentice condition met - unlock Prenty.
		if (Game.prefs.getBoolean("isPrenticeUnlocked")) {
			characters.add("prentice");
			characterNames.add("Prentice");
		}

		// Load Back Button Texture
		TextureRegion buttonReg = new TextureRegion(
				Game.res.getTexture("buttons"), 64, 0, 270, 64);

		TextureRegion smallButtonReg = new TextureRegion(
				Game.res.getTexture("buttons"), 0, 0, 64, 64);

		// Back Button
		backButton = new Button(buttonReg, (Game.GAME_WIDTH / 2), 50, cam);
		backButton.setText("Back");

		// Character Selection Buttons
		leftButton = new Button(smallButtonReg, (Game.GAME_WIDTH) / 4,
				Game.GAME_HEIGHT / 2, cam);
		leftButton.setText("<");
		rightButton = new Button(smallButtonReg, (Game.GAME_WIDTH) / 4 * 3,
				Game.GAME_HEIGHT / 2, cam);
		rightButton.setText(">");

		characterArrayLength = characters.size();
		choice = Game.prefs.getInteger("currentArrayNumber");

		// Creates an animated preview of the selected cat.
		createPlayerPreview();

	}

	public void update(float dt) {

		world.step(Game.STEP, 1, 1);
		player.update(dt);

		// Back button logic.
		backButton.update(dt);
		if (backButton.isClicked()) {
			Menu.backSound.play();
			gsm.setState(GameStateManager.MENU);
		}

		// Left button logic.
		leftButton.update(dt);
		if (leftButton.isClicked()) {
			Menu.menuSound.play();

			if (choice == 0) {
				choice = (characterArrayLength - 1);
			} else {
				choice--;
			}

			Game.prefs.putInteger("currentArrayNumber", choice);
			Game.prefs.putString("playerChoice", characters.get(choice));
			Game.prefs.flush();
			createPlayerPreview();
		}

		// Right button logic.
		rightButton.update(dt);
		if (rightButton.isClicked()) {
			Menu.menuSound.play();

			if (choice == (characterArrayLength - 1)) {
				choice = 0;
			} else {
				choice++;
			}

			// Saves the user's choices + creates a preview.
			Game.prefs.putInteger("currentArrayNumber", choice);
			Game.prefs.putString("playerChoice", characters.get(choice));
			Game.prefs.flush();
			createPlayerPreview();
		}

	}

	public void render() {

		// Draw Background.
		sb.setProjectionMatrix(hudCam.combined);
		sb.begin();
		sb.draw(Game.background, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		sb.end();

		// draw player
		sb.setProjectionMatrix(hudCam.combined);
		player.render(sb);

		sb.begin();
		
		// Setting font black for this state.
		Menu.bodyFont.setColor(Color.BLACK);
		
		// Draw Title
		float width = Menu.titleFont.getBounds("Character").width;
		Menu.titleFont.draw(sb, "Character", ((Game.GAME_WIDTH - width) / 2),
				325);

		// Draw Title
		float nameWidth = Menu.bodyFont.getBounds(characterNames.get(choice)).width;
		Menu.bodyFont.draw(sb, characterNames.get(choice),
				((Game.GAME_WIDTH - nameWidth) / 2), 125);

		sb.end();

		// Draws the buttons + changing colours.
		leftButton.render(sb, Menu.bodyFont);
		rightButton.render(sb, Menu.bodyFont);
		Menu.bodyFont.setColor(Color.WHITE);
		backButton.render(sb, Menu.bodyFont);

	}

	public void dispose() {

		// Returning to default colour.
		Menu.bodyFont.setColor(Color.WHITE);
		
	}

	public void handleInput() {
	}

	// Creates a sprite of the selected character
	private void createPlayerPreview() {

		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();

		// Create player
		bdef.position
				.set(Game.GAME_WIDTH / PPM / 2, Game.GAME_HEIGHT / PPM / 2);
		bdef.type = BodyType.StaticBody;
		Body body = world.createBody(bdef);

		shape.setAsBox(10 / PPM, 13 / PPM);
		fdef.shape = shape;
		body.createFixture(fdef).setUserData("player");
		shape.dispose();

		player = new Player(body);
		body.setUserData(player);
	}

}
