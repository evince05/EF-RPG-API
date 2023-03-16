package dev.eternalformula.api.ecs.systems.nav;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.api.ecs.components.PositionComponent;
import dev.eternalformula.api.ecs.components.nav.PathfindingComponent;
import dev.eternalformula.api.ecs.components.physics.MotionComponent;
import dev.eternalformula.api.ecs.systems.MotionSystem;
import dev.eternalformula.api.scenes.SceneManager;
import dev.eternalformula.api.util.EFDebug;
import dev.eternalformula.api.util.EFGFX;
import dev.eternalformula.api.util.Strings;

/**
 * PathfindingSystem
 *
 * @author EternalFormula
 * @since Alpha 0.0.5
 */

public class PathfindingSystem extends IteratingSystem {

	public PathfindingSystem() {
		super(Family.all(PathfindingComponent.class, MotionComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		PathfindingComponent pfComp = PathfindingComponent.MAPPER.get(entity);
		MotionComponent motionComp = MotionComponent.MAPPER.get(entity);
		PositionComponent posComp = PositionComponent.MAPPER.get(entity);
		
		MotionSystem motionSys = SceneManager.getInstance()
				.getEngine().getSystem(MotionSystem.class);
		
		if (!pfComp.hasReachedFinalNode) {
			
			if (pfComp.targetNode != null) {
				Vector2 currentPos = posComp.position;
				
				Vector2 targetPos = pfComp.targetNode.worldPos;
				
				// 1px error margin
				if (!currentPos.epsilonEquals(targetPos, 0.0625f)) {
					// Positions do not match. Entity must move
					float dstX = targetPos.x - currentPos.x;
					float dstY = targetPos.y - currentPos.y;
					
					float dirX = Math.signum(dstX);
					float dirY = Math.signum(dstY);
					
					float aDstX = Math.abs(dstX);
					float aDstY = Math.abs(dstY);
					
					Vector2 entityDelta = new Vector2(0f, 0f);
					
					if (Math.abs(aDstX - aDstY) < 2f / EFGFX.PPM) {
						// Distances are virtually equal. Must move diagonally.
						entityDelta.x = dirX * motionComp.speed * deltaTime;
						entityDelta.y = dirY * motionComp.speed * deltaTime;
						
					}
					else if (Math.abs(dstX) > Math.abs(dstY)) {
						// Furthest away on x-axis -> move x first
						entityDelta.x = dirX * motionComp.speed * deltaTime;
					}
					else {
						// Furthest away on y-axis. -> move y first
						entityDelta.y = dirY * motionComp.speed * deltaTime;
					}
					
					/*
					if (Math.abs(currentPos.x + entityDelta.x) > Math.abs(targetPos.x)) {
						// Movement will overshoot on x axis. Capping
						EFDebug.info("Finish x [" + entityDelta.x + ", " + targetPos.x + "]");
						entityDelta.x = targetPos.x - currentPos.x;
					}*/
					
					/*
					if (Math.abs(currentPos.y + entityDelta.y) > Math.abs(targetPos.y)) {
						// Movement will overshoot on y axis. Capping
						EFDebug.info("Finish y [" + entityDelta.y + ", " + targetPos.y + "]");
						entityDelta.y = targetPos.y - currentPos.y;
						
					}*/
					
					motionSys.moveEntity(entity, entityDelta);
				}
				else {
					// Entity has reached target node.
					if (pfComp.getPath().hasNextNode()) {
						pfComp.targetNode = pfComp.getPath().extractNextNode();
						posComp.position = targetPos;
					}
					else {
						// Path is finished.
						pfComp.hasReachedFinalNode = true;
						motionComp.velocity = Vector2.Zero;
						
						// TODO: entity -> idle
					}
				}
			}
		}
	}
}
