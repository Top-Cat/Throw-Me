package com.thorgaming.throwme.displayobjects.cloud;

import org.jbox2d.collision.Shape;
import org.jbox2d.common.Vec2;

import com.thorgaming.throwme.R;

public class LightningCloud extends Cloud {
	
	public LightningCloud() {
		super(R.drawable.lightningcloud);
	}
	
	@Override
	public void persistContact(Shape character) {
		character.getBody().applyForce(new Vec2(8, 20), character.getBody().getWorldCenter());
	}
	
}