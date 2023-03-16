/**
 * 
 */
package dev.eternalformula.api.files;

import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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
	
	/**
	 * Copied from LibGDX's BaseTmxMapLoader.
	 * A very beautiful method.
	 */
	
	public static FileHandle getRelativeFileHandle (FileHandle file, String path) {
		StringTokenizer tokenizer = new StringTokenizer(path, "\\/");
		FileHandle result = file.parent();
		while (tokenizer.hasMoreElements()) {
			String token = tokenizer.nextToken();
			if (token.equals(".."))
				result = result.parent();
			else {
				result = result.child(token);
			}
		}
		return result;
	}

}
