package dev.eternalformula.api.ecs.systems;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.api.ecs.components.PositionComponent;
import dev.eternalformula.api.ecs.components.interfaces.TranslatableComponent;
import dev.eternalformula.api.ecs.components.physics.MotionComponent;

/**
 * The MotionSystem handles entity movement.
 *
 * @author EternalFormula
 * @since Alpha 0.0.5
 */

public class MotionSystem extends IteratingSystem {

	/**
	 * Basic MotionSystem constructor.
	 */
	
	public MotionSystem() {
		super(Family.all(MotionComponent.class, PositionComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		MotionComponent motionComp = MotionComponent.MAPPER.get(entity);
		PositionComponent posComp = PositionComponent.MAPPER.get(entity);
		
		if (!motionComp.acceleration.equals(Vector2.Zero)) {
			
			// Entity is accelerating
			motionComp.velocity.mulAdd(motionComp.acceleration, deltaTime);
		}
		
		if (!motionComp.velocity.equals(Vector2.Zero)) {
			
			// This should (hopefully) keep deltaPos and the physComp positions equal.
			Vector2 deltaPos = new Vector2(motionComp.velocity).scl(deltaTime); // scaling the velocity directly is bad.
			
			for (Component c : entity.getComponents()) {
				if (c instanceof TranslatableComponent) {
					((TranslatableComponent) c).translate(deltaPos);
				}
				
			}
			
			posComp.position.add(deltaPos);
			motionComp.isMoving = true;
		}
		else {
			motionComp.isMoving = false;
		}
		
		
	}

}
