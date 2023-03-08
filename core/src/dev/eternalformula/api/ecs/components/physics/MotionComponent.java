package dev.eternalformula.api.ecs.components.physics;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.api.ecs.components.interfaces.EFComponent;
import dev.eternalformula.api.scenes.SceneManager;

/**
 * The MotionComponent permits entities to move.
 *
 * @author EternalFormula
 * @since Alpha 0.0.5
 */

public class MotionComponent implements EFComponent {

	public static final ComponentMapper<MotionComponent> MAPPER = 
			ComponentMapper.getFor(MotionComponent.class);
	
	public static final int DIRECTION_UP = 1;
	public static final int DIRECTION_LEFT = 2;
	public static final int DIRECTION_RIGHT = 3;
	public static final int DIRECTION_DOWN = 4;
	
	/**
	 * Velocity vector in the x and y directions; units m/s.
	 */
	
	public Vector2 velocity;
	
	/**
	 * Acceleration vector in the x and y directions; units m/s^2.
	 */
	
	public Vector2 acceleration;
	
	public boolean isMoving;
	
	private int direction;
	
	public MotionComponent() {
		this.direction = DIRECTION_DOWN;
		this.velocity = new Vector2(0f, 0f);
		this.acceleration = new Vector2(0f, 0f);
	}
	
	public Vector2 getVelocity() {
		return velocity;
	}
	
	public void setVelocity(Vector2 vel) {
		setVelocity(vel.x, vel.y);
	}
	
	public void setVelocity(float velX, float velY) {
		this.velocity.x = velX;
		this.velocity.y = velY;
	}
	
	public Vector2 getAcceleration() {
		return acceleration;
	}
	
	public void setAcceleration(Vector2 accel) {
		setAcceleration(accel.x, accel.y);
	}
	
	public void setAcceleration(float accelX, float accelY) {
		this.acceleration.x = accelX;
		this.acceleration.y = accelY;
	}
	
	public int getDirection() {
		return direction;
	}
	
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	@Override
	public MotionComponent copy() {
		MotionComponent newComp = SceneManager.getInstance().getEngine()
				.createComponent(MotionComponent.class);
		
		return newComp;
	}

}
