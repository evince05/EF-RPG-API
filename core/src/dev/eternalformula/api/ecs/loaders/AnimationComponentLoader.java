package dev.eternalformula.api.ecs.loaders;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.fasterxml.jackson.databind.JsonNode;

import dev.eternalformula.api.ecs.components.AnimationComponent;
import dev.eternalformula.api.files.EFFileUtil;
import dev.eternalformula.api.util.Assets;
import dev.eternalformula.api.util.EFDebug;
import dev.eternalformula.api.util.EFGFX;

/**
 * The AnimationComponentLoader util class provides an easy way to
 * 		load the Animations of an AnimationComponent.
 *
 * @author EternalFormula
 * @since Alpha 0.0.5
 */

public class AnimationComponentLoader {

	/**
	 * Fills the animations map of an AnimationComponent from the file at the specified path.
	 * @param animComp The AnimationComponent to fill
	 * @param animDataPath The path of the .animdata file (or .ad)
	 */

	public static void loadAnimations(AnimationComponent animComp, String animDataPath) {
		JsonNode rootNode = EFFileUtil.getJsonRoot(animDataPath);

		String atlasPath = rootNode.get("atlasPath").textValue();

		FileHandle atlasFile = EFFileUtil.getRelativeFileHandle(Gdx.files.internal(animDataPath), atlasPath);

		if (atlasPath == null || atlasFile == null) {
			EFDebug.warn("Could not load AnimComp from \"" + animDataPath + "\": Atlas not found!");
			return;
		}

		if (!Assets.isLoaded(atlasFile.path(), TextureAtlas.class)) {
			Assets.quickLoad(atlasFile.path(), TextureAtlas.class);
		}

		// Load atlas
		TextureAtlas atlas = Assets.get(atlasFile.path(), TextureAtlas.class);

		if (rootNode.has("animations")) {
			Map<String, Animation<TextureRegion>> anims =
					new HashMap<String, Animation<TextureRegion>>();

			Array<TextureRegion> frames = new Array<TextureRegion>();

			rootNode.get("animations").fields().forEachRemaining(e -> {
				
				JsonNode animNode = e.getValue();
				// Animation data
				String animName = animNode.get("name").textValue();

				int numFrames = animNode.get("frames").intValue();

				float frameWidth = (float) animNode.get("frameWidth").asDouble();
				float frameHeight = (float) animNode.get("frameHeight").asDouble();
				float frameTime = (float) animNode.get("frameTime").asDouble();

				boolean loop = animNode.get("loop").booleanValue();
				boolean createIdleAnim = animNode.get("hasIdle").booleanValue();

				for (int i = 0; i < numFrames; i++) {
					TextureRegion texReg = new TextureRegion(atlas.findRegion(animName),
							(int) frameWidth * i, 0, (int) frameWidth, (int) frameHeight);
					
					// Doesnt work for some reason
					//texReg.setRegionWidth((int) (frameWidth / EFGFX.PPM));
					//texReg.setRegionHeight((int) (frameHeight / EFGFX.PPM));
					frames.add(texReg);
				}
				
				EFDebug.info("Loaded " + numFrames + " frames for animation " + animName + " [fT=" + frameTime + "]");

				PlayMode animMode = loop ? PlayMode.LOOP : PlayMode.NORMAL;
				Animation<TextureRegion> anim = new Animation<TextureRegion>(frameTime, frames, animMode);
				anims.put(animName, anim);

				/*
				 *  Idle animation creation (Only do if no "idle" anim is specified (eg. player bouncing / blinking)
				 *  Creates a single animation from the first frame of said animation.
				 */

				if (createIdleAnim) {
					Animation<TextureRegion> idleAnim = new Animation<TextureRegion>(0f, frames.get(0));

					// Names are usually split like "name-direction", so 'name' can easily be replaced with 'idle'.

					String[] animNameArgs = animName.split("-");
					String idleName = "idle";

					if (animNameArgs.length == 0) {
						// Case for non-accpeted inputs. Try not to do this lol
						idleName += "-" + animName;
					}
					else {
						idleName = "idle-" + animName.split("-")[1];
					}

					anims.put(idleName, idleAnim);
				}

				frames.clear();
			});
			
			// Adds the map to the animcomp
			animComp.animations = anims;
		}
	}
}
