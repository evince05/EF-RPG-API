/**
 * 
 */
package dev.eternalformula.api.input;

/**
 * The InputHandler interface acts as a template for handling various input events.
 * If the InputHandler is added to the InputListener, its methods will be called
 * whenever the InputListener receives input.
 *
 * @author EternalFormula
 * @since Alpha 0.0.3
 */

public interface InputHandler {
	
	/**
	 * Handler type. The WORLD_HANDLER type causes all screen coordinates to be
	 * converted to world coordinates.
	 */
	
	public static final int WORLD_HANDLER = 0;
	
	/**
	 * Handler type. The UI_HANDLER type causes all screen coordinates to be
	 * converted to UI coordinates.
	 */
	
	public static final int UI_HANDLER = 1;
	
	public void onKeyDown(int keycode);
	
	public void onKeyUp(int keycode);
	
	public void onKeyTyped(char key);
	
	public void onMouseClicked(int x, int y, int button);
	
	public void onMouseReleased(int x, int y, int button);
	
	public void onMouseHovered(int x, int y);
	
	public void onMouseDrag(int x, int y);
	
	/**
	 * Handles when the mouse wheel is scrolled.
	 * @param direction 1 if the mouse wheel is being scrolled down,
	 * 		-1 if it is being scrolled up.
	 */
	
	public void onMouseWheelScrolled(int direction);
	
	public int getHandlerType();
	
	/**
	 * Returns whether the input handler is blocked.
	 */
	
	public boolean isBlocked();
	
	/**
	 * Dictates whether the input handler should ignore the
	 * inputs received from the InputListener.
	 */
	
	public void setBlocked(boolean isBlocked);
}
