/**
 * 
 */
package dev.eternalformula.api.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;

import dev.eternalformula.api.ecs.components.physics.LightComponent;

/**
 * The LightSystem handles the logic of lights.
 *
 * @author EternalFormula
 * @since Alpha 0.0.2
 */
public class LightSystem extends IteratingSystem {

	public LightSystem() {
		super(Family.all(LightComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		LightComponent lightComp = entity.getComponent(LightComponent.class);
		
		// Flickering
		if (lightComp.shouldFlicker) {
			lightComp.flickerTimer += deltaTime;
			
			if (lightComp.flickerTimer >= lightComp.maxTimeBetweenFlicker) {
				
				// The light can flicker
				float dst = lightComp.lightDistance;
				float maxDisplacement = lightComp.maxFlickerDistance;
				
				float newDistance = MathUtils.random(dst - maxDisplacement, dst + maxDisplacement);
				lightComp.light.setDistance(newDistance);
				
				// Reset the flicker 
				lightComp.flickerTimer = 0f;
			}
		}
		
		
	}

}
