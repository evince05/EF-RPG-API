package dev.eternalformula.api.ecs.components.interfaces;

import com.badlogic.ashley.core.Component;

import dev.eternalformula.api.interfaces.Copyable;

/**
 * The EFComponent is a simple extension of the Ashley component interface.
 * In the EFComponent version, the component should be able to be copied.
 *
 * @author EternalFormula
 * @since Alpha 0.0.4
 */

public interface EFComponent extends Component, Copyable {
}
