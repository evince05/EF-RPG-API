package dev.eternalformula.api.ecs.components;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.eternalformula.api.ecs.components.interfaces.EFComponent;
import dev.eternalformula.api.scenes.SceneManager;

/**
 * AnimationComponent
 *
 * @author EternalFormula
 * @since Alpha 0.0.3
 */

public class AnimationComponent implements EFComponent {
	
	public static final ComponentMapper<AnimationComponent> MAPPER =
			ComponentMapper.getFor(AnimationComponent.class);
	
	public Map<String, Animation<TextureRegion>> animations;
	public Animation<TextureRegion> currentAnim;
	
	public float animElapsedTime;

	public AnimationComponent() {
		this.animations = new HashMap<String, Animation<TextureRegion>>();
		this.currentAnim = null;
	}
	
	public Animation<TextureRegion> getAnimation(String animName) {
		return animations.get(animName);
	}
	
	public Animation<TextureRegion> getCurrentAnimation() {
		return currentAnim;
	}
	
	public void setAnimation(String animName) {
		if (animations.containsKey(animName)) {
			this.currentAnim = animations.get(animName);
			this.animElapsedTime = 0f;
		}	
	}
	
	public boolean isCurrentAnimFinished() {
		return currentAnim != null && currentAnim.isAnimationFinished(animElapsedTime);
	}

	@Override
	public AnimationComponent copy() {
		AnimationComponent animComp = SceneManager.getInstance().getEngine()
				.createComponent(AnimationComponent.class);
		
		return animComp;
	}
}
