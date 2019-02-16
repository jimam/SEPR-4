package com.geeselightning.zepr.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class TileNode {
	
	private static int globalIndex;
	private int x;
	private int y;
	private int index;
	
	private Array<Connection<TileNode>> connections;
	
	public TileNode(int x, int y) {
		this.x = x;
		this.y = y;
		index = globalIndex;
		globalIndex++;
		this.connections = new Array<>();
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Vector2 getPosition() {
		return new Vector2(x,y);
	}
	
	public int getIndex() {
		return index;
	}
	
	public void add(Connection<TileNode> connector) {
		connections.add(connector);;
	}
	
	public Array<Connection<TileNode>> getConnections() {
		return connections;
	}

}
