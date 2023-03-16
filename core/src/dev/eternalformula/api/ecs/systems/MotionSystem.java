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
	
	/**
	 * Sets the velocity of an entity. This will cause constant movement at the specified velocity,
	 * until the entity's velocity is changed.
	 */
	
	public void setEntityVelocity(Entity e, Vector2 vel) {
		MotionComponent motionComp = MotionComponent.MAPPER.get(e);
		
		if (vel.x != 0 && vel.y != 0) {
			vel.scl(1f / (float) Math.sqrt(2));
		}
		motionComp.velocity = vel;
	}
	
	public void setEntityVelocity(Entity e, float moveX, float moveY) {
		setEntityVelocity(e, new Vector2(moveX, moveY));
	}
	
	/**
	 * Moves the entity by x world units along the horizontal axis and
	 * by y units along the vertical axis.
	 * 
	 * @param entity The entity to move.
	 */
	
	public void moveEntity(Entity entity, float x, float y) {
		moveEntity(entity, new Vector2(x, y));
	}
	
	public void moveEntity(Entity entity, Vector2 deltaPos) {
		PositionComponent posComp = PositionComponent.MAPPER.get(entity);
		if (deltaPos.x == deltaPos.y) {
			deltaPos.scl(1f /(float) Math.sqrt(2));
		}
		
		posComp.position.add(deltaPos);
		
		for (Component c : entity.getComponents()) {
			if (c instanceof TranslatableComponent) {
				((TranslatableComponent) c).translate(deltaPos);
			}
		}
	}

}
