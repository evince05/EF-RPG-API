package dev.eternalformula.api.pathfinding;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;

import dev.eternalformula.api.scenes.SceneManager;
import dev.eternalformula.api.util.EFDebug;
import dev.eternalformula.api.util.Strings;
import dev.eternalformula.api.world.GameWorld;

/**
 * The path object is an implementation of the A* pathfinding algorithm.<br>
 * It enables objects to move from points A to B in the most optimal way.<br>
 * 
 * In the EF-RPG-API, each entity can have its own PathfindingComponent, which
 * references a Path object. However, certain optimizations occur.<br>
 * 
 * Since maps come in various dimensions, a unique node grid with dimensions
 * matching the size of the map.
 * 
 * TODO: Optimize this algo. The calcualtePath method alone takes roughly 13ms (tested on unplugged laptop;
 * 		note that one update tick is 16ms in a 60FPS loop)
 * 		
 * 		Idea: When repeatedly performing pathfinding calculations (eg. HostileEntity chasing player), 
 * 			instead of doing calcualtePath whenever playerPos != chaserPos, calculate an initial path,
 * 			follow said path, and only if the target's path changes course (node-based distance >= someValue),
 * 			reset the path.
 *
 * @author EternalFormula
 * @since Alpha 0.0.4 (SNAPSHOT-2.0)
 */

public class Path {

	// gCost = dist from starting node (whether that is 4-tile or 8-tile adjacent)
	// hCost = dist from end node
	// fCost = gCost + hCost
	
	// looks at fCost first :)
	
	/**
	 * The PathfindingGrid to which the path will belong.
	 */
	
	private PathfindingGrid pfGrid;
	
	private Vector2 startPos;
	private Vector2 endPos;
	
	private Queue<PathNode> pathPoints;
	
	/**
	 * Determines a path from points startPos to endPos using the A* pathfinding algorithm. 
	 * @param startPos
	 * @param endPos
	 * @return
	 */
	
	public static Path findPath(GameWorld world, Vector2 startPos, Vector2 endPos) {
		return new Path(world.getWorldMap().getPathfindingGrid(), startPos, endPos);
	}
	
	/**
	 * Creates a new path object.
	 * @param pfGrid The PathfindingGrid on which to calculate the path.
	 * @param startPos
	 * @param endPos
	 */
	
	private Path(PathfindingGrid pfGrid, Vector2 startPos, Vector2 endPos) {
		this.pfGrid = pfGrid;
		this.startPos = new Vector2(startPos);
		this.endPos = new Vector2(endPos);
		this.pathPoints = new Queue<PathNode>();
		
		
		long start = System.currentTimeMillis();
		calculatePath();
		long end = System.currentTimeMillis();
		
		EFDebug.info("Created path from " + Strings.vec2(startPos) + " to " +
				Strings.vec2(endPos) + " in " + (end - start) + "ms");
	}
	
	/**
	 * Calculates the optimal path using the A* algo.
	 * TODO: Add some comments LOL
	 */
	
	private void calculatePath() {
		
		EFDebug.info("Calculating path");
		PathNode startNode = pfGrid.getNodeFromWorldPos(startPos);
		PathNode endNode = pfGrid.getNodeFromWorldPos(endPos);
		
		Array<PathNode> closedNodes = new Array<PathNode>();
		Array<PathNode> openNodes = new Array<PathNode>();
		
		openNodes.add(startNode);
		
		while (openNodes.size > 0) {
			PathNode currentNode = openNodes.get(0);
			for (int i = 1; i < openNodes.size; i++) {
				PathNode node = openNodes.get(i);
				if (node.getFCost() < currentNode.getFCost() || 
						(node.getFCost() == currentNode.getFCost()
						&& node.hCost < currentNode.hCost)) {
					
					currentNode = node;
				}
			}
			
			openNodes.removeValue(currentNode, false);
			closedNodes.add(currentNode);
			
			if (currentNode.equals(endNode)) {
				// Path has been found.
				retraceAndSetPath(startNode, endNode);
				EFDebug.info("Returning full path"); 
				return;
			}
			
			for (PathNode neighbor : pfGrid.getNeighbors(currentNode)) {
				if (!neighbor.walkable || closedNodes.contains(neighbor, false)) {
					continue;
				}
				
				int movementCostToNeighbor = currentNode.gCost + 
						getDistanceBetweenNodes(currentNode, neighbor);
				
				if (movementCostToNeighbor < neighbor.gCost || !openNodes.contains(neighbor, false)) {
					
					// Potential next on path.
					if (getDistanceBetweenNodes(currentNode, neighbor) == 14) {
						// Diagonal node.
						
						int deltaX = (int) (neighbor.worldPos.x - currentNode.worldPos.x);
						int deltaY = (int) (neighbor.worldPos.y - currentNode.worldPos.y);
						
						PathNode deltaXNode = pfGrid.getNodeFromWorldPos((int) currentNode.worldPos.x + deltaX,
								(int) currentNode.worldPos.y);
						
						PathNode deltaYNode = pfGrid.getNodeFromWorldPos((int) currentNode.worldPos.x,
								(int) currentNode.worldPos.y + deltaY);
						
						if (!deltaXNode.walkable && !deltaYNode.walkable) {
							
							/*
							 * Path is blocked. Entities cannot travel through two tiles diagonally.
							 * O = Open tile, X = closed tile
							 * 
							 * Traversable: | Not Traversable:
							 * X O          | X O
							 * O O          | O X
							 */
							
							continue;
						}
					}
					neighbor.gCost = movementCostToNeighbor;
					neighbor.hCost = getDistanceBetweenNodes(neighbor, endNode);

					// Assigns parent (for tracing back to origin point)
					neighbor.parent = currentNode;
					
					if (!openNodes.contains(neighbor, false)) {
						openNodes.add(neighbor);
					}
				}
			}
		}
	}
	
