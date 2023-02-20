/**
 * 
 */
package dev.eternalformula.api.files;

import java.io.IOException;
import java.io.InputStream;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * FileUtil
 *
 * @author EternalFormula
 * @since Alpha 0.0.2
 */
public class EFFileUtil {
	
	/**
	 * Gets the root node from a JSON file in the assets folder.
	 * @param jsonFile The path of the file to be parsed (in the assets folder).
	 * @return The root node of the json file.
	 */
	
	public static JsonNode getJsonRoot(String jsonFile) {
		ObjectMapper objMapper = new ObjectMapper();
		InputStream jsonStream = Gdx.files.internal(jsonFile).read();
		try {
			return objMapper.readTree(jsonStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
