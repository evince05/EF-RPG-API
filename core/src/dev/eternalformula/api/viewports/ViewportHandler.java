package dev.eternalformula.api.viewports;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import dev.eternalformula.api.util.EFGFX;

/**
 * Handles the math behind the viewports in order to achieve crisp pixel-perfect rendering.
 * 
 * @author EternalFormula
 * @since Alpha 0.0.1
 * @lastEdit Alpha 0.0.1 (02/09/23)
 */

public class ViewportHandler {

	private ScreenViewport viewport;
	private ScreenViewport uiViewport;

	private int width;
	private int height;

	/**
	 * Creates a viewport handler.
	 * @param width Initial viewport width
	 * @param height Initial viewport height
	 */
	
	public ViewportHandler(int width, int height) {

		this.width = width;
		this.height = height;

		// Game Viewport
		this.viewport = new ScreenViewport();
		viewport.setUnitsPerPixel(EFGFX.DEFAULT_UPP);
		viewport.update(width, height);

		// UI Viewport
		this.uiViewport = new ScreenViewport();
		uiViewport.setUnitsPerPixel(1 / 2f);
		uiViewport.update(width, height);

		uiViewport.getCamera().position.set(uiViewport.getWorldWidth() / 2f, 
				uiViewport.getWorldHeight() / 2f, 0f);
	}

	public void update(float delta) {
		viewport.update(width, height);
		viewport.getCamera().update();
		uiViewport.update(width, height);
		uiViewport.getCamera().update();
	}

	public void resize(int width, int height) {
		this.width = width;
		this.height = height;

		// Viewport
		float upp = (width / EFGFX.DEFAULT_WIDTH);
		EFGFX.RENDER_SCALE = 2 * upp;
		viewport.setUnitsPerPixel(EFGFX.DEFAULT_UPP / upp);

		// UIViewport
		uiViewport.setUnitsPerPixel(1f / EFGFX.RENDER_SCALE);
	}

	public ScreenViewport getGameViewport() {
		return viewport;
	}

	public ScreenViewport getUIViewport() {
		return uiViewport;
	}

	public float getWorldWidth() {
		return viewport.getWorldWidth();
	}

	public float getWorldHeight() {
		return viewport.getWorldHeight();
	}

	public Vector2 getUIDimensions() {
		return new Vector2(uiViewport.getWorldWidth(), uiViewport.getWorldHeight());
	}
}
