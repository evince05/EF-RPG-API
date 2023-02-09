package dev.eternalformula.examples.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.eternalformula.api.scenes.Scene;
import dev.eternalformula.api.util.EFDebug;

/**
 * GameScene demo class
 * 
 * @since Alpha 0.0.1
 * @lastEdit Alpha 0.0.1 (02/09/23)
 * @author EternalFormula
 *
 */
public class GameScene extends Scene {
	
	private TextureRegion badlogicReg;
	
	public GameScene() {
		badlogicReg = new TextureRegion(new Texture
				(Gdx.files.internal("badlogic.jpg")));
	}

	@Override
	public void update(float delta) {	
	}

	@Override
	public void draw(SpriteBatch batch, float delta) {
		batch.begin();
		EFDebug.info("New draw");
		batch.draw(badlogicReg, -6, -6, 12, 12);
		
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
		
	}

}
