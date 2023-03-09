package dev.eternalformula.examples.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.api.input.InputListener;
import dev.eternalformula.api.pathfinding.Path;
import dev.eternalformula.api.scenes.Scene;
import dev.eternalformula.api.scenes.SceneManager;
import dev.eternalformula.api.ui.UIContainer;
import dev.eternalformula.api.ui.UIInputHandler;
import dev.eternalformula.api.ui.actions.ButtonClickAction;
import dev.eternalformula.api.ui.elements.EFButton;
import dev.eternalformula.api.ui.elements.EFLabel;
import dev.eternalformula.api.util.EFDebug;
import dev.eternalformula.api.world.GameWorld;
import dev.eternalformula.examples.input.GameInputHandler;

/**
 * GameScene demo class
 * 
 * @author EternalFormula
 * @since Alpha 0.0.1
 */

public class GameScene extends Scene {
	
	private GameInputHandler inputHandler;
	private GameWorld world;
	
	// UI Stuff
	private UIContainer uiLayout;
	private UIInputHandler uiInHand;
	
	private OrthographicCamera camera;
	public Vector2 cameraPos;
	
	private Path path;
	
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
		this.inputHandler = new GameInputHandler(this);
		InputListener.getInstance().addInputHandler(inputHandler);
		
		this.uiLayout = new UIContainer();
		this.uiInHand = new UIInputHandler();
		
		// Adds UI
		EFLabel testLabel = new EFLabel("Test :)");
		testLabel.setPosition(10, 30);
		uiLayout.addChild(testLabel);
		
		EFButton testBtn = new EFButton();
		testBtn.setSkin(new TextureRegion(new Texture(Gdx.files.internal("badlogic.jpg")), 16, 16));
		testBtn.setOnClick(new ButtonClickAction() {

			@Override
			public void onClick() {
				EFDebug.info("Click!");
			}
		});
		uiLayout.addChild(testBtn);
		
		// UI Input
		uiInHand.attachTo(uiLayout);
		InputListener.getInstance().addInputHandler(uiInHand);
		
		this.path = Path.findPath(world, new Vector2(20, 4), new Vector2(4, 17));
	}

	@Override
	public void update(float delta) {	
		world.update(delta);
		
		camera.position.set(cameraPos, 0f);
		camera.update();
	}

	@Override
	public void draw(SpriteBatch batch, float delta) {
		batch.begin();
		
		world.draw(batch, delta);
		
		batch.end();
		
		path.draw(batch, delta);
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