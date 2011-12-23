package com.thorgaming.throwme.displayobjects;

import org.jbox2d.collision.Shape;

public interface Sensor {

	public void hit(Shape otherShape);

	public void persistContact(Shape otherShape);

}