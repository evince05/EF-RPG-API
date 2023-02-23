/**
 * 
 */
package dev.eternalformula.api.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Vector2;

/**
 * PositionComponent {@value dev.eternalformula.api.EFAPI#API_VERSION}
 * @author EternalFormula @value dev.eternalformula.api.EFAPI#API_VERSION}
 * @since {@value dev.eternalformula.api.EFAPI#API_VERSION}
 * @lastEdit {@value dev.eternalformula.api.EFAPI#API_VERSION} (02/16/2023)
 */
public class PositionComponent implements Component {

	public static final ComponentMapper<PositionComponent> Map =
			ComponentMapper.getFor(PositionComponent.class);

	public Vector2 position;
	public float width;
	public float height;

	public PositionComponent() {
		this.position = new Vector2(0f, 0f);
	}

	public Vector2 getPosition() {
		return position;
	}

	public float getX() {
		return position.x;
	}

	public void setX(float x) {
		position.x = x;
	}

	public float getY() {
		return position.y;
	}

	public void setY(float y) {
		position.y = y;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public void setPosition(float x, float y) {
		this.position = new Vector2(x, y);
	}
	
	public float getWidth() {
		return width;
	}
	
	public void setWidth(float width) {
		this.width = width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public void setHeight(float height) {
		this.height = height;
	}
}
