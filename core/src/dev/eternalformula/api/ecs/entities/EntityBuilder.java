/**
 * 
 */
package dev.eternalformula.api.ecs.entities;

/**
 * EntityBuilder
 * @since Alpha 0.0.2 (02/16/2023)
 */

public class EntityBuilder {
	
	/**
	 * Creates a new MapEntity
	 * @param isAnimated Whether the MapEntity is animated or not
	 */
	
	public static MapEntity createMapEntity(boolean isAnimated) {
		return new MapEntity(isAnimated);
	}
}
