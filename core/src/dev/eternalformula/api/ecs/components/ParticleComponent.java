/**
 * 
 */
package dev.eternalformula.api.ecs.components;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.api.ecs.components.interfaces.EFComponent;
import dev.eternalformula.api.ecs.components.interfaces.TranslatableComponent;
import dev.eternalformula.api.scenes.SceneManager;

/**
 * The ParticleComponent houses particle effects.
 *
 * @author EternalFormula
 * @since Alpha 0.0.4
 */

public class ParticleComponent implements EFComponent, TranslatableComponent {

	public static final ComponentMapper<ParticleComponent> MAPPER =
			ComponentMapper.getFor(ParticleComponent.class);
	
	public ParticleEffect effect;
	
	@Override
	public ParticleComponent copy() {
		// Watch to see how this affects the original effect
		ParticleComponent particleComp = SceneManager.getInstance().getEngine()
				.createComponent(ParticleComponent.class);
		
		particleComp.effect = new ParticleEffect(effect);
		
		return particleComp;
	}

	@Override
	public void translate(Vector2 deltaPos) {
		
		for (ParticleEmitter emitter : effect.getEmitters()) {
			// the emitter's set position is really its own translate method
			emitter.setPosition(deltaPos.x, deltaPos.y);
		}
	}

}
