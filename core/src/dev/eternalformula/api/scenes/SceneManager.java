package dev.eternalformula.api.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.eternalformula.api.EFAPI;
import dev.eternalformula.api.util.EFDebug;
import dev.eternalformula.api.util.EFGFX;
import dev.eternalformula.api.viewports.ViewportHandler;

/**
 * The SceneManager.
 * 
 * @author EternalFormula
 * @since Alpha 0.0.1
 * @lastEdit Alpha 0.0.1 (02/09/23)
 */

public class SceneManager {
	
	private Scene currentScene;
	
	private ViewportHandler viewportHandler;
	
	private SpriteBatch gameBatch;
	private SpriteBatch uiBatch;
	
	private SceneManager() {
		this.currentScene = null;
		
		// Batch initialization
		this.gameBatch = new SpriteBatch();
		this.uiBatch = new SpriteBatch();
		
		// viewport
		this.viewportHandler = new ViewportHandler(EFGFX.DEFAULT_WIDTH,
				EFGFX.DEFAULT_HEIGHT);
	}
	
	/**
	 * Draws all non-UI-related stuff for the current scene.
	 * @param delta Time elapsed since last frame.
	 */
	
	public void draw(float delta) {
		
		if (currentScene != null) {
			
			// Batch prepping
			gameBatch.setProjectionMatrix(getGameCamera().combined);
			//Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glClear(GL20.GL_BLEND);
			
			currentScene.draw(gameBatch, delta);
		}
	}
	
	/**
	 * Draws all UI-related stuff for the current scene.
	 * @param delta Time elapsed since last frame.
	 */
	
	public void drawUI(float delta) {
		
		if (currentScene != null) {
			
			// Batch prepping
			uiBatch.setProjectionMatrix(getUICamera().combined);
			Gdx.gl.glEnable(GL20.GL_BLEND);
			
			currentScene.drawUI(uiBatch, delta);
		}
	}
	
	/**
	 * Handles all logic for the current scene.
	 * @param delta Time elapsed since last frame.
	 */
	
	public void update(float delta) {
		
		viewportHandler.update(delta);
		
		if (currentScene != null) {
			handleInput(delta);
			currentScene.update(delta);
		}
	}
	
	private void handleInput(float delta) {
		// inputManager.update(delta);
		// on input, scenemanager gets notified and procked?
	}
	
	public void resize(int width, int height) {
		viewportHandler.resize(width, height);
		
		if (currentScene != null) {
			currentScene.resize(width, height);
		}
	}
	
	public void dispose() {
		if (currentScene != null) {
			currentScene.dispose();
		}
	}
	
	public Scene getCurrentScene() {
		return currentScene;
	}
	
	public void setCurrentScene(Scene scene) {
		this.currentScene = scene;
	}
	
	public OrthographicCamera getGameCamera() {
		return (OrthographicCamera) viewportHandler.getGameViewport().getCamera();
	}
	
	public OrthographicCamera getUICamera() {
		return (OrthographicCamera) viewportHandler.getUIViewport().getCamera();
	}
	
	/**
	 * Returns the singleton instance for the SceneManager. Note that in order to use this,<br>
	 * {@link EFAPI#handleInit()} must first be executed, otherwise this will return null.
	 */
	
	public static SceneManager getInstance() {
		return EFAPI.getInstance().getSceneManager();
	}
	
	/**
	 * Creates a new instance of the SceneManager if it does not already exist.
	 */
	
	public static SceneManager createNewInstance() {
		if (SceneManager.getInstance() == null) {
			return new SceneManager();
		}
		else {
			EFDebug.info("An instance of the SceneManager already exists, so this call was ignored.");
		}
		return null;
	}

}
