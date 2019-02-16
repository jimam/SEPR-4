package com.geeselightning.zepr.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;

public class Connector implements Connection<TileNode> {
	
	public TileNode owner;
	public TileNode other;
	
	public Connector(TileNode owner, TileNode other) {
		this.owner = owner;
		this.other = other;
	}

	@Override
	public float getCost() {
		return 0;
	}

	@Override
	public TileNode getFromNode() {
		return owner;
	}

	@Override
	public TileNode getToNode() {
		return other;
	}

}
