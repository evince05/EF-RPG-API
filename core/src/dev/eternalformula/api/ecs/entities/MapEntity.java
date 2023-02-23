/**
 * 
 */
package dev.eternalformula.api.ecs.entities;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import box2dLight.PointLight;
import dev.eternalformula.api.ecs.components.AnimationComponent;
import dev.eternalformula.api.ecs.components.MapEntityComponent;
import dev.eternalformula.api.ecs.components.PositionComponent;
import dev.eternalformula.api.ecs.components.TextureComponent;
import dev.eternalformula.api.ecs.components.physics.LightComponent;
import dev.eternalformula.api.ecs.components.physics.PhysicsComponent;
import dev.eternalformula.api.maps.CustomTiledProperty;
import dev.eternalformula.api.physics.PhysicsUtil;
import dev.eternalformula.api.scenes.SceneManager;
import dev.eternalformula.api.util.EFGFX;
import dev.eternalformula.api.world.GameWorld;

/**
 * MapEntity class
 * @since Alpha 0.0.2
 */

public class MapEntity extends Entity {
	
	private boolean isAnimated;
	
	MapEntity(boolean isAnimated) {
		super();
		
		this.isAnimated = isAnimated;
		
		PooledEngine engine = SceneManager.getInstance().getEngine();
		
		MapEntityComponent mapEntComp = engine.createComponent(MapEntityComponent.class);
		PositionComponent posComp = engine.createComponent(PositionComponent.class);
		
		Component texComp = null;
		if (isAnimated) {
			texComp = engine.createComponent(AnimationComponent.class);
		}
		else {
			texComp = engine.createComponent(TextureComponent.class);
		}
		
		add(mapEntComp);
		add(posComp);
		add(texComp);
	}
	
	/**
	 * Extracts and loads any special properties associated with the MapEntity
	 * (eg. lights, physics body, etc.)
	 * 
	 * @param gameWorld The current GameWorld (for physics and lighting purposes)
	 * @param props The MapProperties object associated with the entity.
	 */
	
	public void loadProperties(GameWorld gameWorld, MapProperties props) {
		
		PooledEngine ecsEngine = SceneManager.getInstance().getEngine();
		
		// PositionComponent - for position setting
		PositionComponent posComp = getComponent(PositionComponent.class);
		float entityX = posComp.getX();
		float entityY = posComp.getY();
		
		if (props.containsKey("light")) {
			// Light
			
			LightComponent lightComp = ecsEngine.createComponent(LightComponent.class);
			CustomTiledProperty lightProp = (CustomTiledProperty) props.get("light");
			
			if (lightProp.containsKey("color")) {
				lightComp.lightColor = (Color) lightProp.get("color");
			}
			else {
				lightComp.lightColor = Color.valueOf(LightComponent.DEFAULT_LIGHT_COLOR);
				lightComp.lightColor.a = 0.95f;
			}
			
			// localPos of light (relative to entity pos)
			Vector2 localPos = Vector2.Zero;
			if (lightProp.containsKey("localPos")) {
				localPos = (Vector2) lightProp.get("localPos");
			}
			
			float width = (float) lightProp.get("width");
			float height = (float) lightProp.get("height");
			
			localPos.x += entityX;
			localPos.y += entityY;
			
			if (lightProp.containsKey("distance")) {
				lightComp.lightDistance = (float) lightProp.get("distance");
			}
			
			if (lightProp.containsKey("maxFlickerDistance")) {
				lightComp.maxFlickerDistance = (float) lightProp.get("maxFlickerDistance");
			}
			
			if (lightProp.containsKey("maxTimeBetweenFlicker")) {
				lightComp.maxTimeBetweenFlicker = (float) lightProp.get("maxTimeBetweenFlicker");
			}
			
			if (lightProp.containsKey("shouldFlicker")) {
				lightComp.shouldFlicker = (boolean) lightProp.get("shouldFlicker");
			}
			
			lightComp.createLightBody(gameWorld, localPos, width, height);
			lightComp.createLight(gameWorld);
			
			// Add default light values.
			PointLight light = lightComp.light;
			light.setSoftnessLength(0f);
			light.setXray(true);
			// Adds light component to the entity
			add(lightComp);
		}
		
		if (props.containsKey("physicsBody")) {
			
			CustomTiledProperty physicsBody = (CustomTiledProperty) props.get("physicsBody");
			PhysicsComponent physicsComp = ecsEngine.createComponent(PhysicsComponent.class);
			
			if (physicsBody.containsKey("hitbox")) {
				Rectangle hitboxRect = (Rectangle) physicsBody.get("hitbox");
				
				// Creates hitbox body
				physicsComp.hitboxBody = PhysicsUtil.createBodyFromTiledProperty
						(gameWorld, posComp.position, hitboxRect);
			}
			
			if (physicsBody.containsKey("collider")) {
				Rectangle colliderRect = (Rectangle) physicsBody.get("collider");
				
				// Creates collider body
				Vector2 colliderOrigin = new Vector2(posComp.position);
				colliderOrigin.y = colliderOrigin.y - posComp.height / 2f + colliderRect.height / 2f;
				physicsComp.colliderBody = PhysicsUtil.createBodyFromTiledProperty
						(gameWorld, posComp.position, colliderRect);
			}
			
		}
	}
	
