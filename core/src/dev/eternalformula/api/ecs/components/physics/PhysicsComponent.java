/**
 * 
 */
package dev.eternalformula.api.ecs.components.physics;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import dev.eternalformula.api.ecs.components.interfaces.EFComponent;
import dev.eternalformula.api.ecs.components.interfaces.TranslatableComponent;
import dev.eternalformula.api.physics.PhysicsUtil;
import dev.eternalformula.api.scenes.SceneManager;

/**
 * PhysicsComponent
 *
 * @author EternalFormula
 * @since Alpha 0.0.3
 */

public class PhysicsComponent implements EFComponent, TranslatableComponent {
	
	public static final ComponentMapper<PhysicsComponent> MAPPER =
			ComponentMapper.getFor(PhysicsComponent.class);

	// [width, height]
	public Body hitboxBody;
	public Body colliderBody;
	
	public Vector2 hitboxDimensions;
	public Vector2 colliderDimensions;
	
	public boolean hasHitbox() {
		return hitboxBody != null;
	}
	
	public boolean hasCollider() {
		return colliderBody != null;
	}

	@Override
	public PhysicsComponent copy() {
		PhysicsComponent comp = SceneManager.getInstance().getEngine()
				.createComponent(PhysicsComponent.class);
		
		// Not all entities will have both a collider and a hitbox
		if (hasHitbox()) {
			comp.hitboxDimensions = new Vector2(hitboxDimensions);
			Vector2 hitboxPos = hitboxBody.getPosition();
			
			comp.hitboxBody = PhysicsUtil.createBody(hitboxBody.getWorld(), hitboxPos.x,
					hitboxPos.y, hitboxDimensions.x, hitboxDimensions.y,
					hitboxBody.getType(), hitboxBody.getUserData());
		}
		
		if (hasCollider()) {
			comp.colliderDimensions = new Vector2(colliderDimensions);
			Vector2 colliderPos = colliderBody.getPosition();
			
			comp.colliderBody = PhysicsUtil.createBody(colliderBody.getWorld(), colliderPos.x,
					colliderPos.y, colliderDimensions.x, colliderDimensions.y,
					colliderBody.getType(), colliderBody.getUserData());
		}
		
		return comp;
	}

	@Override
	public void translate(Vector2 deltaPos) {
		
		// use setTransform (i think this is fixed on PC)
		hitboxBody.getPosition().x += deltaPos.x;
		colliderBody.getPosition().y += deltaPos.y;
	}
}
