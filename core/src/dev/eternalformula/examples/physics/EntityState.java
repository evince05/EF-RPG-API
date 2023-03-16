package dev.eternalformula.examples.physics;

import dev.eternalformula.api.physics.State;

/**
 * The EntityState is a simple demonstration of a possible
 * implementation of the State interface.
 *
 * @author EternalFormula
 * @since Alpha 0.0.5
 */
public enum EntityState implements State {
	
	IDLE("idle"),
	WALKING("walk");

	private String animPrefix;
	
	private EntityState(String animPrefix) {
		this.animPrefix = animPrefix;
	}
	@Override
	public String getAnimationPrefix() {
		return animPrefix;
	}

}
