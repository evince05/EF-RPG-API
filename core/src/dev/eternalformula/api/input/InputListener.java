/**
 * 
 */
package dev.eternalformula.api.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import dev.eternalformula.api.EFAPI;
import dev.eternalformula.api.scenes.SceneManager;
import dev.eternalformula.api.util.EFDebug;
import dev.eternalformula.api.viewports.ViewportHandler;

/**
 * The InputListener handles input from the keyboard and mouse and
 * passes it off wherever necessary.
 *
 * @author EternalFormula
 * @since Alpha 0.0.3
 */

public class InputListener implements InputProcessor {
	
	private Array<InputHandler> inputHandlers;
	
	private InputListener() {
		this.inputHandlers = new Array<InputHandler>();
	}
	
	/**
	 * Returns the singleton instance of the InputListener. Note that in order to use this,<br>
	 * {@link EFAPI#handleInit()} must first be called, otherwise this will return null.
	 */
	
	public static InputListener getInstance() {
		return EFAPI.getInstance().getInputListener();
	}
	
	public static InputListener createNewInstance() {
		if (InputListener.getInstance() == null) {
			return new InputListener();
		}
		else {
			EFDebug.warn("An instance of the InputListener already exists, "
					+ "so this call was ignored.");
		}
		return null;
	}
	
	public void addInputHandler(InputHandler inHandler) {
		inputHandlers.add(inHandler);
	}
	
	public void removeInputHandler(InputHandler inHandler) {
		inputHandlers.removeValue(inHandler, false);
	}

	@Override
	public boolean keyDown(int keycode) {
		for (InputHandler inHandler : inputHandlers) {
			inHandler.onKeyDown(keycode);
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		for (InputHandler inHandler : inputHandlers) {
			inHandler.onKeyUp(keycode);
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		for (InputHandler inHandler : inputHandlers) {
			inHandler.onKeyTyped(character);
		}
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		Vector2 worldCoords = unprojectCoordsToGame(screenX, screenY);
		Vector2 uiCoords = unprojectCoordsToUI(screenX, screenY);
		
		for (InputHandler inHandler : inputHandlers) {
			if (inHandler.getHandlerType() == InputHandler.WORLD_HANDLER) {
				inHandler.onMouseClicked((int) worldCoords.x, (int) worldCoords.y, button);
			}
			else {
				// Must be UI InputHandler
				inHandler.onMouseClicked((int) uiCoords.x, (int) uiCoords.y, button);
			}
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		
		Vector2 worldCoords = unprojectCoordsToGame(screenX, screenY);
		Vector2 uiCoords = unprojectCoordsToUI(screenX, screenY);
		
		for (InputHandler inHandler : inputHandlers) {
			if (inHandler.getHandlerType() == InputHandler.WORLD_HANDLER) {
				inHandler.onMouseClicked((int) worldCoords.x, (int) worldCoords.y, button);
			}
			else {
				// Must be UI InputHandler
				inHandler.onMouseReleased((int) uiCoords.x, (int) uiCoords.y, button);
			}
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		
		Vector2 worldCoords = unprojectCoordsToGame(screenX, screenY);
		Vector2 uiCoords = unprojectCoordsToUI(screenX, screenY);
		
		for (InputHandler inHandler : inputHandlers) {
			if (inHandler.getHandlerType() == InputHandler.WORLD_HANDLER) {
				inHandler.onMouseDrag((int) worldCoords.x, (int) worldCoords.y);
			}
			else {
				// Must be UI InputHandler
				inHandler.onMouseDrag((int) uiCoords.x, (int) uiCoords.y);
			}
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		Vector2 worldCoords = unprojectCoordsToGame(screenX, screenY);
		Vector2 uiCoords = unprojectCoordsToUI(screenX, screenY);
		
		for (InputHandler inHandler : inputHandlers) {
			if (inHandler.getHandlerType() == InputHandler.WORLD_HANDLER) {
				inHandler.onMouseHovered((int) worldCoords.x, (int) worldCoords.y);
			}
			else {
				// Must be UI InputHandler
				inHandler.onMouseHovered((int) uiCoords.x, (int) uiCoords.y);
			}
		}
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		
		for (InputHandler inHandler : inputHandlers) {
			inHandler.onMouseWheelScrolled((int) amountY);
		}
		return false;
	}
	
	/**
	 * Converts screen coordinates to world coordinates.
	 * @param screenX The screen X coordinate
	 * @param screenY The screen Y coordinate
	 * @return The local world coordinates at the desired position.
	 */
	
	private Vector2 unprojectCoordsToGame(int screenX, int screenY) {
		ViewportHandler vh = SceneManager.getInstance().getViewportHandler();
		return vh.getGameViewport().unproject(new Vector2(screenX, screenY));
	}
	
	/**
	 * Converts screen coordinates to UI coordinates.
	 * @param screenX The screen X coordinate
	 * @param screenY The screen Y coordinate
	 * @return The local UI coordinates at the desired position.
	 */
	
	private Vector2 unprojectCoordsToUI(int screenX, int screenY) {
		ViewportHandler vh = SceneManager.getInstance().getViewportHandler();
		return vh.getUIViewport().unproject(new Vector2(screenX, screenY));
	}

}
