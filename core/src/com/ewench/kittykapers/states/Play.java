package com.ewench.kittykapers.states;

import static com.ewench.kittykapers.handlers.Box2DVariables.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.ewench.kittykapers.entities.Coin;
import com.ewench.kittykapers.entities.HUD;
import com.ewench.kittykapers.entities.Player;
import com.ewench.kittykapers.handlers.Box2DVariables;
import com.ewench.kittykapers.handlers.GameStateManager;
import com.ewench.kittykapers.handlers.MyContactListener;
import com.ewench.kittykapers.handlers.MyInput;
import com.ewench.kittykapers.main.Game;

public class Play extends GameState {

	private World world;
	private MyContactListener contactListener;

	private TiledMap tileMap;
	public static int mapWidth;
	private int mapHeight;
	private int tileSize;
	private OrthogonalTiledMapRenderer tmr;

	private Player player;

	public static Music music;
	public static boolean isGameMusicPlaying = false;

	private Sound jump;
	private Sound colourChange;
	private Sound dead;
	private Sound coin1;
	private Sound coin2;
	private Sound coin3;
	private static int coinSound;

	private int totalLevelCoins;
	private Array<Coin> coins;

	public static int score;
	public static int highScore;
	public static int originalHighScore;

	private HUD hud;

	public static int level;

	public Play(GameStateManager gsm) {

		super(gsm);

		// Box2D Setup
		world = new World(new Vector2(0, -12.5f), true);
		contactListener = new MyContactListener();
		world.setContactListener(contactListener);

		// Sets the score and retrieves the high score for the map.
		score = 0;
		highScore = Game.prefs.getInteger("highscore" + level);
		originalHighScore = Game.prefs.getInteger("highscore" + level);

		// Plays music if it is not already playing.
		if (!isGameMusicPlaying) {
			if (Game.prefs.getBoolean("musicEnabled")) {
				music = Gdx.audio.newMusic(Gdx.files
						.internal("music/awake.ogg"));
				music.setLooping(true);
				music.play();
				isGameMusicPlaying = true;
			}
		}

		// Loading Sounds
		jump = Gdx.audio.newSound(Gdx.files.internal("sfx/jump.wav"));
		colourChange = Gdx.audio.newSound(Gdx.files.internal("sfx/beep.wav"));
		dead = Gdx.audio.newSound(Gdx.files.internal("sfx/dead.wav"));
		coin1 = Gdx.audio.newSound(Gdx.files.internal("sfx/coin1.wav"));
		coin2 = Gdx.audio.newSound(Gdx.files.internal("sfx/coin2.wav"));
		coin3 = Gdx.audio.newSound(Gdx.files.internal("sfx/coin3.wav"));
		coinSound = 0;

		// Creating essential world objects.
		createPlayer();
		createTiles();
		createCoins();

		// Sets the bounds for the bounded camera.
		cam.setBounds(0, mapWidth * tileSize, 0, mapHeight * tileSize
				+ Game.GAME_HEIGHT);

		// Creates a new HUD with the selected block on.
		hud = new HUD(player);

		Menu.bodyFont.setColor(Color.BLACK);

	}

	public void handleInput() {

		// Desktop controls.
		if (MyInput.isPressed(MyInput.BUTTON1)) {
			playerJump();
		}
		if (MyInput.isPressed(MyInput.BUTTON2)) {
			if (Game.prefs.getBoolean("soundEnabled")) {
				colourChange.play();
			}
			switchBlocks();
		}

		// Touch Controls.
		if (MyInput.isPressed()) {
			if (MyInput.x < Gdx.graphics.getWidth() / 2) {
				playerJump();
			} else {
				if (Game.prefs.getBoolean("soundEnabled")) {
					colourChange.play();
				}
				switchBlocks();
			}
		}

	}

	// Method to make the player jump.
	public void playerJump() {
		if (contactListener.isPlayerOnGround()) {
			if (Game.prefs.getBoolean("soundEnabled")) {
				jump.play();
			}
			player.getBody().applyForceToCenter(0, 300, true);

		}
	}

