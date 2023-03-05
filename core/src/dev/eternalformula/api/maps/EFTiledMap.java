/**
 * 
 */
package dev.eternalformula.api.maps;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;

import dev.eternalformula.api.ecs.entities.MapEntity;

/**
 * EFTiledMap
 * @author EternalFormula
 * @since Alpha 0.0.2
 */

public class EFTiledMap {
	
	private Array<MapEntity> mapEntities;
	
	private TiledMap tiledMap;
	
	EFTiledMap(TiledMap tiledMap, Array<MapEntity> mapEntities) {
		this.tiledMap = tiledMap;
		this.mapEntities = mapEntities;
	}
	
	public void update(float delta) {
	}
	
	public void draw(SpriteBatch batch, float delta) {
		for (MapEntity entity : mapEntities) {
			entity.draw(batch, delta);
		}
	}
	
	public void dispose() {
		
	}
	
	public TiledMap getMap() {
		return tiledMap;
	}
}
