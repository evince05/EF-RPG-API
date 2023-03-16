package dev.eternalformula.api.physics;

/**
 * Simple enum representing the four cardinal directions.
 * Mainly used for animation purposes.
 * 
 * @author EternalFormula
 * @since Alpha 0.0.5
 */
public enum Direction {
	NORTH("up"),
	WEST("left"),
	EAST("right"),
	SOUTH("down");
	
	private String animSuffix;
	
	private Direction(String animSuffix) {
		this.animSuffix = animSuffix;
	}
	
	public String getAnimationSuffix() {
		return animSuffix;
	}
	
}
