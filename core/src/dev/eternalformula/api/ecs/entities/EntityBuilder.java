/**
 * 
 */
package dev.eternalformula.api.ecs.entities;

import com.badlogic.ashley.core.Entity;

/**
 * EntityBuilder {@value dev.eternalformula.api.EFAPI#API_VERSION}
 * @author EternalFormula @value dev.eternalformula.api.EFAPI#API_VERSION}
 * @since {@value dev.eternalformula.api.EFAPI#API_VERSION}
 * @lastEdit {@value dev.eternalformula.api.EFAPI#API_VERSION} (02/16/2023)
 */
public class EntityBuilder {
	
	/**
	 * Creates a new Entity.
	 * @param <T> The entity type
	 * @param clazz The entity class
	 * @return A new instance of the specified entity.
	 */
	
	public static Entity createEntity(Class<?> clazz) {
		if (clazz.equals(MapEntity.class)) {
			return new MapEntity();
		}
		
		return null;
	}
}
