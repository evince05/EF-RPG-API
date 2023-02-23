/**
 * 
 */
package dev.eternalformula.api.ecs.components.physics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import box2dLight.PointLight;
import dev.eternalformula.api.ecs.components.UpdatableComponent;
import dev.eternalformula.api.world.GameWorld;

/**
 * The LightComponent is a component that attaches a light source to an entity.
 *
 * @author EternalFormula
 * @since Alpha 0.0.2
 */
public class LightComponent implements UpdatableComponent {
	
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
	
	public LightComponent() {
		super();
		this.light = null; 
		this.lightColor = new Color(247 / 255f, 198 / 255f, 74 / 255f, 1f);
		this.lightDistance = 1f;
		this.maxFlickerDistance = 0.2f * lightDistance;
		this.maxTimeBetweenFlicker = 0.6f;
		this.shouldFlicker = true;
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}
	
	public void createLightBody(GameWorld world, Vector2 pos, float width, float height) {
		Body body;
		BodyDef bdef = new BodyDef();
		bdef.position.set(pos);
		bdef.type = BodyType.StaticBody;
		bdef.fixedRotation = true;
		
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

}
