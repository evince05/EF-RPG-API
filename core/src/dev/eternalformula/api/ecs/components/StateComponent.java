package dev.eternalformula.api.ecs.components;

import com.badlogic.ashley.core.ComponentMapper;

import dev.eternalformula.api.ecs.components.interfaces.EFComponent;
import dev.eternalformula.api.physics.Direction;
import dev.eternalformula.api.physics.State;
import dev.eternalformula.api.scenes.SceneManager;

/**
 * The StateComponent determines entity state and direction.
 *
 * @author EternalFormula
 * @since Alpha 0.0.5
 */

public class StateComponent implements EFComponent {
	
	public static final ComponentMapper<StateComponent> MAPPER =
			ComponentMapper.getFor(StateComponent.class);

	public State state;
	
	public Direction direction;
	
	@Override
	public StateComponent copy() {
		
		StateComponent sc = SceneManager.getInstance().getEngine()
				.createComponent(StateComponent.class);
		
		sc.state = state;
		sc.direction = direction;
		
		return sc;
	}

}
