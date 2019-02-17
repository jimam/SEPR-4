package com.geeselightning.zepr.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
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
import com.geeselightning.zepr.entities.BossZombie;
import com.geeselightning.zepr.entities.Entity;
import com.geeselightning.zepr.entities.Player;
import com.geeselightning.zepr.entities.PowerUp;
import com.geeselightning.zepr.entities.Zombie;
import com.geeselightning.zepr.stages.Hud;
import com.geeselightning.zepr.util.Constant;
import com.geeselightning.zepr.util.RandomEnum;
import com.geeselightning.zepr.world.Level;
import com.geeselightning.zepr.world.Wave;
import com.geeselightning.zepr.world.WorldContactListener;

import box2dLight.RayHandler;

//import box2dLights.RayHandler;

/**
 * Coordinator for the main game logic and rendering. <br/>
 * Changes:
 * <ul>
 * <li>implemented class for game coordination</li>
 * </ul>
 * 
 * @author Xzytl
 */
public class GameManager implements Disposable {

	private final Zepr parent;
	public static GameManager instance;

	private Preferences prefs;

	private boolean gameRunning = false;
	private boolean levelLoaded = false;

	private int levelProgress = 0;

	/* GameScreen display objects */
	private OrthographicCamera gameCamera;
	private SpriteBatch batch;

	private KeyboardController controller;

	private Map<Level.Location, Wave[]> waves = new HashMap<>();

	/* Level specific fields */
	private World world;
	private TiledMapRenderer tiledMapRenderer;
	private Box2DDebugRenderer debugRenderer;

	private RayHandler rayHandler;
	private Hud hud;
  
	private Player player;
	private Player.Type playerType;
	private Level level;
	private Level.Location location;
	private int waveProgress = 0;
	private int zombiesToSpawn = 0;
	private float spawnCooldown = 0;
	private boolean activeBoss;

	private ArrayList<Entity> entities;
	private ArrayList<Zombie> zombies;
	private ArrayList<PowerUp> powerUps;

	private static RandomEnum<PowerUp.Type> randomPowerUpType = new RandomEnum<PowerUp.Type>(PowerUp.Type.class);
	private static RandomEnum<Zombie.Type> randomZombieType = new RandomEnum<Zombie.Type>(Zombie.Type.class);

	private GameManager(Zepr parent) {
		this.parent = parent;

		controller = new KeyboardController();
		
		prefs = Gdx.app.getPreferences("ZEPR Preferences");

		waves.put(Level.Location.TOWN, new Wave[] { Wave.SMALL, Wave.MEDIUM });
		waves.put(Level.Location.HALIFAX, new Wave[] { Wave.SMALL, Wave.MEDIUM });
		waves.put(Level.Location.CENTRALHALL, new Wave[] { Wave.MEDIUM, Wave.LARGE, Wave.MINIBOSS });
		waves.put(Level.Location.COURTYARD, new Wave[] { Wave.MEDIUM, Wave.LARGE, Wave.MEDIUM });
		waves.put(Level.Location.LIBRARY, new Wave[] { Wave.SMALL, Wave.MEDIUM, Wave.MEDIUM });
		waves.put(Level.Location.RONCOOKE, new Wave[] { Wave.LARGE, Wave.LARGE, Wave.BOSS });
	}

	public static GameManager getInstance(Zepr parent) {
		if (instance == null) {
			instance = new GameManager(parent);
		}
		return instance;
	}

	/* Accessor methods */
	public Preferences getPrefs() {
		return prefs;
	}
	
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

	public Wave getWave(Level.Location location, int wave) {
		if (waves.containsKey(location)) {
			return waves.get(location)[wave];
		} else {
			return Wave.SMALL;
		}
	}

	public World getWorld() {
		return world;
	}

	public Player getPlayer() {
		return player;
	}

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

	public void removeEntity(Entity entity) {
		this.entities.remove(entity);
	}

