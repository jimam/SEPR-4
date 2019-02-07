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
	
	private Zepr parent;
	private GameManager gameManager;
	
	private String levelName;
	private TiledMap tiledMap;
	private int[] backgroundLayers = {0};
	private int[] foregroundLayers = {1};
	
	private Vector2 playerSpawn;
	private List<Vector2> zombieSpawnPoints;

	public Level(Zepr parent, String levelName) {
		this.parent = parent;
		this.levelName = levelName;
		this.gameManager = GameManager.getInstance(parent);
		zombieSpawnPoints = new ArrayList<>();
	}
	
	public TiledMap load() {
		tiledMap = new TmxMapLoader().load("maps/" + levelName + ".tmx");
		
		MapLayer spawnLayer = tiledMap.getLayers().get("Spawns");
		MapObjects spawnPoints = spawnLayer.getObjects();
		spawnPoints.forEach(sp -> {
			MapProperties props = sp.getProperties();
			String type = props.get("spawnType", String.class);
			float x = props.get("x", Float.class) / Constant.PPT;
			float y = props.get("y", Float.class) / Constant.PPT;
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

}
