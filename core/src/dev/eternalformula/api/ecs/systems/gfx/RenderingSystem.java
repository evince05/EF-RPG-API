package dev.eternalformula.api.ecs.systems.gfx;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.api.ecs.components.AnimationComponent;
import dev.eternalformula.api.ecs.components.MapEntityComponent;
import dev.eternalformula.api.ecs.components.PositionComponent;
import dev.eternalformula.api.ecs.components.StateComponent;
import dev.eternalformula.api.ecs.components.TextureComponent;
import dev.eternalformula.api.scenes.SceneManager;
import dev.eternalformula.api.util.EFDebug;
import dev.eternalformula.api.util.EFGFX;
import dev.eternalformula.api.util.Strings;

/**
 * RenderingSystem
 *
 * @author EternalFormula
 * @since Alpha 0.0.5
 * 
 * TODO: Idea: Add MapEntities to RenderingSystem (for Z order functionality with other entities
 * 	(eg. player behind tree)).
 */

public class RenderingSystem extends EntitySystem {

	private static final Family FAMILY = Family.one(AnimationComponent.class, TextureComponent.class)
			.exclude(MapEntityComponent.class).get();
	
	/**
	 * This system uses a manual draw call rather than in update() so
	 * proper layering can occur.
	 */
	
	public void draw(SpriteBatch gameBatch, float deltaTime) {
		
		for (Entity entity : SceneManager.getInstance().getEngine().getEntitiesFor(FAMILY)) {
			Vector2 pos = PositionComponent.MAPPER.get(entity).position;
			
			float width = 0f;
			float height = 0f;
			TextureRegion texReg;
			if (TextureComponent.MAPPER.has(entity)) {
				TextureComponent texComp = TextureComponent.MAPPER.get(entity);
				texReg = texComp.getTextureRegion();
				width = texComp.getWidth();
				height = texComp.getHeight();
			}
			else {
				// Animated
				AnimationComponent animComp = AnimationComponent.MAPPER.get(entity);
				
				if (StateComponent.MAPPER.has(entity)) {
					StateComponent stateComp = StateComponent.MAPPER.get(entity);
					
					String animName = stateComp.state.getAnimationPrefix() + "-"
							+ stateComp.direction.getAnimationSuffix();
					
					if (!animComp.currentAnimName.equals(animName)) {
						// Must set new anim
						animComp.setAnimation(animName);
					}
				}
				texReg = animComp.getCurrentAnimation().getKeyFrame(animComp.animElapsedTime);
				animComp.animElapsedTime += deltaTime;
				
				if (animComp.isCurrentAnimFinished()) {
					animComp.animElapsedTime = 0f;
				}
				
				width = texReg.getRegionWidth() / EFGFX.PPM;
				height = texReg.getRegionHeight() / EFGFX.PPM;
			}
			
			if (texReg != null) {
				gameBatch.draw(texReg, pos.x, pos.y, width, height);
			}

		}
	}
}
