package dev.eternalformula.examples.scenes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.api.input.InputListener;
import dev.eternalformula.api.scenes.Scene;
import dev.eternalformula.api.scenes.SceneManager;
import dev.eternalformula.api.util.EFDebug;
import dev.eternalformula.api.world.GameWorld;
import dev.eternalformula.examples.input.GameInputHandler;

/**
 * GameScene demo class
 * 
 * @author EternalFormula
 * @since Alpha 0.0.1
 * @lastEdit Alpha 0.0.1 (02/09/23)
 *
 */
public class GameScene extends Scene {
	
	private GameInputHandler inputHandler;
	private GameWorld world;
	
	private OrthographicCamera camera;
	public Vector2 cameraPos;
	
	public GameScene() {
		this.world = GameWorld.createNewWorld();
		
		long start = System.currentTimeMillis();
		world.loadNewMapArea("examples/maps/forestMap/forestMap.tmx");
		long end = System.currentTimeMillis();
		
		EFDebug.info("Loaded map area in " + ((end - start) / 1000D) + "s");
		
		this.camera = SceneManager.getInstance().getGameCamera();
		
		this.cameraPos = new Vector2(10, 8);
		camera.position.set(cameraPos, 0f);
		this.inputHandler = new GameInputHandler(this);
		InputListener.getInstance().addInputHandler(inputHandler);
	}

	@Override
	public void update(float delta) {	
		world.update(delta);
		
		camera.position.set(cameraPos, 0f);
		camera.update();
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