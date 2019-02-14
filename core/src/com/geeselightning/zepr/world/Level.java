package com.geeselightning.zepr.world;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.geeselightning.zepr.game.GameManager;
import com.geeselightning.zepr.game.Zepr;
import com.geeselightning.zepr.util.Constant;

public class Level {
	
	public enum Location {
		TOWN("Town", "townmap", 0),
		HALIFAX("Halifax College", "halifaxmap", 1),
		COURTYARD("Courtyard", "courtyard", 2);
		
		String name;
		String mapFileName;
		int num;
		
		Location(String name, String mapFileName, int num) {
			this.name= name;
			this.mapFileName = mapFileName;
			this.num = num;
		}
		
		public int getNum() {
			return num;
		}
	}
	
	private GameManager gameManager;
	
	private Location location;
	private TiledMap tiledMap;
	private int[] backgroundLayers = {0};
	private int[] foregroundLayers = {1};
	
	private Vector2 playerSpawn;
	private List<Vector2> zombieSpawnPoints;

	public Level(Zepr parent, Location location) {
		this.location = location;
		this.gameManager = GameManager.getInstance(parent);
		zombieSpawnPoints = new ArrayList<>();
	}
	
	public TiledMap load() {
		tiledMap = new TmxMapLoader().load("maps/" + location.mapFileName + ".tmx");
		
		MapLayer spawnLayer = tiledMap.getLayers().get("Spawns");
		MapObjects spawnPoints = spawnLayer.getObjects();
		spawnPoints.forEach(sp -> {
			MapProperties props = sp.getProperties();
			String type = props.get("type", String.class);
			float x = props.get("x", Float.class) / Constant.PPT;
			float y = props.get("y", Float.class) / Constant.PPT;
			if (type == null) {
				return;
			}
			if (type.equals("player")) {
				playerSpawn = new Vector2(x,y);
			} else if (type.equals("zombie")) {
				zombieSpawnPoints.add(new Vector2(x,y));
			} else {
				System.out.println("Unrecognised spawnpoint type '" + type + "'.");
			}
		});
		
		MapBodyBuilder.buildBodies(tiledMap, gameManager.getWorld());
		
		return tiledMap;
	}
	
	public TiledMap getTiledMap() {
		return tiledMap;
	}
	
	public int[] getBackgroundLayers() {
		return backgroundLayers;
	}
	
	public int[] getForegroundLayers() {
		return foregroundLayers;
	}
	
	public Vector2 getPlayerSpawn() {
		return playerSpawn;
	}
	
	public List<Vector2> getZombieSpawns() {
		return zombieSpawnPoints;
	}

}
