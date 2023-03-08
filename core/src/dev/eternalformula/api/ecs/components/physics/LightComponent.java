/**
 * 
 */
package dev.eternalformula.api.ecs.components.physics;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import box2dLight.PointLight;
import dev.eternalformula.api.ecs.components.UpdatableComponent;
import dev.eternalformula.api.ecs.components.interfaces.EFComponent;
import dev.eternalformula.api.ecs.components.interfaces.TranslatableComponent;
import dev.eternalformula.api.scenes.SceneManager;
import dev.eternalformula.api.world.GameWorld;

/**
 * The LightComponent is a component that attaches a light source to an entity.
 *
 * @author EternalFormula
 * @since Alpha 0.0.2
 */
public class LightComponent implements EFComponent, UpdatableComponent,
		TranslatableComponent {
	
	public static final ComponentMapper<LightComponent> MAPPER =
			ComponentMapper.getFor(LightComponent.class);
	
	public static final String DEFAULT_LIGHT_COLOR = "#f2d355";
	
	public PointLight light;
	private Body lightBody;
	
	public float lightDistance;
	
	/**
	 * The max distance from the original distance for flickering.
	 * Eg. if lightDistance is 5 and maxFlickerDistance is 0.2,
	 * the farthest possible lightDistance values would be 4.8 and 5.2
	 */
	
	public float maxFlickerDistance;
	
	// Time (in seconds) between flickering
	public float maxTimeBetweenFlicker;
	
	public float flickerTimer;
	
	public boolean shouldFlicker;
	
	public Color lightColor;
	
	// For copying purposes
	public float lightWidth;
	public float lightHeight;
	public Vector2 lightPos;
	
	public LightComponent() {
		super();
		this.light = null; 
		this.lightColor = new Color(247 / 255f, 198 / 255f, 74 / 255f, 1f);
		this.lightDistance = 1f;
		this.maxFlickerDistance = 0.2f * lightDistance;
		this.maxTimeBetweenFlicker = 0.6f;
		this.shouldFlicker = true;
		this.lightPos = Vector2.Zero;
	}

	@Override
	public void update(float delta) {
		light.update();
	}
	
	public void createLightBody(GameWorld world, Vector2 pos, float width, float height) {
		Body body;
		BodyDef bdef = new BodyDef();
		bdef.position.set(pos);
		bdef.type = BodyType.StaticBody;
		bdef.fixedRotation = true;
		
		// Copying purposes
		this.lightWidth = width;
		this.lightHeight = height;
		this.lightPos = pos;
		
		body = world.getWorld().createBody(bdef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2f, height / 2f);
		
		body.createFixture(shape, 0f);
		shape.dispose();
		this.lightBody = body;
	}
	
	public void createLight(GameWorld world) {
		this.light = new PointLight(world.getRayHandler(), 
				150, lightColor, lightDistance, 0, 0);
		light.setSoftnessLength(0f);
		light.setXray(true);
		light.attachToBody(lightBody);
	}

	@Override
	public LightComponent copy() {
		LightComponent lightComp = SceneManager.getInstance().getEngine()
				.createComponent(LightComponent.class);
		
		lightComp.lightColor = new Color(lightColor);
		lightComp.lightDistance = lightDistance;
		lightComp.maxFlickerDistance = maxFlickerDistance;
		lightComp.maxTimeBetweenFlicker = maxTimeBetweenFlicker;
		lightComp.shouldFlicker = shouldFlicker;
		
		lightComp.lightWidth = lightWidth;
		lightComp.lightHeight = lightHeight;
		lightComp.lightPos = new Vector2(lightPos);
		
		return lightComp;
	}

	@Override
	public void translate(Vector2 deltaPos) {
		lightPos.x += deltaPos.x;
		lightPos.y += deltaPos.y;
		
		light.getBody().setTransform(lightPos, 0f);
	}
}