	public void update(float dt) {

		// Updates the world and checks for input.
		handleInput();
		world.step(Game.STEP, 1, 1);

		// Updating + Removing coins
		for (int i = 0; i < coins.size; i++) {
			coins.get(i).update(dt);
		}

		// Removes any bodies that couldn't be removed when the
		// world was updating.
		Array<Body> bodies = contactListener.getBodiesToRemove();
		for (int i = 0; i < bodies.size; i++) {
			Body body = bodies.get(i);
			coins.removeValue((Coin) body.getUserData(), true);

			// Multiple Coin sound logic
			coinSound += 1;
			if (Game.prefs.getBoolean("soundEnabled")) {
				if (coinSound == 1) {
					coin1.play();
				}
				if (coinSound == 2) {
					coin2.play();
				}
				if (coinSound == 3) {
					coin3.play();
					coinSound = 0;
				}
			}

			// Removes the body of any coin that has been collided with.
			world.destroyBody(body);
		}
		bodies.clear();

		// Updates the player and checks if they have died/won.
		player.update(dt);
		if (player.isDead()) {
			if (Game.prefs.getBoolean("soundEnabled")) {
				dead.play();
			}
			if (Game.prefs.getBoolean("musicEnabled")) {
				music.dispose();
				isGameMusicPlaying = false;
			}
			gsm.setState(GameStateManager.GAME_OVER);
		}

		if (player.hasWon()) {

			// Sets the level as completed in prefs.
			Game.prefs.putBoolean("playerCompletedLevel" + level, true);

			// Sets the allCoinsCollected prefs as true if all the coins
			// have been collected.
			if (coins.size == 0) {
				Game.prefs.putBoolean("allCoinsOnLevel" + level, true);
			}

			Game.prefs.flush();

			gsm.setState(GameStateManager.LEVEL_COMPLETE);
		}

		// Updates the score.
		score = totalLevelCoins - (coins.size * 100);

		// If the current score is more than highscore, this sets the
		// current high score as the new high score.
		if (score > highScore) {
			highScore = score;
		}

	}

