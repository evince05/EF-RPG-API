package dev.eternalformula.examples.scenes;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.api.ecs.components.nav.PathfindingComponent;
import dev.eternalformula.api.ecs.systems.gfx.CameraSystem;
import dev.eternalformula.api.input.InputListener;
import dev.eternalformula.api.pathfinding.Path;
import dev.eternalformula.api.scenes.Scene;
import dev.eternalformula.api.scenes.SceneManager;
import dev.eternalformula.api.ui.UIContainer;
import dev.eternalformula.api.ui.UIInputHandler;
import dev.eternalformula.api.util.EFDebug;
import dev.eternalformula.api.world.GameWorld;
import dev.eternalformula.examples.ecs.entities.Glorb;
import dev.eternalformula.examples.ecs.entities.Player;
import dev.eternalformula.examples.input.PlayerInputHandler;

/**
 * GameScene demo class
 * 
 * @author EternalFormula
 * @since Alpha 0.0.1
 */

public class GameScene extends Scene {
	
	private GameWorld world;
	
	// UI Stuff
	private UIContainer uiLayout;
	private UIInputHandler uiInHand;
	
	private OrthographicCamera camera;
	public Vector2 cameraPos;
	
	private Player player;
	private Glorb glorb;
	
	public GameScene() {
		super();
		this.world = GameWorld.createNewWorld();
		
		long start = System.currentTimeMillis();
		world.loadNewMapArea("examples/maps/forestMap/forestMap.tmx");
		long end = System.currentTimeMillis();
		
		EFDebug.info("Loaded map area in " + ((end - start) / 1000D) + "s");
		
		this.camera = SceneManager.getInstance().getGameCamera();
		
		this.cameraPos = new Vector2(10, 8);
		camera.position.set(cameraPos, 0f);
		
		this.uiLayout = new UIContainer();
		this.uiInHand = new UIInputHandler();
		
		// UI Input
		uiInHand.attachTo(uiLayout);
		InputListener.getInstance().addInputHandler(uiInHand);
		
		this.player = Player.createPlayer();
		world.addEntity(player);
		
		this.glorb = new Glorb();
		glorb.setTarget(player);
		world.addEntity(glorb);
		
		// Post-player creation
		handlePostPlayerCreation();
		
		// ECS Configuration
		configureECS();
	}
	
	private void handlePostPlayerCreation() {
		PlayerInputHandler pIn = new PlayerInputHandler(player);
		//InputListener.getInstance().addInputHandler(pIn);
		
		PathfindingComponent pfComp = PathfindingComponent.MAPPER.get(player);
		pfComp.setPath(Path.findPath(world, player.getPosition(), new Vector2(4, 17)));
	}
	
	private void configureECS() {
		PooledEngine ecsEngine = SceneManager.getInstance().getEngine();
		
		CameraSystem camSystem = ecsEngine.getSystem(CameraSystem.class);
		camSystem.setFocusedEntity(glorb);
	}

	@Override
	public void update(float delta) {	
		world.update(delta);
	}

	@Override
	public void draw(SpriteBatch batch, float delta) {
		batch.begin();
		
		world.draw(batch, delta);
		
		batch.end();
		
		glorb.getComponent(PathfindingComponent.class).getPath().draw(batch, delta);
		
	}

	@Override
	public void drawUI(SpriteBatch uiBatch, float delta) {
		uiBatch.begin();
		uiLayout.draw(uiBatch, delta);
		uiBatch.end();
	}
	
	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}
	
	@Override
	public void dispose() {
		world.dispose();
	}

}