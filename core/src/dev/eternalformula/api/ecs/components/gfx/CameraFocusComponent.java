package dev.eternalformula.api.ecs.components.gfx;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

/**
 * The CameraFocusComponent is a simple flag component.
 * It allows the entity to request focus (fixed attachment) from the camera.
 *
 * @author EternalFormula
 * @since Alpha 0.0.5
 */

public class CameraFocusComponent implements Component {
	
	public static final ComponentMapper<CameraFocusComponent> MAPPER = 
			ComponentMapper.getFor(CameraFocusComponent.class);
}
