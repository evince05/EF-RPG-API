/**
 * 
 */
package dev.eternalformula.api.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * The UIElement is the foundation of the EF RPG API's UI system.<br>
 * UIElements come in a variety of forms - <b>buttons, textfields, sliders,</b><br>
 * and everything in between. They are extremely powerful, and the EF API RPG<br>
 * provides a wide variety of UIElements by default.
 *
 * @author EternalFormula
 * @since Alpha 0.0.4 (SNAPSHOT-2.0)
 */

public abstract class UIElement {
	
	UIContainer container;
	
	protected boolean visible;
	protected boolean active;
	
	protected Rectangle bounds;
	
	protected TextureRegion skin;
	
	protected Vector2 position;
	
	/**
	 * Empty UIElement constructor. This is often used for UIContainers.
	 */
	
	public UIElement() {
		this.visible = true;
		this.active = true;
		this.bounds = new Rectangle(0, 0, 0, 0);
		this.position = Vector2.Zero;
		this.container = null;
	}
	
	public void setSkin(TextureRegion skin) {
		this.skin = skin;
		this.bounds = new Rectangle(position.x, position.y,
				skin.getRegionWidth(), skin.getRegionHeight());
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public void setPosition(Vector2 pos) {
		setPosition(pos.x, pos.y);
	}
	
	public void setPosition(float x, float y) {
		this.position.x = x;
		this.position.y = y;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean isActive) {
		this.active = isActive;
	}
	
	/**
	 * Handles mouse clicking on the UIElement.<br>
	 * Note that this will not call if <code><b>active</b></code> is set to false.
	 * @param x The x location of the click (UI units)
	 * @param y The y location of the click (UI units)
	 * @param button The mouse button used during the click.
	 */
	
	public abstract void onMouseClicked(int x, int y, int button);
	
	/**
	 * Handles when the mouse releases.<br>
	 * Note that this will not call if <code><b>active</b></code> is set to false.
	 * @param x The x location of the release (UI units)
	 * @param y The y location of the release (UI units)
	 * @param button The mouse button used during the click.
	 */
	
	public abstract void onMouseReleased(int x, int y, int button);
	
	/**
	 * Handles when the mouse is hovered on the point <b>(x, y)</b>.
	 * @param x The x position of the point
	 * @param y The y position of the point
	 */
	
	public abstract void onMouseHovered(int x, int y);
	
	/**
	 * Handles when the mouse is dragged (int 
	 * @param x
	 * @param y
	 */
	public void onMouseDrag(int x, int y) {
	}
	
	public void onMouseWheelScrolled(int amount) {
	}
	
	public void onKeyTyped(char key) {
	}
	
	public abstract void update(float delta);
	
	public abstract void draw(SpriteBatch uiBatch, float delta);
	
	public UIContainer getParent() {
		return container;
	}
}