	public void addZombie(Zombie zombie) {
		this.zombies.add(zombie);
		this.entities.add(zombie);
	}

	public void removeZombie(Zombie zombie) {
		this.zombies.remove(zombie);
		this.entities.remove(zombie);
	}

	public void addPowerUp(PowerUp powerUp) {
		this.powerUps.add(powerUp);
		this.entities.add(powerUp);
	}

	public void removePowerUp(PowerUp powerUp) {
		this.powerUps.remove(powerUp);
		this.entities.remove(powerUp);
	}

	/**
	 * Gets the mouse position in screen coordinates (origin top-left).
	 * 
	 * @return a {@link Vector2} representing the mouse's position on the screen
	 */
	public Vector2 getMouseScreenPos() {
		return new Vector2(Gdx.input.getX(), Gdx.input.getY());
	}

	/**
	 * Gets the mouse position in world coordinates (origin center).
	 * 
	 * @return a {@link Vector2} representing the mouse's position in the world
	 */
	public Vector2 getMouseWorldPos() {
		Vector3 mousePos3D = gameCamera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		Vector2 mousePos = new Vector2(mousePos3D.x, mousePos3D.y);
		return mousePos;
	}

	public void loadLevel() {
		System.out.println("Beginning level loading...");
		world = new World(Vector2.Zero, true);
		world.setContactListener(new WorldContactListener());

		debugRenderer = new Box2DDebugRenderer();

		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(0.7f);

		level = new Level(parent, location);
		tiledMapRenderer = new OrthogonalTiledMapRenderer(level.load(), 1 / (float) Constant.PPT);
		tiledMapRenderer.setView(gameCamera);

		hud = new Hud(parent);

		this.entities = new ArrayList<>();
		this.zombies = new ArrayList<>();
		this.powerUps = new ArrayList<>();

		spawnPlayer();

		hud.setHealthLabel(player.getHealth());

		waveProgress = 0;

		levelLoaded = true;

		loadWave();

		System.out.println("Finished level loading");
	}

	public void spawnPlayer() {
		player = new Player(parent, 0.3f, level.getPlayerSpawn(), 0f, playerType);
		player.defineBody();
		addEntity(player);
	}

	public void loadWave() {
		System.out.println("Beginning wave loading...");
		boolean spawnBoss = false;
		activeBoss = false;
		List<Vector2> zombieSpawns = level.getZombieSpawns();
		switch (getWave(this.location, waveProgress)) {
		case LARGE:
			zombiesToSpawn = 4 * zombieSpawns.size();
			break;
		case MEDIUM:
			zombiesToSpawn = 3 * zombieSpawns.size();
			break;
		case SMALL:
			zombiesToSpawn = 2 * zombieSpawns.size();
			break;
		case MINIBOSS:
		case BOSS:
			zombiesToSpawn = 3 * zombieSpawns.size();
			spawnBoss = true;
			break;
		default:
			break;
		}
		hud.setProgressLabel(waveProgress + 1, zombiesToSpawn);
		spawnCooldown = 0;
		System.out.println("Zombies to spawn: " + zombiesToSpawn);

		if (waveProgress > 0) {
			PowerUp powerUp = new PowerUp(parent, 0.2f, level.getPlayerSpawn(), 0, randomPowerUpType.getRandom());
			powerUp.defineBody();
			addPowerUp(powerUp);
		}

		if (spawnBoss) {
			spawnBoss(zombieSpawns.get(0));
		}
		System.out.println("Finished wave loading");
	}
	
	public void spawnBoss(Vector2 spawnLocation) {
		BossZombie.Type type;
		Wave wave = getWave(this.location, waveProgress);
		if (wave.equals(Wave.MINIBOSS)) {
			type = BossZombie.Type.MINIBOSS;
		} else if (wave.equals(Wave.BOSS)) {
			type = BossZombie.Type.BOSS;
		} else {
			return;
		}
		BossZombie zombie = new BossZombie(parent, spawnLocation, 0, type);
		zombie.defineBody();
		addEntity(zombie);
		activeBoss = true;
	}

