package dev.eternalformula.api.ecs.components.nav;

import com.badlogic.ashley.core.ComponentMapper;

import dev.eternalformula.api.ecs.components.interfaces.EFComponent;
import dev.eternalformula.api.pathfinding.Path;
import dev.eternalformula.api.pathfinding.PathNode;

/**
 * PathfindingComponent
 *
 * @author EternalFormula
 * @since Alpha 0.0.5
 */
public class PathfindingComponent implements EFComponent {

	public static final ComponentMapper<PathfindingComponent> MAPPER =
			ComponentMapper.getFor(PathfindingComponent.class);
	
	private Path path;
	
	public PathNode targetNode;
	
	public boolean hasReachedFinalNode;
	
	public Path getPath() {
		return path;
	}
	
	public void setPath(Path path) {
		this.path = path;
		this.targetNode = path.extractNextNode();
		this.hasReachedFinalNode = false;
	}
	@Override
	public PathfindingComponent copy() {
		return null;
	}

}
