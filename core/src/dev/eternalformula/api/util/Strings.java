/**
 * 
 */
package dev.eternalformula.api.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Strings
 *
 * @author EternalFormula
 * @since Alpha 0.0.1 (or 0.0.2)
 */
public class Strings {
	
	public static String vec2(Vector2 vec) {
		return "[" + vec.x + ", " + vec.y + "]";
	}
	
	public static String vec2(float x, float y) {
		return "[" + x + ", " + y + "]";
	}
	
	public static String vec3(Vector3 vec) {
		return "[" + vec.x + ", " + vec.y + ", " + vec.z + "]";
	}
	
	public static String vec3(float x, float y, float z) {
		return "[" + x + ", " + y + ", " + z + "]";
	}

}
