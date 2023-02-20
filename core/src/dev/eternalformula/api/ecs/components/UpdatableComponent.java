/**
 * 
 */
package dev.eternalformula.api.ecs.components;

import com.badlogic.ashley.core.Component;

/**
 * UpdatableComponents are components that require updating.
 * For example, the LightComponent uses this interface, as the radius of the light
 * needs constant updating to achieve a flickering effect.
 *
 * @author EternalFormula
 * @since Alpha 0.0.2
 */

public interface UpdatableComponent extends Component {
	
	public void update(float delta);

}
