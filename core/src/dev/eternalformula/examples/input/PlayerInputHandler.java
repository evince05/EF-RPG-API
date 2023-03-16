package dev.eternalformula.examples.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.api.ecs.components.StateComponent;
import dev.eternalformula.api.ecs.components.physics.MotionComponent;
import dev.eternalformula.api.input.InputHandler;
import dev.eternalformula.api.physics.Direction;
import dev.eternalformula.examples.ecs.entities.Player;
import dev.eternalformula.examples.physics.EntityState;

/**
 * The PlayerInputHandler 
 *
 * @author EternalFormula
 * @since Alpha 0.0.5
 */

public class PlayerInputHandler implements InputHandler {
	
	private Player player;
	
	public PlayerInputHandler(Player player) {
		this.player = player;
	}
	
	public PlayerInputHandler() {
	}
	
	public void update(float delta) {
		
		MotionComponent motionComp = MotionComponent.MAPPER.get(player);
		StateComponent stateComp = StateComponent.MAPPER.get(player);
		
		handleMovement(motionComp, stateComp);	
	}
	
	/**
	 * Handles player movement. This uses real-time input polling rather than keyDown
	 * and keyUp so that it is easier to control.
	 */
	
	private void handleMovement(MotionComponent motionComp, StateComponent stateComp) {
		Vector2 vel = new Vector2(0f, 0f);
		Direction dir = stateComp.direction;
		
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			vel.y = motionComp.speed; // * playerSpeed
			dir = Direction.NORTH;
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			vel.y = -motionComp.speed; // * playerSpeed
			dir = Direction.SOUTH;
		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			vel.x = -motionComp.speed;
			dir = Direction.WEST;
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			vel.x = motionComp.speed;
			dir = Direction.EAST;
		}
		
		if (vel.x != 0 || vel.y != 0) {
			// If the entity moves on either axis, the walking animation is triggered.
			stateComp.state = EntityState.WALKING;
			
			if (vel.x != 0 && vel.y != 0) {
				// Pythagorean stuff (special triangle)
				
				/*
				 * Typically, if this occurs, vel.x should equal vel.y
				 * Mathematically, this must be true for this to occur.
				 */
				
				vel.scl(1f / (float) Math.sqrt(2));
			}
		}
		else {
			stateComp.state = EntityState.IDLE;
		}
		
		if (!motionComp.velocity.equals(vel)) {
			motionComp.velocity = vel;
		}
		
		if (!stateComp.direction.equals(dir)) {
			stateComp.direction = dir;
		}
	}

	@Override
	public void onKeyDown(int keycode) {
	}

	@Override
	public void onKeyUp(int keycode) {
	}

	@Override
	public void onKeyTyped(char key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMouseClicked(int x, int y, int button) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMouseReleased(int x, int y, int button) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMouseHovered(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMouseDrag(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMouseWheelScrolled(int direction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getHandlerType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isBlocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setBlocked(boolean isBlocked) {
		// TODO Auto-generated method stub
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}

}
