/**
 * 
 */
package dev.eternalformula.api.maps;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * CustomTemplateAttribute
 *
 * @author EternalFormula
 * @since Alpha 0.0.2
 */
public class CustomTiledProperty {
	
	private String rootPropName;
	
	private HashMap<String, Object> attrMembers;
	
	private CustomTiledProperty(Element elementRoot) {
		// key value pairs
		this.rootPropName = elementRoot.get("name");
		
		this.attrMembers= new HashMap<String, Object>();
		loadSubProperties(elementRoot);
	}
	
	/**
	 * Gets all sub properties from a property element.
	 * @param rootProperty The highest property element.
	 */
	
	private void loadSubProperties(Element rootProperty) {
		if (rootProperty.hasChild("properties")) {
			for (Element subProp : rootProperty.getChildByName("properties")
					.getChildrenByName("property")) {
				
				String key = subProp.get("name");
				
				//EFDebug.info("Key: " + key);
				String type = null;
				if (subProp.hasAttribute("type")) {
					type = subProp.get("type");
				}

				// Custom types
				if (isCustomProperty(subProp)) {
					String customType = subProp.get("propertytype");
					attrMembers.put(key, loadCustomProperty(customType, subProp));
				}
				else {
					// Tiled-supported default
					String value = subProp.get("value");
					Object castedProp = castProperty(key, value, type);
					attrMembers.put(key, castedProp);
				}
			}
		}
	}
	
	/**
	 * Loads a custom property type that is not handled by default in Tiled.
	 * @param type The property type (eg. position)
	 * @param property The root element (of the custom property)
	 * 
	 * @return The property object
	 */
	
	private Object loadCustomProperty(String type, Element property) {
		if (type.equalsIgnoreCase("position")) {
			Element propertiesElem = property.getChildByName("properties");
			
			float posX = 0f;
			float posY = 0f;
			
			for (Element prop : propertiesElem.getChildrenByName("property")) {
				if (prop.get("name").equals("x")) {
					posX = prop.getFloat("value");
				}
				else if (prop.get("name").equals("y")) {
					posY = prop.getFloat("value");
				}
			}
			
			Vector2 pos = new Vector2(posX, posY);
			return pos;
		}
		
		if (type.equalsIgnoreCase("rectangle")) {
			Element propertiesElem = property.getChildByName("properties");
			
			Vector2 centerPos = Vector2.Zero;
			float width = 0f;
			float height = 0f;
			
			for (Element prop : propertiesElem.getChildrenByName("property")) {
				
				if (prop.hasAttribute("propertytype")) {
					// This is the "centerPos" attribute
					centerPos = (Vector2) loadCustomProperty("position", prop);
				}
				else if (prop.get("name").equals("width")) {
					width = prop.getFloat("value");
				}
				else if (prop.get("name").equals("height")) {
					height = prop.getFloat("value");
				}
			}
			
			/* 
			 * Creates a new rectangle off of the given information.
			 * Note that this is not a graphical representation of a square,
			 * but is is the center point, along with the width and height.
			 */
			
			return new Rectangle(centerPos.x - width / 2f,
					centerPos.y - height / 2f, width, height);
		}
		
		return null;
	}
	
	private boolean isCustomProperty(Element property) {
		return property.hasAttribute("propertytype") && property.hasChild("properties");
	}
	
	/**
	 * Gets the custom tiled property for a given property element.
	 * @param customType The root property element.
	 * @return An object representing the custom property.
	 */
	
	public static CustomTiledProperty getCustomProperty(Element customType) {
		return new CustomTiledProperty(customType);
	}
	
	/**
	 * (Ripped from LibGDX's BaseTmxMapLoader class)<br>
	 * Casts a map property depending on the type.<br><br>
	 * 
	 * @param name The name of the key-value pair
	 * @param value The value in its string form
	 * @param type The type to cast
	 * @return The casted value.
	 */
	
	private Object castProperty(String name, String value, String type) {
		if (type == null) {
			return value;
		} else if (type.equals("int")) {
			return Integer.valueOf(value);
		} else if (type.equals("float")) {
			return Float.valueOf(value);
		} else if (type.equals("bool")) {
			return Boolean.valueOf(value);
		} else if (type.equals("color")) {
			// Tiled uses the format #AARRGGBB
			String opaqueColor = value.substring(3);
			String alpha = value.substring(1, 3);
			return Color.valueOf(opaqueColor + alpha);
		} else {
			throw new GdxRuntimeException(
				"Wrong type given for property " + name + ", given : " + type + ", supported : string, bool, int, float, color");
		}
	}
	
	public boolean containsKey(String key) {
		return attrMembers.containsKey(key);
	}
	
	public Object get(String key) {
		return attrMembers.get(key);
	}
	
	public String toString() {
		String mapInfo = attrMembers.toString();
		return "[" + rootPropName + ": " + mapInfo + "]";
	}
}
