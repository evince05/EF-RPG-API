package dev.eternalformula.api.interfaces;

/**
 * The Copyable interface provides a way to copy objects,
 * creating a new instance of their inner variables rather than
 * copying their memory addresses.
 *
 * @author EternalFormula
 * @since Alpha 0.0.4
 */

public interface Copyable {
	
	/**
	 * Returns a copy of the object. However, unlike the Cloneable interface,
	 * this object creates new instances of inner objects, rather than cloning 
	 * their memory address.
	 * 
	 * @return A copy of the object
	 */
	public Object copy();
}
