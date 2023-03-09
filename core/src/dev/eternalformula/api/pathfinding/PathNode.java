package dev.eternalformula.api.pathfinding;

import com.badlogic.gdx.math.Vector2;

/**
 * PathNode
 *
 * @author EternalFormula
 * @since Alpha 0.0.5
 */

public class PathNode {
	
	public boolean walkable;
	
	public Vector2 worldPos;
	
	public int gCost;
	public int hCost;
	
	public PathNode parent;
	
	 // gCost + hCost
	
	public PathNode(Vector2 worldPos, boolean walkable) {
		this.worldPos = worldPos;
		this.walkable = walkable;
	}
	
	public int getFCost() {
		return gCost + hCost;
	}

}
