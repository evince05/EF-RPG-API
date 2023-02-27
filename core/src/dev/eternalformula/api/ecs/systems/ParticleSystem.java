/**
 * 
 */
package dev.eternalformula.api.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.eternalformula.api.ecs.components.ParticleComponent;

/**
 * The ParticleSystem manages and handles any particles in the game.
 *
 * @author EternalFormula
 * @since Alpha 0.0.4
 */

public class ParticleSystem extends IteratingSystem {

	private static final Family FAMILY = Family.all(ParticleComponent.class).get();
	
	/**
	 * Creates a new Particle System.
	 */
	
	public ParticleSystem() {
		super(FAMILY);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		ParticleComponent particleComp = ParticleComponent.MAPPER.get(entity);
		particleComp.effect.update(deltaTime);
	}
	
	public void draw(SpriteBatch gameBatch, float delta) {
		for (Entity e : getEntities()) {
			ParticleEffect pEffect = ParticleComponent.MAPPER.get(e).effect;
			pEffect.draw(gameBatch, delta);
			if (pEffect.isComplete()) {
				pEffect.reset();
			}
		}
	}

}
