package dev.eternalformula.api.scenes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Scene {
	
	public abstract void update(float delta);
	
	public abstract void draw(SpriteBatch batch, float delta);
	
	public abstract void drawUI(SpriteBatch uiBatch, float delta);
	
	public abstract void resize(int width, int height);
	
	public abstract void pause();
	
	public abstract void resume();
	
	public abstract void dispose();

}
