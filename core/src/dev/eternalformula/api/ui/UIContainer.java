/**
 * 
 */
package dev.eternalformula.api.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * The UIContainer is a UIElement that acts as a container for other UIElements.
 *
 * @author EternalFormula
 * @since Alpha 0.0.4 (SNAPSHOT 2.0)
 */

public class UIContainer extends UIElement {
	
	protected Array<UIElement> children;
	
	public UIContainer() {
		super();
		this.children = new Array<UIElement>();
	}
	
	public Array<UIElement> getChildren() {
		return children;
	}
	
	public void addChild(UIElement element) {
		children.add(element);
		
		// Sets the UIContainer
		element.container = this;
		
	}
	
	public void addChildren(UIElement... elements) {
		for (UIElement e : elements) {
			addChild(e);
		}
	}

	@Override
	public void onMouseClicked(int x, int y, int button) {
		for (UIElement e : children) {
			if (e.isActive()) {
				e.onMouseClicked(x, y, button);
			}
		}
	}

	@Override
	public void onMouseReleased(int x, int y, int button) {
		for (UIElement e : children) {
			if (e.isActive()) {
				e.onMouseReleased(x, y, button);
			}
		}
	}

	@Override
	public void onMouseHovered(int x, int y) {
		for (UIElement e : children) {
			if (e.isActive()) {
				e.onMouseHovered(x, y);
			}
		}
	}
	
	@Override
	public void onKeyTyped(char key) {
		super.onKeyTyped(key); // watch for changes if you do something in parent
		for (UIElement e : children) {
			e.onKeyTyped(key);
		}
	}

	@Override
	public void update(float delta) {
		for (UIElement e : children) {
			if (e.isActive()) {
				e.update(delta);
			}
		}
	}

	@Override
	public void draw(SpriteBatch uiBatch, float delta) {
		for (UIElement e : children) {
			if (e.isVisible()) {
				e.draw(uiBatch, delta);
			}
		}
	}
	
	public boolean hasParent() {
		return container != null;
	}

}
