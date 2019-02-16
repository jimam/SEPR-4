package com.geeselightning.zepr.pathfinding;

import com.badlogic.gdx.ai.pfa.Heuristic;

public class ManhattanDistanceHeuristic implements Heuristic<TileNode> {
	
	public static final ManhattanDistanceHeuristic instance =
			new ManhattanDistanceHeuristic();

	@Override
	public float estimate(TileNode node, TileNode endNode) {
		return Math.abs(endNode.getX() - node.getX()) + Math.abs(endNode.getY() - node.getY());
	}

}
