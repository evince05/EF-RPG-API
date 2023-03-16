package dev.eternalformula.api.physics;

/**
 * The State interface is a non-game specific interface for the State enum.
 * Ideally, a game would implement the state interface in an enum like "EntityState" or something,
 * which would provide game-specific enum values to suit the different states of entities in said game
 * (eg. running, swimming, idle, dead, etc.)
 *
 * @author EternalFormula
 * @since Alpha 0.0.5
 */

public interface State {
	
	/**
	 * Gets the animation prefix for the state.
	 * Animation names are usually written in the format "animName-direction",
	 * 		so this should return "animName".
	 */
	
	public String getAnimationPrefix();

}