	public void setupTextures(TextureRegion atlasRegion, boolean isAnimated, float frameDuration) {
		if (isAnimated) {
			PositionComponent posComp = getComponent(PositionComponent.class);
			
			float entityWidth = posComp.width;
			float entityHeight = posComp.height;
			
			float regionWidth = atlasRegion.getRegionWidth() / EFGFX.PPM;
			
			int numFrames = Math.round(regionWidth / entityWidth);
			
			/*
			 * Optimization idea: Rather than creating individual animations for each MapEntity, create one if it does not exist,
			 * and then simply create copies of the same animation for each subsequent MapEntity that needs it.
			 */
			
			Array<TextureRegion> frames = new Array<TextureRegion>();
			for (int i = 0; i < numFrames; i++) {
				// This *should* work lol
				frames.add(new TextureRegion(atlasRegion, i * (atlasRegion.getRegionWidth() / numFrames),
						0, (int) (entityWidth * EFGFX.PPM), (int) (entityHeight * EFGFX.PPM)));
			}
			
			// Future (Add support for multiple animations?)
			AnimationComponent animComp = getComponent(AnimationComponent.class);
			Animation<TextureRegion> defaultAnim = new Animation<TextureRegion>(frameDuration, frames, PlayMode.LOOP);
					
			animComp.animations.put("default", defaultAnim);
			animComp.setAnimation("default");
		}
		else {
			getComponent(TextureComponent.class).setTextureRegion(atlasRegion);
		}
	}
	
	public void draw(SpriteBatch batch, float delta) {
		
		PositionComponent posComp = getComponent(PositionComponent.class);
		
		float renderX = posComp.getX();
		float renderY = posComp.getY();
		
		float width = 0f;
		float height = 0f;
		
		TextureRegion drawReg = null;
		if (isAnimated) {
			AnimationComponent animComp = getComponent(AnimationComponent.class);
			animComp.animElapsedTime += delta;
			drawReg = animComp.getCurrentAnimation().getKeyFrame(animComp.animElapsedTime);
			
			width = drawReg.getRegionWidth() / EFGFX.PPM;
			height = drawReg.getRegionHeight() / EFGFX.PPM;
		}
		else {
			TextureComponent texComp = getComponent(TextureComponent.class);
			drawReg = getComponent(TextureComponent.class).getTextureRegion();
			
			// TextureComp already handles pixels -> world units scaling on construction
			width = texComp.getWidth();
			height = texComp.getHeight();
		}
		
		batch.draw(drawReg, renderX, renderY, width, height);
	}

}
