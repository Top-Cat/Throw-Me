package com.thorgaming.throwme.displayobjects;

import org.jbox2d.collision.Shape;

/**
 * Sensors can be called by the hit listener on physics events
 * 
 * @author Thomas Cheyney
 * @version 1.0
 */
public interface Sensor {

	/**
	 * Called when the character first interacts with this body
	 * 
	 * @param otherShape The character
	 */
	public void hit(Shape otherShape);

	/**
	 * Called during resolves when the character is still intersecting this body
	 * 
	 * @param otherShape The character
	 */
	public void persistContact(Shape otherShape);

}