	public void render() {

		// Draw Background.
		sb.setProjectionMatrix(hudCam.combined);
		sb.begin();
		sb.draw(Game.background, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		sb.end();

		// Follow player + not go out of view
		cam.setPosition(player.getPosition().x * PPM + Game.GAME_WIDTH / 4,
				player.getPosition().y * PPM + Game.GAME_HEIGHT / mapHeight
						- 50);
		cam.update();

		// Draw Tiled map
		tmr.setView(cam);
		tmr.render();

		// Draw player.
		sb.setProjectionMatrix(cam.combined);
		player.render(sb);

		// Draws the coins.
		for (int i = 0; i < coins.size; i++) {
			coins.get(i).render(sb);
		}

		// Draws the HUD.
		sb.setProjectionMatrix(hudCam.combined);
		hud.render(sb);

		// draw score
		sb.begin();

		// Draws the current score and high score.
		Menu.bodyFont.draw(sb, "Score: " + Integer.toString(score), 15,
				Game.GAME_HEIGHT - 15);

		Menu.bodyFont.draw(sb, "High Score: " + Integer.toString(highScore),
				15, Game.GAME_HEIGHT - 35);
		sb.end();

	}

	public void dispose() {

		// DISPOSING CERTAIN OBJECTS
		// THIS IS IMPORTANT BECAUSE JAVA IS NOT GOOD AT THIS

		if (score > highScore || score == highScore) {
			Game.prefs.putInteger("highscore" + level, score);
			Game.prefs.flush();
		}

		coin1.dispose();
		coin2.dispose();
		coin3.dispose();
		jump.dispose();
		colourChange.dispose();

		tileMap.dispose();
		tmr.dispose();

		Menu.bodyFont.setColor(Color.WHITE);

		// world.dispose(); - This kills the program

	}

	// Creates the player.
	private void createPlayer() {

		// New definition objects.
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();

		// Create the player body
		bdef.position.set(20 / PPM, 100 / PPM);
		bdef.type = BodyType.DynamicBody; // Dynamic bodies are affected by
											// gravity.
		bdef.linearVelocity.set(1.2f, 0); // Always moves right.
		Body body = world.createBody(bdef);

		shape.setAsBox(10 / PPM, 13 / PPM); // Creates a shape.
		fdef.shape = shape;
		fdef.filter.categoryBits = Box2DVariables.BIT_PLAYER; // Category Bits.

		fdef.filter.maskBits = Box2DVariables.BIT_RED | Box2DVariables.BIT_COIN
				| Box2DVariables.BIT_GROUND; // The bits that this can collide
												// with.

		body.createFixture(fdef).setUserData("player");

		// Create player jump sensor
		shape.setAsBox(13 / PPM, 2 / PPM, new Vector2(0, -15 / PPM), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = Box2DVariables.BIT_PLAYER;

		fdef.filter.maskBits = Box2DVariables.BIT_RED
				| Box2DVariables.BIT_GROUND;

		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("foot");
		shape.dispose();

		MassData md = body.getMassData();
		md.mass = 1;
		body.setMassData(md);

		// create player
		player = new Player(body);
		body.setUserData(player);

	}

	// Creates the map with the tmx file.
	private void createTiles() {

		// Loads in the tmx file based on the static level variable.
		tileMap = new TmxMapLoader().load("maps/level" + level + ".tmx");
		tmr = new OrthogonalTiledMapRenderer(tileMap);
		tileSize = tileMap.getProperties().get("tilewidth", Integer.class);

		// Sets the height/width of the loaded tile map.
		mapHeight = tileMap.getProperties().get("height", Integer.class);
		mapWidth = tileMap.getProperties().get("width", Integer.class);

		TiledMapTileLayer layer;

		// Creates the different layers with different bits.
		layer = (TiledMapTileLayer) tileMap.getLayers().get("ground");
		createLayer(layer, Box2DVariables.BIT_GROUND);

		layer = (TiledMapTileLayer) tileMap.getLayers().get("red");
		createLayer(layer, Box2DVariables.BIT_RED);

		layer = (TiledMapTileLayer) tileMap.getLayers().get("green");
		createLayer(layer, Box2DVariables.BIT_GREEN);

		layer = (TiledMapTileLayer) tileMap.getLayers().get("blue");
		createLayer(layer, Box2DVariables.BIT_BLUE);

	}

	// Creates the layer.
	private void createLayer(TiledMapTileLayer layer, short bits) {

		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();

		// This returns, so in the case of a layer having nothing to
		// create, the game will not crash.
		if (layer == null) {
			return;
		}

		// Runs through every tile, and creates an oject when needed.
		for (int row = 0; row < layer.getHeight(); row++) {
			for (int column = 0; column < layer.getWidth(); column++) {

				Cell cell = layer.getCell(column, row);

				// Skips the current cell if it's null.
				if (cell == null)
					continue;
				if (cell.getTile() == null)
					continue;

				bdef.type = BodyType.StaticBody; // Static bodies not affected
													// by forces.
				bdef.position.set((column + 0.5f) * tileSize / PPM,
						(row + 0.5f) * tileSize / PPM);

				// Creates the shape.
				ChainShape chainShape = new ChainShape();
				Vector2[] v = new Vector2[3];
				v[0] = new Vector2(-tileSize / 2 / PPM, -tileSize / 2 / PPM);
				v[1] = new Vector2(-tileSize / 2 / PPM, tileSize / 2 / PPM);
				v[2] = new Vector2(tileSize / 2 / PPM, tileSize / 2 / PPM);
				chainShape.createChain(v);

				fdef.friction = 0;
				fdef.shape = chainShape;
				fdef.filter.categoryBits = bits;
				fdef.filter.maskBits = Box2DVariables.BIT_PLAYER;
				fdef.isSensor = false;
				world.createBody(bdef).createFixture(fdef);
				chainShape.dispose();

			}
		}

	}

	// Creates the coins.
	private void createCoins() {

		coins = new Array<Coin>();

		// Loads the coins from the 'coins' layer in the tmx file.
		MapLayer layer = tileMap.getLayers().get("coins");

		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();

		// Runs through each coin object.
		for (MapObject mapObject : layer.getObjects()) {

			bdef.type = BodyType.StaticBody;

			float x = Float.parseFloat((mapObject.getProperties().get("x")
					.toString())) / PPM;
			float y = Float.parseFloat((mapObject.getProperties().get("y")
					.toString())) / PPM;

			bdef.position.set(x, y);

			CircleShape circle = new CircleShape();
			circle.setRadius(16 / PPM);

			fdef.shape = circle;
			fdef.isSensor = true;
			fdef.filter.categoryBits = Box2DVariables.BIT_COIN;
			fdef.filter.maskBits = Box2DVariables.BIT_PLAYER;

			Body body = world.createBody(bdef);
			body.createFixture(fdef).setUserData("coin");

			Coin coin = new Coin(body);
			coins.add(coin);

			body.setUserData(coin);
			circle.dispose();

		}

		totalLevelCoins = coins.size * 100;

	}

	// Switches the selected colour.
	private void switchBlocks() {

		Filter filter = player.getBody().getFixtureList().first()
				.getFilterData();

		short bits = filter.maskBits;

		// Switch to next colour
		// RED > GREEN > BLUE
		if ((bits & Box2DVariables.BIT_RED) != 0) {
			bits &= ~Box2DVariables.BIT_RED;
			bits |= Box2DVariables.BIT_GREEN;
		} else if ((bits & Box2DVariables.BIT_GREEN) != 0) {
			bits &= ~Box2DVariables.BIT_GREEN;
			bits |= Box2DVariables.BIT_BLUE;
		} else if ((bits & Box2DVariables.BIT_BLUE) != 0) {
			bits &= ~Box2DVariables.BIT_BLUE;
			bits |= Box2DVariables.BIT_RED;
		}

		// Set new mask bits
		filter.maskBits = bits;
		player.getBody().getFixtureList().first().setFilterData(filter);

		filter = player.getBody().getFixtureList().get(1).getFilterData();
		bits &= ~Box2DVariables.BIT_COIN;
		filter.maskBits = bits;
		player.getBody().getFixtureList().get(1).setFilterData(filter);

	}

}
