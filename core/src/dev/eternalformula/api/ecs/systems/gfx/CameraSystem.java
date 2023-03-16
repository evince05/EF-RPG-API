package dev.eternalformula.api.ecs.systems.gfx;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.api.ecs.components.PositionComponent;
import dev.eternalformula.api.ecs.components.gfx.CameraFocusComponent;
import dev.eternalformula.api.scenes.SceneManager;
import dev.eternalformula.api.util.EFDebug;
import dev.eternalformula.api.util.Strings;

/**
 * The CameraSystem handles the logic of camera movement and attachment.
 *
 * @author EternalFormula
 * @since Alpha 0.0.5
 */

public class CameraSystem extends EntitySystem {
	
	private static final float DEFAULT_MOVE_SPEED = 3f;
	
	private Entity focusedEntity;
	
	private Vector2 focusedPos;
	private Vector2 targetPos;
	
	private boolean isMovingToNewPos;

	public CameraSystem() {
		super();
		this.focusedPos = new Vector2(0f, 0f);
		this.targetPos = new Vector2(focusedPos);
	}
	
	public Entity getFocusedEntity() {
		return focusedEntity;
	}
	
	/**
	 * Sets the entity on which the camera should focus.
	 * Note that this only works for entities with the {@link CameraFocusComponent}.
	 * 
	 * @param entity The entity on which to set the camera
	 */
	
	public void setFocusedEntity(Entity entity) {
		if (CameraFocusComponent.MAPPER.has(entity)) {
			this.focusedEntity = entity;
		}
	}
	
	/**
	 * Moves the camera's focus to a new position.
	 * Note that this will automatically remove focus from any
	 * currently-focused entity.
	 * 
	 * @param position The new camera position.
	 * @param instaMove True if the camera should automatically
	 * 		set itself at the new position, false if it should visually move over.
	 */
	
	public void moveFocusTo(Vector2 position, boolean instaMove) {
		
		this.focusedEntity = null;
		
		if (instaMove) {
			this.targetPos = position;
		}
		else {
			isMovingToNewPos = true;
		}
		
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if (isMovingToNewPos) {
			// Ignoring player focus.
		}
		else if (focusedEntity != null) {
			PositionComponent feComp = PositionComponent.MAPPER.get(focusedEntity);
			Vector2 entCenterPos = feComp.getCenterPosition();
			
			if (!focusedPos.epsilonEquals(entCenterPos)) {
				// Camera needs to update pos.
				focusedPos.set(entCenterPos);
			}
		}
		
		// Updates camera pos if a change has occurred
		Vector2 cameraPos = new Vector2(SceneManager.getInstance().getGameCamera().position.x,
				SceneManager.getInstance().getGameCamera().position.y);
		
		if (!cameraPos.epsilonEquals(focusedPos)) {
			SceneManager.getInstance().getGameCamera().position.set(focusedPos, 0f);
		}
	}
}
