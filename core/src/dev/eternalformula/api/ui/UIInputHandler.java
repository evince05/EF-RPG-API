package dev.eternalformula.api.ui;

import dev.eternalformula.api.input.InputHandler;

/**
 * The UIInputHandler is designed to pass the InputListener's input to a UIContainer.
 *
 * @author EternalFormula
 * @since Alpha 0.0.4 (SNAPSHOT-2.0)
 */

public class UIInputHandler implements InputHandler {
	
	private UIContainer container;
	
	private boolean blocked;
	
	public UIInputHandler() {
		this.container = null;
	}
	
	public UIInputHandler(UIContainer container) {
		this.container = container;
	}
	
	/**
	 * Sets a UIContainer to receive all input.<br>
	 * This overwrites any existing UIContainer to which
	 * this is already attached.
	 * 
	 * @param container The container to receive input.
	 */
	
	public void attachTo(UIContainer container) {
		this.container = container;
	}

	@Override
	public void onKeyDown(int keycode) {
	}

	@Override
	public void onKeyUp(int keycode) {
	}

	@Override
	public void onKeyTyped(char key) {
		if (container != null) {
			container.onKeyTyped(key);
		}
	}

	@Override
	public void onMouseClicked(int x, int y, int button) {
		if (container != null) {
			container.onMouseClicked(x, y, button);
		}
	}

	@Override
	public void onMouseReleased(int x, int y, int button) {
		if (container != null) {
			container.onMouseReleased(x, y, button);
		}
		
	}

	@Override
	public void onMouseHovered(int x, int y) {
		if (container != null) {
			container.onMouseHovered(x, y);
		}
	}

	@Override
	public void onMouseDrag(int x, int y) {
		if (container != null) {
			container.onMouseDrag(x, y);
		}
		
	}

	@Override
	public void onMouseWheelScrolled(int direction) {
		if (container != null) {
			container.onMouseWheelScrolled(direction);
		}
	}

	@Override
	public int getHandlerType() {
		return InputHandler.UI_HANDLER;
	}

	@Override
	public boolean isBlocked() {
		return blocked;
	}

	@Override
	public void setBlocked(boolean isBlocked) {
		this.blocked = isBlocked;
	}

}
