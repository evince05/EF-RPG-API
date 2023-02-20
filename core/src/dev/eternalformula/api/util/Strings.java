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
 * @since {current-version}
 */
public class Strings {
	
	public static String vec2(Vector2 vec) {
		return "[" + vec.x + ", " + vec.y + "]";
	}
	
	public static String vec3(Vector3 vec) {
		return "[" + vec.x + ", " + vec.y + ", " + vec.z + "]";
	}

}
