package dev.eternalformula.api.util;

import com.badlogic.gdx.Gdx;

/**
 * Simple util class for debugging values.
 * 
 * @author EternalFormula
 * @since Alpha 0.0.1
 */

public class EFDebug {
	
	public static void info(String msg) {
		Gdx.app.log("INFO", msg);
	}
	
	public static void debug(String msg) {
		Gdx.app.debug("DEBUG", msg);
	}
	
	public static void warn(String msg) {
		Gdx.app.log("WARN", msg);
	}
	
	public static void error(String msg) {
		Gdx.app.log("ERROR", msg);
	}


}
