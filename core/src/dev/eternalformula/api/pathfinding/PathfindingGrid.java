package dev.eternalformula.api.pathfinding;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import dev.eternalformula.api.ecs.components.physics.PhysicsComponent;
import dev.eternalformula.api.ecs.entities.MapEntity;
import dev.eternalformula.api.scenes.SceneManager;
import dev.eternalformula.api.world.GameWorld;

/**
 * PathfindingGrids are objects which are attached to the GameWorld object.
 * When a map is loaded, it loads a PathfindingGrid as well. This grid object
 * provides a way to locate and mark all universally-collidable tiles, which facilitates
 * easier (and hopefully more efficient) pathfinding for all entities. 
 *
 * @author EternalFormula
 * @since Alpha 0.0.5
 */

public class PathfindingGrid {
	
	private Array<MapEntity> mapEntities;
	
	private Color debugWalkableColor;
	private Color debugBlockedColor;
	
	private int mapWidth;
	private int mapHeight;
	
	private PathNode[][] nodeGrid;
	private boolean enableDebug;
	
	private PathfindingGrid(GameWorld world, TiledMap map, Array<MapEntity> mapEntities) {
		this.mapEntities = mapEntities;
		
		this.mapWidth = map.getProperties().get("width", int.class);
		this.mapHeight = map.getProperties().get("height", int.class);
		
		this.nodeGrid = new PathNode[mapHeight][mapWidth];
		
		generateNodeGrid();
		debugWalkableColor = new Color(0f, 1f, 0f, 0.2f);
		debugBlockedColor = new Color(1f, 0f, 0f, 0.2f);
	}
	
	public boolean isDebugEnabled() {
		return enableDebug;
	}
	
	public void setDebugMode(boolean enableDebug) {
		this.enableDebug = enableDebug;
	}
	
	/**
	 * Generates and fills the node grid based on the MapEntities of the world map.
	 */
	
	private void generateNodeGrid() {
		
		for (int worldX = 0; worldX < mapWidth; worldX++) {
			for (int worldY = 0; worldY < mapHeight; worldY++) {
				
				// Populates the grid ([0, 0] is bottom left corner)
				int flippedWorldY = mapHeight - 1 - worldY;
				PathNode node = new PathNode(new Vector2(worldX, worldY), true);
				nodeGrid[flippedWorldY][worldX] = node;
			}
		}
		
		// Assigning of blocked tiles based on colliders of objects
		for (MapEntity mapEnt : mapEntities) {
			if (PhysicsComponent.MAPPER.has(mapEnt)) {
				PhysicsComponent mapEntPhys = PhysicsComponent.MAPPER.get(mapEnt);
				
				/**
				 * TODO
				 * Typically, all mapentities cannot be traversed on their colliders.
				 * (Maybe) Add support for certain entities that don't obey these rules (lack of grid and just purely mathematical?)
				 */
				if (mapEntPhys.hasCollider()) {
					Vector2 colliderPos = mapEntPhys.colliderBody.getPosition();
					Vector2 colliderDims = mapEntPhys.colliderDimensions; // [width, height]
					
					// The division causes the rect's origin to be at the bottom left corner of the collider.
					Rectangle colliderRect = new Rectangle(colliderPos.x - colliderDims.x / 2f,
							colliderPos.y - colliderDims.y / 2f, colliderDims.x, colliderDims.y);
					
					int maxPosX = (int) Math.ceil(colliderRect.x + colliderRect.width);
					int maxPosY = (int) Math.ceil(colliderRect.y + colliderRect.height);
					
					for (int x = (int) Math.floor(colliderRect.x); x < maxPosX; x++) {
						for (int y = (int) Math.floor(colliderRect.y); y < maxPosY; y++) {
							getNodeFromWorldPos(x, y).walkable = false;
						}
					}
				}
			}
		}
	}
	
	/**
	 * Gets the node in the 2D grid array based on the given world position.
	 * @param worldX The x position (world units)
	 * @param worldY The y position (world units)
	 * @return The PathNode at the specified position
	 */
	
	public PathNode getNodeFromWorldPos(int worldX, int worldY) {
		int flippedY = mapHeight - 1 - worldY;
		return nodeGrid[flippedY][worldX];
	}
	
	public PathNode getNodeFromWorldPos(Vector2 worldPos) {
		return getNodeFromWorldPos((int) worldPos.x, (int) worldPos.y);
	}
	
	public void draw(SpriteBatch gameBatch, float delta) {
		
		ShapeRenderer sr = SceneManager.getInstance().getShapeRenderer();
		
		sr.begin();
		sr.set(ShapeType.Filled);
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		for (int x = 0; x < mapWidth; x++) {
			for (int y = 0; y < mapHeight; y++) {
				
				// Gets the node
				PathNode node = getNodeFromWorldPos(x, y);
				
				Color drawColor = (node.walkable ? debugWalkableColor : debugBlockedColor);
				sr.setColor(drawColor);
				sr.rect(x, y, 1f, 1f);
			}
		}
		sr.setColor(Color.WHITE);
				
		sr.end();
	}
	
	public Array<PathNode> getNeighbors(PathNode node) {
		Array<PathNode> neighbors = new Array<PathNode>();
		for (int x = - 1; x <= 1; x++) {
			for (int y = - 1; y <= 1; y++) {
				if (x == 0 && y == 0) {
					// This node is the center node (node passed in method args).
					continue;
				}
				
				int searchX = (int) node.worldPos.x + x;
				int searchY = (int) node.worldPos.y + y;
				
				// Check if the PathNode is within the map bounds
				if (searchX >= 0 && searchX < mapWidth && searchY >= 0 && searchY < mapHeight) {
					neighbors.add(getNodeFromWorldPos(searchX, searchY));
				}
			}
		}
		
		// Returns the filled array of neighbors.
		return neighbors;
	}
	
	/**
	 * Creates a grid to go along with the map of the specified GameWorld.<br>
	 * This way of passing everything in the constructor is a bit clunky, but it avoids
	 * having to iterate the grid twice to determine the stuff for regular tiles.
	 * 
	 * TODO: Consider rebuilding this system for ease of use and fluidity
	 * 
	 * @param world The GameWorld (to determine physics stuff)
	 * @param map The EFTiledMap to which the grid will belong.
	 * @param mapEntities An array of entities to be added to the map
	 * 
	 * @return A PathfindingGrid built from the world's EFTiledMap.
	 */
	
	public static PathfindingGrid createGridForWorld(GameWorld world, TiledMap map, Array<MapEntity> mapEntities) {
		return new PathfindingGrid(world, map, mapEntities);
	}
}
