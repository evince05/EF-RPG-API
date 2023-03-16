package dev.eternalformula.examples.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.api.ecs.components.PositionComponent;
import dev.eternalformula.api.ecs.components.nav.PathfindingComponent;
import dev.eternalformula.api.pathfinding.Path;
import dev.eternalformula.api.util.EFDebug;
import dev.eternalformula.api.world.GameWorld;
import dev.eternalformula.examples.ecs.components.pathfinding.HostileEntityAIComponent;
import dev.eternalformula.examples.ecs.components.pathfinding.HostileEntityAIComponent.HostileEntityBehavior;

/**
 * The HostileEntityAISystem is a simple pathfinding application.
 *
 * @author EternalFormula
 * @since Alpha 0.0.5
 */

public class HostileEntityAISystem extends IteratingSystem {
	
	/**
	 * The distance at which a path will be recalcaulted if the
	 * position has changed. 
	 */
	public static final float FOLLOW_PATH_MIN_RECALC_DST = 2.5f;
	
	public HostileEntityAISystem() {
		super(Family.all(HostileEntityAIComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		HostileEntityAIComponent aiComp = HostileEntityAIComponent.MAPPER.get(entity);
		
		ComponentMapper<PositionComponent> posMapper = PositionComponent.MAPPER;
		
		Vector2 targetPos = posMapper.get(aiComp.targetEntity).position;
		
		if (PathfindingComponent.MAPPER.has(entity)) {
			PathfindingComponent pfComp = PathfindingComponent.MAPPER.get(entity);
			
			if (pfComp.getPath() == null) {
				// Path has not yet been created.
				
				EFDebug.info("Setting path for hostile entity!");
				pfComp.setPath(Path.findPath(GameWorld.getInstance(),
						posMapper.get(entity).position, targetPos));
			}
			
			// Tracking algo
			if (aiComp.entityBehavior == HostileEntityBehavior.FOLLOW) {
				Vector2 endPos = pfComp.getPath().getEndPosition();
				
				if (targetPos.dst(endPos) >= FOLLOW_PATH_MIN_RECALC_DST) {
					
					/*
					 * Entity has moved from their position where the path was calculated.
					 * Recalcualte path.
					 */
					
					pfComp.setPath(Path.findPath(GameWorld.getInstance(), 
							posMapper.get(entity).position, targetPos));
					
					EFDebug.info("Recalibrating hostile path");
				}
				else {
					EFDebug.info("Dst: " + targetPos.dst(endPos));
				}
			}
			
			EFDebug.info("Finished: " + pfComp.hasReachedFinalNode);
		}
		
		
	}
	
	
}
