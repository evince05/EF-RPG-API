/**
 * 
 */
package dev.eternalformula.api.ui.elements;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.eternalformula.api.ui.UIElement;
import dev.eternalformula.api.ui.actions.ButtonClickAction;

/**
 * The EFButton is one of the most direct ways to get user input.
 *
 * @author EternalFormula
 * @since Alpha 0.0.4 (SNAPSHOT-2.0)
 */

public class EFButton extends UIElement {
	
	public static final int DEFAULT = 0;
	public static final int TOGGLE = 1;
	
	private int buttonType;
	private boolean isClicked;
	
	private ButtonClickAction clickAction;
	
	public EFButton() {
		super();
	}
	
	/**
	 * Sets the code that should be executed whenever the button is clicked.
	 * @param clickAction The ButtonClickAction to occur
	 */
	
	public void setOnClick(ButtonClickAction clickAction) {
		this.clickAction = clickAction;
	}

	@Override
	public void onMouseClicked(int x, int y, int button) {
		
		if (bounds.contains(x, y)) {
			
			if (buttonType == EFButton.TOGGLE) {
				isClicked = !isClicked;
			}
			else {
				isClicked = true;
			}
			
			// Runs the button's click action
			if (isClicked && clickAction != null) {
				clickAction.onClick();
			}
		}
	}

	@Override
	public void onMouseReleased(int x, int y, int button) {
	}

	@Override
	public void onMouseHovered(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(SpriteBatch uiBatch, float delta) {
		uiBatch.draw(skin, position.x, position.y);
	}

}
