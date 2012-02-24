package com.thorgaming.throwme.drawing;

import org.jbox2d.collision.Shape;
import org.jbox2d.dynamics.ContactListener;
import org.jbox2d.dynamics.contacts.ContactPoint;
import org.jbox2d.dynamics.contacts.ContactResult;

import com.thorgaming.throwme.displayobjects.Sensor;
import com.thorgaming.throwme.displayobjects.game.characters.Character;

/**
 * @author Thomas Cheyney
 * @version 1.0
 */
public class HitListener implements ContactListener {

	/**
	 * New collision	
	 */
	@Override
	public void add(ContactPoint arg0) {
		Shape i = arg0.shape1;
		Shape j = arg0.shape2;
		if (i.getUserData() instanceof Sensor) {
			Shape t = null;
			t = i;
			i = j;
			j = t;
		}
		if (i.getUserData() instanceof Character && j.getUserData() instanceof Sensor) {
			((Sensor) j.getUserData()).hit(i);
		}
	}

	/**
	 * Past collision that still has intersecting shapes
	 * Called before the solver has started
	 */
	@Override
	public void persist(ContactPoint arg0) {
		Shape i = arg0.shape1;
		Shape j = arg0.shape2;
		if (i.getUserData() instanceof Sensor) {
			Shape t = null;
			t = i;
			i = j;
			j = t;
		}
		if (i.getUserData() instanceof Character && j.getUserData() instanceof Sensor) {
			((Sensor) j.getUserData()).persistContact(i);
		}
	}

	/**
	 * Collision finished, no longer touching
	 */
	@Override
	public void remove(ContactPoint arg0) {

	}

	/**
	 * Past collision that still has intersecting shapes
	 * Called after the solver has finished
	 */
	@Override
	public void result(ContactResult arg0) {

	}

}