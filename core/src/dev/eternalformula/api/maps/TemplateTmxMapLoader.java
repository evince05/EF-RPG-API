/**
 * 
 */
package dev.eternalformula.api.maps;

import java.util.Comparator;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.ImageResolver;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Sort;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

import dev.eternalformula.api.ecs.components.PositionComponent;
import dev.eternalformula.api.ecs.components.TextureComponent;
import dev.eternalformula.api.ecs.entities.EntityBuilder;
import dev.eternalformula.api.ecs.entities.MapEntity;
import dev.eternalformula.api.scenes.SceneManager;
import dev.eternalformula.api.util.EFDebug;
import dev.eternalformula.api.util.EFGFX;
import dev.eternalformula.api.world.GameWorld;

/**
 * An extension of the TmxMapLoader that allows Tiled's Template Objects to be loaded.
 * @author EternalFormula
 * @since Alpha 0.0.2
 * @lastEdit Alpha 0.0.2 (02/13/2023)
 */

public class TemplateTmxMapLoader extends TmxMapLoader {

	FileHandle tmxFile;
	
	private int nonTemplateEntityCount;
	
	private Array<MapEntity> mapEntities;
	private GameWorld gameWorld;
	
	public TemplateTmxMapLoader(GameWorld gameWorld) {
		super();
		
		this.gameWorld = gameWorld;
		this.mapEntities = new Array<MapEntity>();
	}
	
    @Override
    protected TiledMap loadTiledMap (FileHandle tmxFile, TmxMapLoader.Parameters parameter, ImageResolver imageResolver){
          this.tmxFile = tmxFile;
          return super.loadTiledMap(tmxFile,parameter,imageResolver);
    }
    
	@Override
	protected void loadObject (TiledMap map, MapObjects objects, XmlReader.Element element, float heightInPixels) {
		if (element.getName().equals("object")) {

			if(!element.hasAttribute("template")) {
				nonTemplateEntityCount++;
				super.loadObject(map,objects,element,heightInPixels);
				return;
			}
			
			FileHandle template = getRelativeFileHandle(tmxFile,element.getAttribute("template"));
			Element el = xml.parse(template);
			for (Element obj : el.getChildrenByName("object")) {
					
				int id = obj.getInt("gid") - el.getChildByName("tileset").getInt("firstgid");
				FileHandle tilesetFile = getRelativeFileHandle(template, el.getChildByName("tileset").get("source"));

				Element tileset = xml.parse(tilesetFile);

				//Can probably optimize this instead of doing a linear search
				for (Element tileElement : tileset.getChildrenByName("tile")) {
					if (tileElement.getInt("id") == id) {
						String source = tileElement.getChild(0).get("source");
						FileHandle img = getRelativeFileHandle(tilesetFile, source);

						Texture tex = new Texture(Gdx.files.internal(img.path()));
						TextureRegion region = new TextureRegion(tex);
						float x = element.getFloat("x") / EFGFX.PPM;
						float y = (map.getProperties().get("height", int.class) * 16 
								- element.getFloat("y")) / EFGFX.PPM;

						// All map entities have a position and texture (animation in some cases)
						MapEntity mapEntity = (MapEntity) EntityBuilder.createEntity(MapEntity.class);
						mapEntity.getComponent(PositionComponent.class).position = new Vector2(x, y);
						mapEntity.getComponent(TextureComponent.class).setTextureRegion(region);
						
						// Custom Properties
						MapProperties objProps = getMapProperties(obj);
						
						if (objProps != null) {
							mapEntity.loadProperties(gameWorld, objProps);
						}
						mapEntities.add(mapEntity);
					}
				}
				return;
				/*
				else {
					super.loadObject(map, objects, element, heightInPixels);
					
					// Load Object Properties and pass them to modified version of loadObject().
				}*/
				
				//return;
			}
		}
	}
	
	/**
	 * Gets the mapproperties of a given element.
	 * @param e The element to be used.
	 * @return The map properties of a given element, null<br>
	 *  if the element has no properties.
	 */
	
	private MapProperties getMapProperties(Element e) {
		
		if (e != null && e.getChildByName("properties") != null) {
			MapProperties props = new MapProperties();
			
			for (Element property : e.getChildByName("properties").getChildrenByName("property")) {
				
				String name = property.getAttribute("name");
				
				if (property.hasAttribute("propertytype")) {
					// Custom Property
					EFDebug.info("CustomTiledProperty detected!");
					EFDebug.info("Found property name=" + name);
					props.put(name, CustomTiledProperty.getCustomProperty(property));
					continue;
				}
				
				String value = property.getAttribute("value");
				String type = "";
				Object castValue;
				
				if (property.hasAttribute("type")) {
					type = property.getAttribute("type");
					castValue = castProperty(name, value, type);
				}
				else {
					castValue = castProperty(name, value, null);
				}
				props.put(name, castValue);
			}
			return props;
		}
		return null;
	}
	
	public Array<MapEntity> getMapEntities() {
		return mapEntities;
	}
	
	public EFTiledMap generateEFTiledMap(String fileName) {
		TiledMap map = load(fileName);
		Array<MapEntity> mapEntities = getMapEntities();
		zSortMapEntities(mapEntities);
		
		for (Entity e : mapEntities) {
			SceneManager.getInstance().getEngine().addEntity(e);
		}
		
		if (nonTemplateEntityCount > 0) {
			EFDebug.info("Loaded " + nonTemplateEntityCount + " entities without using templates."
					+ " Use templates for better support.");
		}
		
		return new EFTiledMap(map, mapEntities);
	}
	
	private void zSortMapEntities(Array<MapEntity> mapEntities) {
		Sort.instance().sort(mapEntities, new MapEntityComparator());
	}
	
	private static class MapEntityComparator implements Comparator<MapEntity> {

		@Override
		public int compare(MapEntity obj1, MapEntity obj2) {
			float y1 = obj1.getComponent(PositionComponent.class).getY();
			float y2 = obj2.getComponent(PositionComponent.class).getY();
			
			return Float.compare(y2, y1);
		}
	}
}
