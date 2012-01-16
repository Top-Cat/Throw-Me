package com.thorgaming.throwme.displayobjects.game.characters;

import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.thorgaming.throwme.Camera;
import com.thorgaming.throwme.R;
import com.thorgaming.throwme.Stage;
import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.displayobjects.game.Character;

public class DSquare extends Character {

	private World world;
	private Body bodyBody;
	private Body bodyBody2;
	private Drawable drawableBalloons;
	
	public DSquare() {
		super();
		drawableBalloons = ThrowMe.stage.getResources().getDrawable(R.drawable.balloon);
		
		world = ThrowMe.stage.world;

		PolygonDef head = new PolygonDef();
		head.setAsBox(40 / Stage.ratio, 40 / Stage.ratio);
		head.filter.groupIndex = -1;
		head.density = (float) 0.16;
		head.friction = (float) 0.5;
		head.restitution = (float) 0.6;
		head.userData = this;

		BodyDef headBodyDef = new BodyDef();
		headBodyDef.position.set(new Vec2(0, 0));

		bodyBody = world.createBody(headBodyDef);
		bodyBody.createShape(head);
		bodyBody.setMassFromShapes();
		bodyBody.m_angularDamping = 0.7F;
		
		headBodyDef.angle = 0.3926990f;
		
		bodyBody2 = world.createBody(headBodyDef);
		bodyBody2.createShape(head);
		bodyBody2.setMassFromShapes();
		bodyBody2.m_angularDamping = 0.7F;
		
		//TODO: Joint
	}

	@Override
	public void move(int x, int y) {
		bodyBody.setXForm(new Vec2(x / Stage.ratio, y / Stage.ratio), 0);
	}

	@Override
	public void destroy() {
		super.destroy();
		world.destroyBody(bodyBody);
	}

	@Override
	public void draw(Canvas canvas, Camera camera) {
		super.draw(canvas, camera);
		
		Vec2 p = getMainBody().getPosition();
		
		int actualX = camera.transformRelativeX((int) (p.x * Stage.ratio));
		int actualY = camera.transformRelativeY((int) (p.y * Stage.ratio));
		
		canvas.save();
		canvas.rotate((float) Math.toDegrees(bodyBody.getAngle()), actualX, actualY);
		canvas.drawRect(actualX - 20, actualY - 20, actualX + 20, actualY + 20, paint);
		canvas.restore();
		
		if (getBoost()) {
			drawableBalloons.setBounds(actualX - 19, actualY - 108, actualX + 15, actualY + 2);
			
			canvas.save();
			canvas.rotate((float) (Math.sin((float) balloonBar / 20 - 4.1887902) * 25), actualX, actualY);
			drawableBalloons.draw(canvas);
			canvas.restore();
			canvas.save();
			canvas.rotate((float) (Math.sin((float) balloonBar / 20) * 25), actualX, actualY);
			drawableBalloons.draw(canvas);
			canvas.restore();
			canvas.save();
			canvas.rotate((float) (Math.sin((float) balloonBar / 20 - 2.0943951) * 25), actualX, actualY);
			drawableBalloons.draw(canvas);
			canvas.restore();
			
			applyImpulse(new Vec2(0.02F, -0.13F));
		}
	}

	@Override
	public boolean checkPress(int x, int y) {
		return true;
	}

	@Override
	public Body getMainBody() {
		return bodyBody;
	}

	@Override
	public void applyImpulse(Vec2 impulse) {
		getMainBody().applyImpulse(impulse, getMainBody().getPosition().add(new Vec2((float) (40 * Math.sin(getMainBody().getAngle())) / Stage.ratio, (float) (40 * Math.cos(getMainBody().getAngle())) / Stage.ratio)));
	}

}