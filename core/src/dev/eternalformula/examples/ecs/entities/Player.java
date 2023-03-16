package dev.eternalformula.examples.ecs.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.api.ecs.components.AnimationComponent;
import dev.eternalformula.api.ecs.components.PositionComponent;
import dev.eternalformula.api.ecs.components.StateComponent;
import dev.eternalformula.api.ecs.components.gfx.CameraFocusComponent;
import dev.eternalformula.api.ecs.components.nav.PathfindingComponent;
import dev.eternalformula.api.ecs.components.physics.MotionComponent;
import dev.eternalformula.api.ecs.loaders.AnimationComponentLoader;
import dev.eternalformula.api.physics.Direction;
import dev.eternalformula.api.scenes.SceneManager;
import dev.eternalformula.examples.physics.EntityState;

/**
 * Example class of a player object
 *
 * @author EternalFormula
 * @since Alpha 0.0.5
 */

public class Player extends Entity {
	
	public static Player createPlayer() {
		Player player = new Player();
		return player;
	}
	
	private Player() {
		PooledEngine ecsEngine = SceneManager.getInstance().getEngine();
		loadComponents(ecsEngine);
		
		ecsEngine.addEntity(this);
	}
	
	private void loadComponents(PooledEngine ecsEngine) {
		// Create components
		PositionComponent posComp = ecsEngine.createComponent(PositionComponent.class);
		AnimationComponent animComp = ecsEngine.createComponent(AnimationComponent.class);
		MotionComponent motionComp = ecsEngine.createComponent(MotionComponent.class);
		CameraFocusComponent cfComp = ecsEngine.createComponent(CameraFocusComponent.class);
		StateComponent stateComp = ecsEngine.createComponent(StateComponent.class);
		PathfindingComponent pfComp = ecsEngine.createComponent(PathfindingComponent.class);
		
		// Passing in values
		posComp.position = new Vector2(13.5f, 8);
		posComp.width = 1f;
		posComp.height = 2f;
	
		stateComp.direction = Direction.SOUTH;
		stateComp.state = EntityState.IDLE;
		
		motionComp.speed = 1.8f;
		
		AnimationComponentLoader.loadAnimations(animComp, "examples/textures/entities/player/player.ad");
		animComp.setAnimation("idle-down");
		
		// Add components to entity
		add(posComp);
		add(animComp);
		add(motionComp);
		add(cfComp);
		add(stateComp);
		add(pfComp);
	}
	
	/**
	 * Easy position access method.
	 */
	
	public Vector2 getPosition() {
		return PositionComponent.MAPPER.get(this).position;
	}
}
