package com.thorgaming.throwme.displayobjects.game.cloud;

import org.jbox2d.collision.Shape;
import org.jbox2d.common.Vec2;

import com.thorgaming.throwme.R;

/**
 * @author Thomas Cheyney
 * @version 1.0
 */
public class LightningCloud extends Cloud {

	public LightningCloud() {
		super(R.drawable.lightningcloud);
	}

	@Override
	public void persistContact(Shape character) {
		character.getBody().applyForce(new Vec2(8, 50), character.getBody().getPosition());
	}

	@Override
	public int getY() {
		return super.getY() - 40;
	}

}