/**
 * 
 */
package dev.eternalformula.api.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;

import dev.eternalformula.api.ecs.entities.MapEntity;
import dev.eternalformula.api.pathfinding.PathfindingGrid;

/**
 * EFTiledMap
 * @author EternalFormula
 * @since Alpha 0.0.2
 */

public class EFTiledMap {
	
	private Array<MapEntity> mapEntities;
	private TiledMap tiledMap;
	
	private PathfindingGrid pfGrid;
	
	private int widthInWorldUnits;
	private int heightInWorldUnits;
	
	EFTiledMap(TiledMap tiledMap, Array<MapEntity> mapEntities, PathfindingGrid pfGrid) {
		this.tiledMap = tiledMap;
		this.mapEntities = mapEntities;
		
		this.widthInWorldUnits = tiledMap.getProperties().get("width", int.class);
		this.heightInWorldUnits = tiledMap.getProperties().get("height", int.class);
		
		this.pfGrid = pfGrid;
	}
	
	public void update(float delta) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
			pfGrid.setDebugMode(!pfGrid.isDebugEnabled());
		}
	}
	
	public void draw(SpriteBatch batch, float delta) {
		for (MapEntity entity : mapEntities) {
			entity.draw(batch, delta);
		}
		
		if (pfGrid.isDebugEnabled()) {
			// Draw the PFGrid
			batch.end();
			
			pfGrid.draw(batch, delta);
			
			batch.begin();
		}
	}
	
	public void dispose() {
		
	}
	
	public TiledMap getMap() {
		return tiledMap;
	}
	
	public Array<MapEntity> getMapEntities() {
		return mapEntities;
	}
	
	/**
	 * Gets the grid on which pathfinding calculations are
	 * performed for this EFTiledMap.
	 */
	
	public PathfindingGrid getPathfindingGrid() {
		return pfGrid;
	}
	
	/**
	 * Gets the map width (in world units)
	 */
	
	public int getMapWidth() {
		return widthInWorldUnits;
	}
	
	/**
	 * Gets the map height (in world units)
	 */
	
	public int getMapHeight() {
		return heightInWorldUnits;
	}
}
