/**
 * 
 */
package dev.eternalformula.api.ecs.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.api.ecs.components.MapEntityComponent;
import dev.eternalformula.api.ecs.components.PositionComponent;
import dev.eternalformula.api.ecs.components.TextureComponent;
import dev.eternalformula.api.ecs.components.physics.LightComponent;
import dev.eternalformula.api.ecs.components.physics.PhysicsComponent;
import dev.eternalformula.api.maps.CustomTiledProperty;
import dev.eternalformula.api.physics.PhysicsUtil;
import dev.eternalformula.api.scenes.SceneManager;
import dev.eternalformula.api.util.EFDebug;
import dev.eternalformula.api.util.Strings;
import dev.eternalformula.api.world.GameWorld;

/**
 * MapEntity class
 * 
 * @since Alpha 0.0.2
 */

public class MapEntity extends Entity {
	
	MapEntity() {
		super();
		
		PooledEngine engine = SceneManager.getInstance().getEngine();
		
		MapEntityComponent mapEntComp = engine.createComponent(MapEntityComponent.class);
		PositionComponent posComp = engine.createComponent(PositionComponent.class);
		TextureComponent texComp = engine.createComponent(TextureComponent.class);
		
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
			EFDebug.info("Attemtping to create light component!");
			
			LightComponent lightComp = ecsEngine.createComponent(LightComponent.class);
			CustomTiledProperty lightProp = (CustomTiledProperty) props.get("light");
			
			if (lightProp.containsKey("color")) {
				lightComp.lightColor = (Color) lightProp.get("color");
			}
			else {
				lightComp.lightColor = Color.valueOf(LightComponent.DEFAULT_LIGHT_COLOR);
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
			
			EFDebug.info("Creating light at " + Strings.vec2(localPos));
			lightComp.createLightBody(gameWorld, localPos, width, height);
			lightComp.createLight(gameWorld);
			
			// Adds light component to the entity
			add(lightComp);
		}
		
		if (props.containsKey("physicsBody")) {
			EFDebug.info("Found physics body!");
			
			CustomTiledProperty physicsBody = (CustomTiledProperty) props.get("physicsBody");
			PhysicsComponent physicsComp = ecsEngine.createComponent(PhysicsComponent.class);
			EFDebug.info("Current Prop: " + physicsBody.toString());
			
			if (physicsBody.containsKey("hitbox")) {
				Rectangle hitboxRect = (Rectangle) physicsBody.get("hitbox");
				
				// Creates hitbox body
				physicsComp.hitboxBody = PhysicsUtil.createBodyFromTiledProperty
						(gameWorld, posComp.position, hitboxRect);
			}
			
			if (physicsBody.containsKey("collider")) {
				Rectangle colliderRect = (Rectangle) physicsBody.get("collider");
				
				// Creates collider body
				physicsComp.colliderBody = PhysicsUtil.createBodyFromTiledProperty
						(gameWorld, posComp.position, colliderRect);
			}
			
		}
	}
	
	public void draw(SpriteBatch batch, float delta) {
		
		PositionComponent posComp = getComponent(PositionComponent.class);
		TextureComponent texComp = getComponent(TextureComponent.class);
		
		float renderX = posComp.getX();
		float renderY = posComp.getY();
		
		float width = texComp.getWidth();
		float height = texComp.getHeight();
		
		batch.draw(texComp.getTextureRegion(), renderX, renderY, width, height);
	}

}