	public void spawnZombies(float delta) {
		if (spawnCooldown <= 0) {
			List<Vector2> zombieSpawns = level.getZombieSpawns();
			zombieSpawns.forEach(sp -> {
				Zombie zombie = new Zombie(parent, 0.3f, sp, 0,
						randomZombieType.getRandom());
				zombie.defineBody();
				addZombie(zombie);
				zombiesToSpawn -= 1;
			});
			spawnCooldown = 3f;
		} else {
			spawnCooldown -= delta;
		}
	}

	public void waveComplete() {
		System.out.println("Wave complete!");
		this.waveProgress += 1;

		if (waveProgress >= waves.get(location).length) {
			levelComplete();
		} else {
			loadWave();
		}
	}

	public void levelComplete() {
		if (location.getNum() + 1 > levelProgress) {
			levelProgress += 1;
		}
		levelLoaded = false;
		gameRunning = false;
		parent.changeScreen(Zepr.LEVEL_COMPLETE);
	}

	public void update(float delta) {
		if (!gameRunning)
			return;
		if (!levelLoaded)
			return;
		if (gameCamera == null || batch == null)
			return;

		// Check if the player has been killed
		if (player.getHealth() <= 0) {
			entities.forEach(e -> e.delete());
			this.entities.clear();
			this.zombies.clear();
			this.powerUps.clear();
			spawnPlayer();
			loadWave();
		}

		// Check if the wave has been completed
		if (zombies.size() + zombiesToSpawn == 0 && !activeBoss) {
			waveComplete();
		}
		
		hud.setBossLabel(activeBoss);

		// Resolve user input
		handleInput();

		// Re-centre the camera on the player after movement
		gameCamera.position.x = player.getX();
		gameCamera.position.y = player.getY();
		gameCamera.update();

		// Change the position of the map
		tiledMapRenderer.setView(gameCamera);

		if (zombiesToSpawn > 0) {
			spawnZombies(delta);
		}

		List<Entity> deadEntities = entities.stream().filter(e -> (!e.isAlive() && !(e instanceof Player)))
				.collect(Collectors.toList());
		deadEntities.forEach(e -> {
			e.delete();
			if (e instanceof Zombie) {
				zombies.remove(e);
			} else if (e instanceof PowerUp) {
				powerUps.remove(e);
			} else if (e instanceof BossZombie) {
				activeBoss = false;
			}
			entities.remove(e);
		});
		entities.forEach(e -> e.update(delta));

		int zombiesAlive = activeBoss ? zombies.size() + zombiesToSpawn + 1: zombies.size() + zombiesToSpawn;
		hud.setProgressLabel(waveProgress + 1, zombiesAlive);
		hud.setHealthLabel(player.getHealth());

		if (powerUps.size() > 0) {
			hud.setPowerUpLabel(powerUps.get(0).getType());
		} else {
			hud.setPowerUpLabel(null);
		}

		draw();

		// Step through the physics world simulation
		world.step(1 / 60f, 6, 2);
	}

	/**
	 * Retrieves user input from the {@link InputProcessor} and moves the player
	 * character accordingly.
	 */
	public void handleInput() {

		float modifier = 1f;
		float speed = player.getSpeed();

		if (player.isPowerUpActive(PowerUp.Type.SPEED)) {
			modifier = 2f;
		}

		speed *= modifier;

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

		// If both buttons for an axis are held down, or neither are, set velocity in
		// that axis to 0
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
		if (Zepr.devMode)
			debugRenderer.render(world, gameCamera.combined);
		hud.stage.draw();
	}

	@Override
	public void dispose() {
//		rayHandler.dispose();
		debugRenderer.dispose();
		world.dispose();
	}

}
