/**
 * 
 */
package dev.eternalformula.api.util;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;

/**
 * The amazing assets class.
 * Provides a direct way to load and interact with assets (maps, atlases, etc.).
 * 
 * @since Alpha 0.0.2
 */

public class Assets {
	
	private AssetManager assMan; // lol assman :)
	
	// Private so INSTANCE variable is not accessible. just makes it nicer for formatting
	private static final Assets INSTANCE = new Assets();
	
	private Assets() {
		this.assMan = new AssetManager();
	}
	
	public void loadDefaultAssets() {
		// Loads any EF-specific assets (used across ALL projects)
	}
	
	/**
	 * Load an asset (letting the assMan do its thaaaang)
	 * @param <T>
	 * @param fileName
	 * @param type
	 */
	public static <T> void load(String fileName, Class<T> type) {
		INSTANCE.assMan.load(fileName, type);
	}
	
	/**
	 * Sets a custom loader for a certain class.
	 * @param <T> The return type of the loaded asset
	 * @param <P> Loading parameters
	 * @param type The class type
	 * @param loader The custom loader 
	 */
	
	public static <T, P extends AssetLoaderParameters<T>> void setLoader
			(Class<T> type, AssetLoader<T, P> loader) {
		INSTANCE.assMan.setLoader(type, loader);
	}
	
	public static void updateINSTANCE() {
		INSTANCE.update();
	}
	
	public void update() {
		while (!INSTANCE.assMan.update()) { 
			EFDebug.info("[AssMan] Loading assets (" + INSTANCE.assMan.getProgress() * 100f + "%)!");
		}
		EFDebug.info("[AssMan] Asset Manager has finished loading!");
	}
	
	public void dispose() {
		assMan.dispose();
	}
	
	public static <T> T get(String fileName, Class<T> type) {
		return INSTANCE.assMan.get(fileName, type);
	}

}
