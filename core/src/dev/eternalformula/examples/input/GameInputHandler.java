/**
 * 
 */
package dev.eternalformula.examples.input;

import com.badlogic.gdx.Input;

import dev.eternalformula.api.input.InputHandler;
import dev.eternalformula.examples.scenes.GameScene;

/**
 * The GameInputHandler is a demo InputHandler.
 *
 * @author EternalFormula
 * @since Alpha 0.0.3
 */

public class GameInputHandler implements InputHandler {

	private GameScene scene;
	private boolean blocked;
	
	private int handlerType;
	
	public GameInputHandler(GameScene scene) {
		this.scene = scene;
		this.handlerType = InputHandler.WORLD_HANDLER;
	}
	
	@Override
	public void onKeyDown(int keycode) {
		
		if (keycode == Input.Keys.W) {
			scene.cameraPos.y += 1f;
			
		}
		else if (keycode == Input.Keys.A) {
			scene.cameraPos.x -= 1f;
			
		}
		else if (keycode == Input.Keys.S) {
			scene.cameraPos.y -= 1f;
		}
		else if (keycode == Input.Keys.D) {
			scene.cameraPos.x += 1f;
		}
	}

	@Override
	public void onKeyUp(int keycode) {
	}

	@Override
	public void onKeyTyped(char key) {
	}

	@Override
	public void onMouseClicked(int x, int y, int button) {
	}

	@Override
	public void onMouseReleased(int x, int y, int button) {
	}

	@Override
	public void onMouseHovered(int x, int y) {
	}

	@Override
	public void onMouseDrag(int x, int y) {
	}
	
	@Override
	public boolean isBlocked() {
		return blocked;
	}
	
	@Override
	public void setBlocked(boolean isBlocked) {
		this.blocked = isBlocked;
	}

	@Override
	public void onMouseWheelScrolled(int direction) {
	}

	@Override
	public int getHandlerType() {
		return handlerType;
	}

}