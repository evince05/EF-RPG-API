package dev.eternalformula.api;

import dev.eternalformula.api.scenes.SceneManager;

/**
 * The EFAPI class can be considered as the main class of the EF RPG API.<br>
 * It has a wide variety of functionalities and uses that act as the backbone<br>
 * of the API. Note that {@link EFAPI#handleInit()} must always be called<br>
 * on project launch so the API can properly load.
 * 
 * @author EternalFormula
 * @since Alpha 0.0.1
 * @lastEdit
 */

public class EFAPI {
	
	public static final String API_VERSION = "Alpha 0.0.1";
	
	private static EFAPI apiInstance = null;
	
	private SceneManager sceneManager;
	
	private EFAPI() {
		apiInstance = this;
		this.sceneManager = SceneManager.createNewInstance();
	}
	
	/**
	 * Handles all initialization-related functions (app launch).
	 */
	
	public static void handleInit() {
		
		if (apiInstance == null) {
			apiInstance = new EFAPI();
		}
		else {
			
		}
	}
	
	/**
	 * Handles all exit-related functions (app termination).
	 */
	
	public static void handleExit() {
		
	}
	
	/**
	 * Gets the EFAPI instance. Note that for this to properly function,
	 * <br>{@link EFAPI#handleInit()}<br> must first be called.
	 * 
	 * @return The EFAPI instance; null if it has not yet been initialized.
	 */
	public static EFAPI getInstance() {
		return apiInstance;
	}
	
	public SceneManager getSceneManager() {
		return sceneManager;
	}

}
