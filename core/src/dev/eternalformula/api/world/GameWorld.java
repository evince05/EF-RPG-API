/**
 * 
 */
package dev.eternalformula.api.world;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import box2dLight.RayHandler;
import dev.eternalformula.api.maps.EFMapRenderer;
import dev.eternalformula.api.maps.EFTiledMap;
import dev.eternalformula.api.maps.TemplateTmxMapLoader;
import dev.eternalformula.api.scenes.SceneManager;
import dev.eternalformula.api.util.EFDebug;

/**
 * The GameWorld is the foundation of the interactable game environment.<br>
 * 
 * It serves as the manager for entity handling, physics interactions, lighting,
 * maps, and all that fun stuff.<br><br>
 * 
 * Note that the GameWorld and EFTiledMap both frequently refer to 'entities'.
 * In the GameWorld context, a worldEntity is an entity that interacts<br>
 * with the environment, such as a player, a mob, or a projectile.<br>
 * 
 * In the EFTiledMap context, a mapEntity is an entity that belongs to the<br>
 * environment, such as a tree, a lamppost, or a trigger.
 * 
 * @author EternalFormula
 * @since Alpha 0.0.2
 */

public class GameWorld {
	
	private World world;
	private RayHandler rayHandler;
	
	private Box2DDebugRenderer b2dr;
	
	private EFMapRenderer mapRenderer;
	private EFTiledMap levelMap;
	
	private Array<Entity> worldEntities;
	
	// These two arrays help avoid the ConcurrentModificationException
	private Array<Entity> worldEntitiesToAdd;
	private Array<Entity> worldEntitiesToRemove;
	
	/**
	 * Creates a new GameWorld.
	 */
	
	private GameWorld() {
		// Physics and light (pretty cool stuff)
		this.world = new World(Vector2.Zero, false);
		this.rayHandler = new RayHandler(world);
		
		rayHandler.setAmbientLight(0.4f);
		
		this.worldEntities = new Array<Entity>();
		this.worldEntitiesToAdd = new Array<Entity>();
		this.worldEntitiesToRemove = new Array<Entity>();
		
		this.mapRenderer = new EFMapRenderer();
		this.b2dr = new Box2DDebugRenderer();
		this.levelMap = null;
		
	}
	
	/**
	 * Creates an empty game world.
	 * @return A new GameWorld object
	 */
	
	public static GameWorld createNewWorld() {
		return new GameWorld();
	}
	
	public void loadNewMapArea(String fileName) {
		EFDebug.debug("Attempting to load map \"" + fileName + "\"!");
		EFDebug.debug("Is load performance dependent on battery being plugged in?");
		
		TemplateTmxMapLoader loader = new TemplateTmxMapLoader(this);
		this.levelMap = loader.generateEFTiledMap(fileName);
		
		mapRenderer.setMap(levelMap);
	}
	
	public void update(float delta) {
		// Game Logic
		world.step(1 / 60f, 6, 2);
		rayHandler.update();
		
		// Bottom of update method
		for (Entity e : worldEntitiesToAdd) {
			worldEntities.add(e);
		}
		
		worldEntities.removeAll(worldEntitiesToRemove, false);
		
		worldEntitiesToAdd.clear();
		worldEntitiesToRemove.clear();
	}
	
	public void draw(SpriteBatch gameBatch, float delta) {
		mapRenderer.draw(gameBatch, delta);
		
		rayHandler.setCombinedMatrix(SceneManager.getInstance().getGameCamera());
		
		gameBatch.end();
		rayHandler.render();
		
		//b2dr.render(world, SceneManager.getInstance().getGameCamera().combined);
		
		gameBatch.begin();
	}
	
	public void dispose() {
		world.dispose();
		rayHandler.dispose();
		b2dr.dispose();
		mapRenderer.dispose();
		levelMap.dispose();
	}
	
	public void pause() {
		
	}
	
	public void resume() {
		
	}
	
	public World getWorld() {
		return world;
	}
	
	public RayHandler getRayHandler() {
		return rayHandler;
	}
	
	public Array<Entity> getEntities() {
		return worldEntities;
	}
	
	public void addEntity(Entity e) {
		worldEntitiesToAdd.add(e);
	}
	
	public void removeEntity(Entity e) {
		worldEntitiesToRemove.add(e);
	}
}
