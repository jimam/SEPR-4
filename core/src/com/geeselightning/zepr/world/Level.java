package com.geeselightning.zepr.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.geeselightning.zepr.game.GameManager;
import com.geeselightning.zepr.game.Zepr;
import com.geeselightning.zepr.pathfinding.Connector;
import com.geeselightning.zepr.pathfinding.TileNode;
import com.geeselightning.zepr.util.Constant;

public class Level implements IndexedGraph<TileNode> {
	
	public enum Location {
		TOWN("Town", "town", 0),
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
	
	private int width;
	private int height;
	
	private Vector2 playerSpawn;
	private List<Vector2> zombieSpawnPoints;
	
	private Set<TileNode> aStarMap;
	private Map<Vector2, TileNode> walkableTiles;

	public Level(Zepr parent, Location location) {
		this.location = location;
		this.gameManager = GameManager.getInstance(parent);
		zombieSpawnPoints = new ArrayList<>();
		aStarMap = new HashSet<>();
		walkableTiles = new HashMap<>();
	}
	
	public TiledMap load() {
		tiledMap = new TmxMapLoader().load("maps/" + location.mapFileName + ".tmx");
		
		TiledMapTileLayer backgroundLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Background");
		width = backgroundLayer.getWidth();
		height = backgroundLayer.getHeight();
		
		TiledMapTileLayer pathLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Path");
		
		for(int i = 0; i < width; i++) {
			
			for(int j = 0; j < height; j++) {
				
				if (pathLayer.getCell(i, j) == null) {
					continue;
				}
				
				TileNode node = new TileNode(i,j);
				aStarMap.add(node);
				walkableTiles.put(new Vector2(i,j), node);
				
			}
			
		}
		
		for(TileNode node : aStarMap) {
			Vector2 above = new Vector2(node.getX(), node.getY() + 1);
			Vector2 left = new Vector2(node.getX() - 1, node.getY());
			Vector2 right = new Vector2(node.getX() + 1, node.getY());
			Vector2 below = new Vector2(node.getX(), node.getY() - 1);
			if (walkableTiles.containsKey(above)) {
				node.add(new Connector(node, walkableTiles.get(above)));
			}
			if (walkableTiles.containsKey(left)) {
				node.add(new Connector(node, walkableTiles.get(left)));
			}
			if (walkableTiles.containsKey(right)) {
				node.add(new Connector(node, walkableTiles.get(right)));
			}
			if (walkableTiles.containsKey(below)) {
				node.add(new Connector(node, walkableTiles.get(below)));
			}
		}
		
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
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	/**
	 * Translates Box2d world position into the tile row/column.
	 * @param worldPos	the world position to query for a tile
	 * @return	Vector2 representing the tile coordinates
	 */
	public Vector2 getTileCoordinates(Vector2 worldPos) {
		if (worldPos.x > width || worldPos.x < 0) {
			throw new IllegalArgumentException("x argument out of range!");
		}
		if (worldPos.y > height || worldPos.y < 0) {
			throw new IllegalArgumentException("y argument out of range!");
		}
		return new Vector2((float)Math.floor(worldPos.x), (float)Math.floor(worldPos.y));
	}

	@Override
	public int getNodeCount() {
		return aStarMap.size();
	}

	@Override
	public Array<Connection<TileNode>> getConnections(TileNode fromNode) {
		return fromNode.getConnections();
	}

	@Override
	public int getIndex(TileNode node) {
		if (node == null) System.out.println("WARNING: node is null!");
		return node.getIndex();
	}
	
	public Map<Vector2, TileNode> getWalkableTiles() {
		return walkableTiles;
	}
	
	public TileNode getTileNode(Vector2 tilePosition) {
		if (!walkableTiles.containsKey(tilePosition)) return null;
		return walkableTiles.get(tilePosition);
	}

}
