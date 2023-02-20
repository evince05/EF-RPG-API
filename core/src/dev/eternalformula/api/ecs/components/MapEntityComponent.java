/**
 * 
 */
package dev.eternalformula.api.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

/**
 * The MapEntityComponent is a simple flag component for easy filtering.
 *
 * @author EternalFormula
 * @since Alpha 0.0.2
 */

public class MapEntityComponent implements Component {
	
	public static final ComponentMapper<MapEntityComponent> Map =
			ComponentMapper.getFor(MapEntityComponent.class);

}
