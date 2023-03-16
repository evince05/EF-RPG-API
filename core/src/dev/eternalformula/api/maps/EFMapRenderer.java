/**
 * 
 */
package dev.eternalformula.api.maps;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.eternalformula.api.ecs.systems.gfx.ParticleSystem;
import dev.eternalformula.api.scenes.SceneManager;
import dev.eternalformula.api.util.EFGFX;

/**
 * EFMapRenderer
 * 
 * @author EternalFormula
 * @since Alpha 0.0.2
 */
public class EFMapRenderer {
	
	private OrthogonalTiledMapRendererBleeding mapRenderer;
	private EFTiledMap tiledMap;
	
	public EFMapRenderer() {
		this.mapRenderer = new OrthogonalTiledMapRendererBleeding(null, 1 / EFGFX.PPM);
	}
	
	public void draw(SpriteBatch batch, float delta) {
		if (mapRenderer.getMap() != null) {
			mapRenderer.setView(SceneManager.getInstance().getGameCamera());
			mapRenderer.render();
			
			// Draw map objs
			tiledMap.draw(batch, delta);
			
			// Draw particles
			ParticleSystem particleSys = SceneManager.getInstance()
					.getEngine().getSystem(ParticleSystem.class);
			
			particleSys.draw(batch, delta);
			
		}
	}
	
	public void dispose() {
		mapRenderer.dispose();
	}
	
	public void setMap(EFTiledMap map) {
		this.tiledMap = map;
		mapRenderer.setMap(map.getMap());
	}

}
