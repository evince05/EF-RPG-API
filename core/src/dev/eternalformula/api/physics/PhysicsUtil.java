/**
 * 
 */
package dev.eternalformula.api.physics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import dev.eternalformula.api.world.GameWorld;

/**
 * PhysicsUtil
 *
 * @author EternalFormula
 * @since Alpha 0.0.3
 */
public class PhysicsUtil {
	
	/**
	 * Creates a basic static body.
	 * @param world An instance of the Box2D world.
	 * @param centerX The x location of the center of the body (world units)
	 * @param centerY The y location of the center of the body (world units)
	 * @param width The width of the body (world units)
	 * @param height The height of the body (world units)
	 * @param bodyType The bodytype of the body.
	 * @param category The PhysicsCategory of the body.
	 * @param userData The user data of the body's fixture.
	 */
	
	public static Body createBody(World world, float centerX, float centerY,
			float width, float height, BodyType bodyType,
			Object userData) {

		Body body;
		BodyDef def = new BodyDef();
		def.type = bodyType;
		def.position.set(centerX, centerY);
		def.fixedRotation = true;
		body = world.createBody(def);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2f, height / 2f);
		
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = 1.0f;
		/*
		fdef.filter.categoryBits = category.getCBits();
		fdef.filter.maskBits = category.getMBits();
		fdef.isSensor = category.isSensor();
		*/
		body.createFixture(fdef);
		body.setUserData(userData);
		shape.dispose();
		return body;
	}
	
	public static Body createBodyFromTiledProperty(GameWorld world, Vector2 pos, Rectangle bodyRect) {
		return createBody(world.getWorld(), pos.x + bodyRect.x + bodyRect.width / 2f, 
				pos.y + bodyRect.y + bodyRect.height / 2f, bodyRect.width, bodyRect.height,
				BodyType.StaticBody, null);
		
	}

}
