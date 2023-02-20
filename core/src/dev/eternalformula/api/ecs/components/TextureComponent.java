/**
 * 
 */
package dev.eternalformula.api.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.eternalformula.api.util.EFGFX;

/**
 * AnimationComponent                                                                                                                                                      
 * @author EternalFormula
 * @since Alpha 0.0.2
 */
public class TextureComponent implements Component {
	
	public static final ComponentMapper<TextureComponent> Map =
			ComponentMapper.getFor(TextureComponent.class);
	
	private TextureRegion region;
	
	public boolean isHidden = false;
	
	private float texWidth;
	private float texHeight;

	public TextureRegion getTextureRegion() {
		return region;
	}
	
	public void setTextureRegion(TextureRegion reg) {
		this.region = reg;
		this.texWidth = reg.getRegionWidth() / EFGFX.PPM;
		this.texHeight = reg.getRegionHeight() / EFGFX.PPM;
	}
	
	/**
	 * Gets the width of the texture (in world units).
	 */
	
	public float getWidth() {
		return texWidth;
	}
	
	/**
	 * Gets the height of the texture (in world units).
	 */
	
	public float getHeight() {
		return texHeight;
	}

}
