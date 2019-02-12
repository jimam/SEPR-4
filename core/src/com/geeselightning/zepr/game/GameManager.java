package com.geeselightning.zepr.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.geeselightning.zepr.KeyboardController;
import com.geeselightning.zepr.entities.Entity;
import com.geeselightning.zepr.entities.Player;
import com.geeselightning.zepr.entities.PowerUp;
import com.geeselightning.zepr.entities.Zombie;
import com.geeselightning.zepr.util.Constant;
import com.geeselightning.zepr.world.Level;

import box2dLight.RayHandler;

public class GameManager implements Disposable {
	
	public static GameManager instance;
	private final Zepr parent;
	
	private boolean gameRunning = false;
	private boolean levelLoaded = false;
	
	private int levelProgress = 0;
	
	/* GameScreen display objects */
	private OrthographicCamera gameCamera;
	private SpriteBatch batch;
	
	private KeyboardController controller;
	
	/* Level specific fields */
	private World world;
	private TiledMapRenderer tiledMapRenderer;
	private Box2DDebugRenderer debugRenderer;
	private RayHandler rayHandler;
	private Player player;
	private Player.Type playerType;
	private Level level;
	private Level.Location location;
	private int waveProgress = 0;
	
	private ArrayList<Entity> entities;
	private ArrayList<Zombie> zombies;
	private ArrayList<PowerUp> powerUps;
	
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
	
	public Level.Location getLocation() {
		return location;
	}
	
	public void setLocation(Level.Location location) {
		this.location = location;
	}
	
	public Level getLevel() {
		return level;
	}
	
	public int getWaveProgress() {
		return waveProgress;
	}
	
	public void addEntity(Entity entity) {
		this.entities.add(entity);
	}
	
	public void addEnemy(Zombie zombie) {
		this.zombies.add(zombie);
		this.entities.add(zombie);
	}
	
	/**
	 * Gets the mouse position in screen coordinates (origin top-left).
	 * @return	a {@link Vector2} representing the mouse's position on the screen
	 */
	public Vector2 getMouseScreenPos() {
		return new Vector2(Gdx.input.getX(), Gdx.input.getY());
	}
	
	/**
	 * Gets the mouse position in world coordinates (origin center).
	 * @return	a {@link Vector2} representing the mouse's position in the world
	 */
	public Vector2 getMouseWorldPos() {
		Vector3 mousePos3D = gameCamera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		Vector2 mousePos = new Vector2(mousePos3D.x, mousePos3D.y);
		return mousePos;
	}
	
	public void loadLevel() {
		world = new World(Vector2.Zero, true);
		world.setContactListener(null);
		
		debugRenderer = new Box2DDebugRenderer();
		
		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(0.7f);
		
		level = new Level(parent, location);
		tiledMapRenderer = new OrthogonalTiledMapRenderer(level.load(), 1 / (float)Constant.PPT);
		tiledMapRenderer.setView(gameCamera);
		
		this.entities = new ArrayList<>();
		this.zombies = new ArrayList<>();
		this.powerUps = new ArrayList<>();
		
		player = new Player(parent, 0.3f, level.getPlayerSpawn(), 0f, playerType);
		player.defineBody();
		
		addEntity(player);
		
		waveProgress = 0;
		
		// Spawn entities
		
		levelLoaded = true;
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
		tiledMapRenderer.render(level.getBackgroundLayers());
		batch.setProjectionMatrix(gameCamera.combined);
		batch.begin();
		entities.forEach(entity -> entity.draw(batch));
		batch.end();
		tiledMapRenderer.render(level.getForegroundLayers());
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
		debugRenderer.dispose();
		world.dispose();
	}

}
