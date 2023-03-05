/**
 * 
 */
package dev.eternalformula.api.ui.elements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.api.ui.UIElement;

/**
 * The EFLabel is a simple element. It does not have a skin,
 * and it simply acts as a text field.
 *
 * @author EternalFormula
 * @since Alpha 0.0.4 (SNAPSHOT-2.0)
 */

public class EFLabel extends UIElement {
	
	
	/* TODO (Later version): Add text-wrapping capabilities?
	 * 		Add support for multiple font types (.ttf, .fnt, etc)
	 *  	EFFont class?
	 */
	
	private BitmapFont font;
	
	private String text;
	
	public float xOffset;
	public float yOffset;
	
	public EFLabel(String text) {
		this.position = new Vector2(0f, 0f);
		this.text = text;
		this.font = new BitmapFont();
		this.font.setColor(Color.WHITE);
		
		// TODO: Add bounds from text (expands with text)
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public BitmapFont getFont() {
		return font;
	}
	
	public void setFont(BitmapFont font) {
		this.font = font;
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
	public void update(float delta) {
	}
	
	@Override
	public void onKeyTyped(char key) {
		super.onKeyTyped(key);
	}

	@Override
	public void draw(SpriteBatch uiBatch, float delta) {
		// Note that pos.x and pos.y represent the location of the top left corner of the text
		font.draw(uiBatch, text, position.x + xOffset, position.y + yOffset);
	}
}
