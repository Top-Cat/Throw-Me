package com.thorgaming.throwme.displayobjects.game.characters;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.thorgaming.throwme.R;
import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.drawing.Camera;
import com.thorgaming.throwme.drawing.Stage;

public class Circle extends Character {

	private World world;
	private Body bodyBody;
	private Drawable drawableBalloons;

	public Circle() {
		super();
		drawableBalloons = ThrowMe.getInstance().stage.getResources().getDrawable(R.drawable.balloon);

		world = ThrowMe.getInstance().stage.world;

		CircleDef head = new CircleDef();
		head.radius = 40 / Stage.ratio;
		head.filter.groupIndex = -1;
		head.density = (float) 0.2;
		head.friction = (float) 0.5;
		head.restitution = (float) 0.6;
		head.userData = this;

		BodyDef headBodyDef = new BodyDef();
		headBodyDef.position.set(new Vec2(0, 0));

		bodyBody = world.createBody(headBodyDef);
		bodyBody.createShape(head);
		bodyBody.setMassFromShapes();
		bodyBody.m_angularDamping = 0.7F;
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
		canvas.drawCircle(camera.transformX((int) (p.x * Stage.ratio - camera.getX())), camera.transformY((int) (p.y * Stage.ratio + camera.getY())), 40, paint);

		if (getBoost()) {
			int actualX = camera.transformRelativeX((int) (p.x * Stage.ratio + 40 * Math.sin(getMainBody().getAngle())));
			int actualY = camera.transformRelativeY((int) (p.y * Stage.ratio + 40 * Math.cos(3.141592f + getMainBody().getAngle())));

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
	public Body getMainBody() {
		return bodyBody;
	}

	@Override
	public void applyImpulse(Vec2 impulse) {
		getMainBody().applyImpulse(impulse, getMainBody().getPosition().add(new Vec2((float) (40 * Math.sin(getMainBody().getAngle())) / Stage.ratio, (float) (40 * Math.cos(getMainBody().getAngle())) / Stage.ratio)));
	}

}