	/**
	 * Retraces the path from the end node back to the start node,
	 * and then fills the pathPoints array with the created path.
	 * 
	 * @param startNode The starting node.
	 * @param endNode The end node.
	 */
	
	private void retraceAndSetPath(PathNode startNode, PathNode endNode) {
		
		/*
		 * Note: If you ever call this method after the initial path has been built,
		 * make sure you clear the pathPoints array.
		 */
		
		PathNode currentNode = endNode;
		while (!currentNode.equals(startNode)) {
			
			pathPoints.addFirst(currentNode);
			
			// Moves back down the path
			currentNode = currentNode.parent;
		}
	}
	
	public static int getDistanceBetweenNodes(PathNode nodeA, PathNode nodeB) {
		int dstX = (int) Math.abs(nodeA.worldPos.x - nodeB.worldPos.x);
		int dstY = (int) Math.abs(nodeA.worldPos.y - nodeB.worldPos.y);
		
		// 14 for diagonal ((sqrt(2) * 10)).
		// 10 for horizontal (1 * 10)
		
		/*
		 * Tries to move in a straight path.
		 * If dstX > dstY, the straight path will be travelled along the x axis.
		 * 		Then, whatever is left will be travelled diagonally.
		 * On the other hand, if dstX < dstY, the straight path will be travelled along the y axis.
		 * 		Then, whatever is left will be travelled diagonally.
		 */
		
		if (dstX > dstY) {
			return 14 * dstY + 10 * (dstX - dstY);
		}
		else {
			return 14 * dstX + 10 * (dstY - dstX);
		}
	}
	
	public boolean areNodesBlockedDiagonal(PathNode nodeA, PathNode nodeB) {
		int deltaX = (int) (nodeB.worldPos.x - nodeA.worldPos.x);
		int deltaY = (int) (nodeB.worldPos.y - nodeA.worldPos.y);
		
		PathNode nodeDX = pfGrid.getNodeFromWorldPos((int) nodeA.worldPos.x + deltaX,
				(int) nodeA.worldPos.y);
		
		PathNode nodeDY = pfGrid.getNodeFromWorldPos((int) nodeA.worldPos.x,
				(int) (nodeA.worldPos.y + deltaY));
		
		return !nodeDX.walkable && !nodeDY.walkable;
	}
	
	/**
	 * Draws the path (for debug purposes).
	 */
	
	public void draw(SpriteBatch gameBatch, float delta) {
		ShapeRenderer sr = SceneManager.getInstance().getShapeRenderer();
		
		sr.begin();
		sr.set(ShapeType.Filled);
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		sr.setColor(new Color(0.475f, 0.839f, 0.95f, 0.2f));
		for (PathNode node : pathPoints) {
			if (node.equals(pathPoints.first())) {
				sr.setColor(Color.GOLD);
			}
			else if (node.equals(pathPoints.get(pathPoints.size - 1))) {
				sr.setColor(Color.PURPLE);
			}
			
			sr.rect(node.worldPos.x, node.worldPos.y, 1f, 1f);
			sr.setColor(new Color(0.475f, 0.839f, 0.95f, 0.2f));
		}
		
		sr.end();
	}
	
	/**
	 * Gets (and removes) the next PathNode from the queue.
	 */
	
	public PathNode extractNextNode() {
		return pathPoints.removeFirst();
	}
	
	/**
	 * Returns whether the path has another following node.
	 */
	
	public boolean hasNextNode() {
		return pathPoints.size > 0;
	}
	
	public Vector2 getEndPosition() {
		return endPos;
	}
}