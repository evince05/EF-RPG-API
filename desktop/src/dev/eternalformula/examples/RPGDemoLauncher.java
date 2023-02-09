package dev.eternalformula.examples;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import dev.eternalformula.api.util.EFGFX;

public class RPGDemoLauncher {

	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setWindowedMode(EFGFX.DEFAULT_WIDTH, EFGFX.DEFAULT_HEIGHT);
		config.setTitle("EF RPG API Demo Game");
		new Lwjgl3Application(new RPGDemoGame(), config);
	}
}
