package thorgaming.throwme;

import org.jbox2d.collision.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.ContactListener;
import org.jbox2d.dynamics.contacts.ContactPoint;
import org.jbox2d.dynamics.contacts.ContactResult;

import thorgaming.throwme.displayobjects.Character;
import thorgaming.throwme.displayobjects.Cloud;

public class HitListener implements ContactListener {

	@Override
	public void add(ContactPoint arg0) {
		Object i = arg0.shape1.getUserData();
		Object j = arg0.shape2.getUserData();
		Object t = null;
		if (i instanceof Cloud) {
			t = i;
			i = j;
			j = t;
		}
		if (i instanceof Character && j instanceof Cloud) {
			((Cloud) j).animate();
		}
	}

	@Override
	public void persist(ContactPoint arg0) {
		Shape i = arg0.shape1;
		Shape j = arg0.shape2;
		Shape t = null;
		if (i.getUserData() instanceof Cloud) {
			t = i;
			i = j;
			j = t;
		}
		if (i.getUserData() instanceof Character && j.getUserData() instanceof Cloud) {
			i.getBody().applyForce(new Vec2(3, -10), i.getBody().getWorldCenter());
		}
	}

	@Override
	public void remove(ContactPoint arg0) {
		
	}

	@Override
	public void result(ContactResult arg0) {
		
	}
	
}