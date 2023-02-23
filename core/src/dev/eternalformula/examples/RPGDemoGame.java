package dev.eternalformula.examples;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

import dev.eternalformula.api.EFAPI;
import dev.eternalformula.api.scenes.SceneManager;
import dev.eternalformula.api.util.EFDebug;
import dev.eternalformula.examples.scenes.GameScene;

public class RPGDemoGame extends ApplicationAdapter {
	
	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		// Setup EFAPI
		EFAPI.handleInit();
		
		EFDebug.info("EFAPI " + EFAPI.API_VERSION + " has been initialized!");
		
		// Scene Creation
		SceneManager.getInstance().setCurrentScene(new GameScene());
		Gdx.graphics.setWindowedMode(1280, 720);
	}
	
	 @Override
	public void render() {
		super.render();
		
		float delta = Gdx.graphics.getDeltaTime();
		
		SceneManager sm = SceneManager.getInstance();
		
		if (sm.getCurrentScene() != null) {
			sm.update(delta);
			sm.draw(delta);
			sm.drawUI(delta);
		}
	}
	 
	@Override
	public void resize(int width, int height) {
		SceneManager.getInstance().resize(width, height);
	}
	 
	 @Override
	public void dispose() {
		super.dispose();
		SceneManager.getInstance().dispose();
	}

}