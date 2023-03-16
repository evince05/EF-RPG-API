package dev.eternalformula.examples.ecs.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import dev.eternalformula.api.ecs.components.PositionComponent;
import dev.eternalformula.api.ecs.components.TextureComponent;
import dev.eternalformula.api.ecs.components.gfx.CameraFocusComponent;
import dev.eternalformula.api.ecs.components.nav.PathfindingComponent;
import dev.eternalformula.api.ecs.components.physics.MotionComponent;
import dev.eternalformula.api.scenes.SceneManager;
import dev.eternalformula.examples.ecs.components.pathfinding.HostileEntityAIComponent;
import dev.eternalformula.examples.ecs.components.pathfinding.HostileEntityAIComponent.HostileEntityBehavior;

/**
 * The Glorb is a hostile entity.
 *
 * @author EternalFormula
 * @since Alpha 0.0.5
 */

public class Glorb extends Entity {
	
	public Glorb() {
		PooledEngine ecsEngine = SceneManager.getInstance().getEngine();
		loadComponents(ecsEngine);
		ecsEngine.addEntity(this);
	}
	
	private void loadComponents(PooledEngine ecsEngine) {
		
		PositionComponent posComp = ecsEngine.createComponent(PositionComponent.class);
		TextureComponent texComp = ecsEngine.createComponent(TextureComponent.class);
		HostileEntityAIComponent hostileAIComp = ecsEngine.createComponent(HostileEntityAIComponent.class);
		PathfindingComponent pfComp = ecsEngine.createComponent(PathfindingComponent.class);
		MotionComponent motionComp = ecsEngine.createComponent(MotionComponent.class);
		CameraFocusComponent cfComp = ecsEngine.createComponent(CameraFocusComponent.class);
		
		hostileAIComp.entityBehavior = HostileEntityBehavior.FOLLOW;
		motionComp.speed = 2f;
		
		add(motionComp);
		add(hostileAIComp);
		add(pfComp);
		add(posComp);
		add(texComp);
		add(cfComp);
	}
	
	// TODO: Add this to a hostileentity parent class.
	public void setTarget(Entity entity) {
		HostileEntityAIComponent.MAPPER.get(this).targetEntity = entity;
	}

}
