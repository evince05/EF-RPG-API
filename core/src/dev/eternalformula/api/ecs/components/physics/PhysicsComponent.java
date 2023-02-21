/**
 * 
 */
package dev.eternalformula.api.ecs.components.physics;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * PhysicsComponent
 *
 * @author EternalFormula
 * @since Alpha 0.0.3
 */

public class PhysicsComponent implements Component {

	public Body hitboxBody;
	public Body colliderBody;
	
	public boolean hasHitbox() {
		return hitboxBody != null;
	}
	
	public boolean hasCollider() {
		return colliderBody != null;
	}
}
