/**
 * 
 */
package dev.eternalformula.api.ecs.entities;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import box2dLight.PointLight;
import dev.eternalformula.api.ecs.components.AnimationComponent;
import dev.eternalformula.api.ecs.components.MapEntityComponent;
import dev.eternalformula.api.ecs.components.ParticleComponent;
import dev.eternalformula.api.ecs.components.PositionComponent;
import dev.eternalformula.api.ecs.components.TextureComponent;
import dev.eternalformula.api.ecs.components.interfaces.TranslatableComponent;
import dev.eternalformula.api.ecs.components.physics.LightComponent;
import dev.eternalformula.api.ecs.components.physics.PhysicsComponent;
import dev.eternalformula.api.interfaces.Copyable;
import dev.eternalformula.api.maps.CustomTiledProperty;
import dev.eternalformula.api.physics.PhysicsUtil;
import dev.eternalformula.api.scenes.SceneManager;
import dev.eternalformula.api.util.EFGFX;
import dev.eternalformula.api.world.GameWorld;

/**
 * The MapEntity class represents the MapObjects that exist in Tiled maps.
 * @since Alpha 0.0.2
 */

public class MapEntity extends Entity {

	private boolean isClone;
	private boolean isAnimated;

	/**
	 * Empty MapEntity constructor (for copying)
	 */

	private MapEntity() {
		this.isClone = true;
	}

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

			// Deletes any previous light component (for custom props)
			if (LightComponent.MAPPER.has(this)) {
				// Previous light exists, must destroy first
				LightComponent previousLight = LightComponent.MAPPER.get(this);
				previousLight.light.remove();
			}
			
			LightComponent lightComp = ecsEngine.createComponent(LightComponent.class);
			CustomTiledProperty lightProp = (CustomTiledProperty) props.get("light");

			if (lightProp.containsKey("lightColor")) {
				lightComp.lightColor = (Color) lightProp.get("lightColor");
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
			
			if (PhysicsComponent.MAPPER.has(this)) {
				PhysicsComponent prevPhysComp = PhysicsComponent.MAPPER.get(this);
				gameWorld.getWorld().destroyBody(prevPhysComp.colliderBody);
				gameWorld.getWorld().destroyBody(prevPhysComp.hitboxBody);
			}

			CustomTiledProperty physicsBody = (CustomTiledProperty) props.get("physicsBody");
			PhysicsComponent physicsComp = ecsEngine.createComponent(PhysicsComponent.class);

			if (physicsBody.containsKey("hitbox")) {
				Rectangle hitboxRect = (Rectangle) physicsBody.get("hitbox");

				// Creates hitbox body
				physicsComp.hitboxBody = PhysicsUtil.createBodyFromTiledProperty
						(gameWorld, posComp.position, hitboxRect);
				
				physicsComp.hitboxDimensions = new Vector2(hitboxRect.width, hitboxRect.height);
			}

			if (physicsBody.containsKey("collider")) {
				Rectangle colliderRect = (Rectangle) physicsBody.get("collider");

				// Creates collider body
				Vector2 colliderOrigin = new Vector2(posComp.position);
				colliderOrigin.y = colliderOrigin.y - posComp.height / 2f + colliderRect.height / 2f;
				physicsComp.colliderBody = PhysicsUtil.createBodyFromTiledProperty
						(gameWorld, posComp.position, colliderRect);
				
				physicsComp.colliderDimensions = new Vector2(colliderRect.width, colliderRect.height);
			}
			
			add(physicsComp);

		}
		
		if (props.containsKey("particleEmitter")) {
			// TODO: Add destroy on override (for custom emitters)
			
			ParticleComponent particleComp = SceneManager.getInstance().getEngine()
					.createComponent(ParticleComponent.class);
			
			// TODO: Add support for atlases (maybe? probably?) Also, add dispose methods for EFComponents
			CustomTiledProperty particleProp = (CustomTiledProperty) props.get("particleEmitter");
			
			// Loads all values from the particle property
			float particleX = (float) particleProp.get("x");
			float particleY = (float) particleProp.get("y");
			FileHandle handle = Gdx.files.internal(String.valueOf(particleProp.get("path")));
			
			particleComp.effect = new ParticleEffect();
			particleComp.effect.load(handle, handle.parent());
			particleComp.effect.setPosition(entityX + particleX, entityY + particleY);
			particleComp.effect.start();
			
			add(particleComp);
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

	/**
	 * Copies a MapEntity to a new position in the world.
	 * @param world The GameWorld in use
	 * @param position The desired position
	 * 
	 * @return An exact copy of this MapEntity, with the position set to the desired position.
	 */

	public MapEntity copy(GameWorld world, Vector2 position) {

		MapEntity entity = new MapEntity();

		/*
		 *  All MapEntities have a PositionComponent.
		 *  Also, this is done before the loop so that
		 *  the translatable components can be done in the same loop.
		 */

		PositionComponent posComp = PositionComponent.MAPPER.get(this).copy();
		posComp.position = position;
		//posComp.position = new Vector2(position);

		entity.add(posComp);

		for (Component c : getComponents()) { // getComponents of current mapentity

			if (c.getClass().equals(PositionComponent.class)) {
				// PositionComponent gets copied outside of the loop
				continue;
			}

			// Ideally, each component should be copyable.
			if (c instanceof Copyable) {
				entity.add((Component) ((Copyable) c).copy());
			}
			else {
				// Monitor for duplicate memory addresses.
				entity.add(c);
			}
		}

		// Additional setup for individual components (if necessary)

		// Make sure to refer to `entity`, not `this` (`this` returns the template entity)
		if (LightComponent.MAPPER.has(entity)) {
			LightComponent lightComp = LightComponent.MAPPER.get(entity);

			lightComp.createLightBody(world, lightComp.lightPos, 
					lightComp.lightWidth, lightComp.lightHeight);

			lightComp.createLight(world);
		}
		
		// Translate each entity accordingly
		// Gets pos of template entity
		Vector2 oldPos = PositionComponent.MAPPER.get(this).position;

		Vector2 deltaPos = new Vector2(position.x - oldPos.x,
				position.y - oldPos.y);

		for (Component newComp : entity.getComponents()) {
			if (newComp instanceof TranslatableComponent) {
				((TranslatableComponent) newComp).translate(deltaPos);
			}
		}

		return entity;
	}

	public boolean isClone() {
		return isClone;
	}
}
