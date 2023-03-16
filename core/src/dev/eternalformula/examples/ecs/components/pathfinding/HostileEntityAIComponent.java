package dev.eternalformula.examples.ecs.components.pathfinding;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

import dev.eternalformula.api.ecs.components.interfaces.EFComponent;
import dev.eternalformula.api.scenes.SceneManager;

/**
 * The HostileEntityAIComponent is used to distinguish certain entities as enemies.
 *
 * @author EternalFormula
 * @since Alpha 0.0.5
 */

public class HostileEntityAIComponent implements EFComponent {

	public static final ComponentMapper<HostileEntityAIComponent> MAPPER =
			ComponentMapper.getFor(HostileEntityAIComponent.class);
	
	public HostileEntityBehavior entityBehavior;
	
	public Entity targetEntity;
	
	/**
	 * Default constructor. Mainly used to avoid issues when forgetting to set the behavior
	 */
	
	public HostileEntityAIComponent() {
		this.entityBehavior = HostileEntityBehavior.FOLLOW;
	}
	
	@Override
	public HostileEntityAIComponent copy() {
		HostileEntityAIComponent aiComp = SceneManager.getInstance().getEngine()
				.createComponent(HostileEntityAIComponent.class);
		
		aiComp.entityBehavior = entityBehavior;
		aiComp.targetEntity = targetEntity;
		
		return aiComp;
	}
	
	public enum HostileEntityBehavior {
		
		/**
		 * Simple following behavior (mainly used for melee combat)
		 */
		
		FOLLOW,
		
		/**
		 * Get within a desired range of the target.
		 * Used mainly for ranged combat.
		 */
		
		GET_WITHIN_RANGE,
		
		/**
		 * Keep the entity within a specified boundary.
		 */
		PATROL,
		
		/**
		 * The entity cannot move, yet it remains hostile.
		 */
		
		STATIONARY;
	}
}
