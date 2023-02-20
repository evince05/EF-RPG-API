package dev.eternalformula.examples.scenes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.api.maps.CustomTiledProperty;
import dev.eternalformula.api.scenes.Scene;
import dev.eternalformula.api.scenes.SceneManager;
import dev.eternalformula.api.world.GameWorld;

/**
 * GameScene demo class
 * 
 * @author EternalFormula
 * @since Alpha 0.0.1
 * @lastEdit Alpha 0.0.1 (02/09/23)
 *
 */
public class GameScene extends Scene {
	
	private GameWorld world;
	
	public GameScene() {
		this.world = GameWorld.createNewWorld();
		CustomTiledProperty.CUSTOM_TYPES_PATH = "examples/maps/forestMap/EFAPI-customtiledtypes.json";
		
		world.loadNewMapArea("examples/maps/forestMap/forestMap.tmx");
		SceneManager.getInstance().getGameCamera().position.set(new Vector2(10, 8), 0);
		
		System.out.println("MapEntity count: " + SceneManager.getInstance().getEngine().getEntities().size());
	}

	@Override
	public void update(float delta) {	
		world.update(delta);
	}

	@Override
	public void draw(SpriteBatch batch, float delta) {
		batch.begin();
		
		world.draw(batch, delta);
		
		batch.end();
	}

	@Override
	public void drawUI(SpriteBatch uiBatch, float delta) {
		
	}
	
	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}
	
	@Override
	public void dispose() {
		world.dispose();
	}

}
