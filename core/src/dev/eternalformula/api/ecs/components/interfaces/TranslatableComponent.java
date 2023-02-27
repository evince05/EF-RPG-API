/**
 * 
 */
package dev.eternalformula.api.ecs.components.interfaces;

import com.badlogic.gdx.math.Vector2;

/**
 * The Translatable interface is an easy way for entities to update the positions
 * of their components.
 *
 * @author EternalFormula
 * @since Alpha 0.0.4
 */

public interface TranslatableComponent {
	
	/**
	 * Translates the component by a specific amount.
	 * @param deltaPos The amount to be translated.
	 */
	
	public void translate(Vector2 deltaPos);

}
