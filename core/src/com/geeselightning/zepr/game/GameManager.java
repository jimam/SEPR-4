package com.geeselightning.zepr.game;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.geeselightning.zepr.KeyboardController;
import com.geeselightning.zepr.entities.Player;
import com.geeselightning.zepr.util.Constant;
import com.geeselightning.zepr.world.Level;

import box2dLight.RayHandler;

public class GameManager implements Disposable {
	
	public static GameManager instance;
	private final Zepr parent;
	
	private boolean gameRunning = false;
	private boolean levelLoaded = false;
	
	/* GameScreen display objects */
	private OrthographicCamera gameCamera;
	private SpriteBatch batch;
	
	private KeyboardController controller;
	
	/* Level specific fields */
	private int levelProgress = 0;
	private World world;
	private TiledMapRenderer tiledMapRenderer;
	private Box2DDebugRenderer debugRenderer;
	private RayHandler rayHandler;
	private Player player;
	private Player.Type playerType;
	private Level level;
	
	private GameManager(Zepr parent) {
		this.parent = parent;
		
		controller = new KeyboardController();
	}

	public static GameManager getInstance(Zepr parent) {
		if (instance == null) {
			instance = new GameManager(parent);
		}
		return instance;
	}
	
	/* Accessor methods */
	public boolean isGameRunning() {
		return gameRunning;
	}
	
	public void setGameRunning(boolean running) {
		this.gameRunning = running;
	}
	
	public boolean isLevelLoaded() {
		return levelLoaded;
	}
	
	public void setLevelLoaded(boolean levelLoaded) {
		this.levelLoaded = levelLoaded;
	}
	
	public OrthographicCamera getGameCamera() {
		return gameCamera;
	}
	
	public void setGameCamera(OrthographicCamera gameCamera) {
		this.gameCamera = gameCamera;
	}
	
	public SpriteBatch getSpriteBatch() {
		return batch;
	}
	
	public void setSpriteBatch(SpriteBatch batch) {
		this.batch = batch;
	}
	
	public KeyboardController getController() {
		return controller;
	}
	
	public int getLevelProgress() {
		return levelProgress;
	}
	
	public void setLevelProgress(int levelProgress) {
		this.levelProgress = levelProgress;
	}
	
	public World getWorld() {
		return world;
	}
	
	public Player getPlayer() {
		return player;
	}
	
//	public void setPlayer(Player player) {
//		this.player = player;
//	}
	
	public Player.Type getPlayerType() {
		return playerType;
	}
	
	public void setPlayerType(Player.Type playerType) {
		this.playerType = playerType;
	}
	
	public Level getLevel() {
		return level;
	}
	
	public void loadLevel() {
		world = new World(Vector2.Zero, true);
		world.setContactListener(null);
		
		debugRenderer = new Box2DDebugRenderer();
		
		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(0.9f);
		
		level = new Level(parent, "townmap");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(level.load(), Constant.PPT);
		tiledMapRenderer.setView(gameCamera);
		
		// Create player
		
		// Spawn entities
		
		// Set levelLoaded to true
	}
	
	public void update(float delta) {
		if (!gameRunning) return;
		if (!levelLoaded) return;
		if (gameCamera == null || batch == null) return;
		if (player.getHealth() <= 0) {
			//TODO: respawn player
		}
		handleInput();
		
		// Re-centre the camera on the player after movement
		gameCamera.position.x = player.getX();
		gameCamera.position.y = player.getY();
		gameCamera.update();
		
		// Change the position of the map
		tiledMapRenderer.setView(gameCamera);
		
		draw();
		
		// Step through the physics world simulation
		world.step(1/60f, 6, 2);
	}

	/**
	 * Retrieves user input from the {@link InputProcessor} and moves the player character
	 * accordingly.
	 */
	public void handleInput() {
		
		float speed = player.getSpeed();
		
		if (controller.left) {
			player.setLinearVelocityX(-1 * speed);
		}
		if (controller.right) {
			player.setLinearVelocityX(speed);
		}
		if (controller.up) {
			player.setLinearVelocityY(speed);
		}
		if (controller.down) {
			player.setLinearVelocityY(-1 * speed);
		}
		if (controller.isMouse1Down) {
			player.setAttacking(true);
		} else {
			player.setAttacking(false);
		}
		
		// If both buttons for an axis are held down, or neither are, set velocity in that axis to 0
		if ((!controller.up && !controller.down) || (controller.up && controller.down)) {
			player.setLinearVelocityY(0);
		}
		if ((!controller.left && !controller.right) || (controller.left && controller.right)) {
			player.setLinearVelocityX(0);
		}
		
	}
	
	public void draw() {
		//TODO: allow rendering foreground/background objects seperately
		tiledMapRenderer.render();
		batch.setProjectionMatrix(gameCamera.combined);
		batch.begin();
		//TODO: render entities
		batch.end();
		rayHandler.setCombinedMatrix(gameCamera);
		rayHandler.updateAndRender();
		// If dev mode is enabled, show the debug renderer for Box2D
		if (Zepr.devMode) debugRenderer.render(world, gameCamera.combined);
	}
	
	public void waveComplete() {
		
	}
	
	public void levelComplete() {
		
	}
	
	@Override
	public void dispose() {
		rayHandler.dispose();
		world.dispose();
	}

}
