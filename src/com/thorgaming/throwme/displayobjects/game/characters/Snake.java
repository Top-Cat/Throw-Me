package com.thorgaming.throwme.displayobjects.game.characters;

import java.util.HashMap;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.thorgaming.throwme.R;
import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.drawing.Camera;
import com.thorgaming.throwme.drawing.Stage;

public class Snake extends Character {

	private World world;
	private Body bodyHead;
	private Body bodySeg1;
	private Body bodySeg2;
	private Body bodySeg3;
	private Body bodySeg4;
	private Drawable drawableEye;
	private Drawable drawableBalloons;

	private Joint joint1;
	private Joint joint2;
	private Joint joint3;
	private Joint joint4;

	private HashMap<Body, Integer> bodies = new HashMap<Body, Integer>();

	public Snake() {
		super();
		drawableEye = ThrowMe.getInstance().stage.getResources().getDrawable(R.drawable.eye);
		drawableBalloons = ThrowMe.getInstance().stage.getResources().getDrawable(R.drawable.balloon);

		world = ThrowMe.getInstance().stage.world;

		CircleDef head = new CircleDef();
		head.radius = 20 / Stage.ratio;
		//head.filter.groupIndex = -1;
		head.density = (float) 0.25;
		head.friction = (float) 0.5;
		head.restitution = (float) 0.6;
		head.userData = this;

		BodyDef headBodyDef = new BodyDef();
		headBodyDef.position.set(new Vec2(0, 0));

		bodyHead = world.createBody(headBodyDef);
		bodyHead.createShape(head);
		bodyHead.setMassFromShapes();
		bodyHead.m_angularDamping = 0.7F;
		//bodies.put(bodyHead, 20);

		head.radius = 15 / Stage.ratio;

		headBodyDef.position.set(new Vec2(0, 33 / Stage.ratio));
		bodySeg1 = world.createBody(headBodyDef);
		bodySeg1.createShape(head);
		bodySeg1.setMassFromShapes();
		bodies.put(bodySeg1, 15);

		headBodyDef.position.set(new Vec2(0, 66 / Stage.ratio));
		bodySeg2 = world.createBody(headBodyDef);
		bodySeg2.createShape(head);
		bodySeg2.setMassFromShapes();
		bodies.put(bodySeg2, 15);

		headBodyDef.position.set(new Vec2(0, 99 / Stage.ratio));
		bodySeg3 = world.createBody(headBodyDef);
		bodySeg3.createShape(head);
		bodySeg3.setMassFromShapes();
		bodies.put(bodySeg3, 15);

		headBodyDef.position.set(new Vec2(0, 132 / Stage.ratio));
		bodySeg4 = world.createBody(headBodyDef);
		bodySeg4.createShape(head);
		bodySeg4.setMassFromShapes();
		bodies.put(bodySeg4, 15);

		RevoluteJointDef jointDef1 = new RevoluteJointDef();
		jointDef1.initialize(bodyHead, bodySeg1, new Vec2(0, 19 / Stage.ratio));
		joint1 = world.createJoint(jointDef1);

		RevoluteJointDef jointDef2 = new RevoluteJointDef();
		jointDef2.initialize(bodySeg2, bodySeg1, new Vec2(0, 52 / Stage.ratio));
		joint2 = world.createJoint(jointDef2);

		RevoluteJointDef jointDef3 = new RevoluteJointDef();
		jointDef3.initialize(bodySeg2, bodySeg3, new Vec2(0, 85 / Stage.ratio));
		joint3 = world.createJoint(jointDef3);

		RevoluteJointDef jointDef4 = new RevoluteJointDef();
		jointDef4.initialize(bodySeg4, bodySeg3, new Vec2(0, 118 / Stage.ratio));
		joint4 = world.createJoint(jointDef4);
	}

	@Override
	public void move(int x, int y) {
		bodyHead.setXForm(new Vec2(x / Stage.ratio, y / Stage.ratio), 0);
		bodySeg1.setXForm(new Vec2(x / Stage.ratio, (y + 33) / Stage.ratio), 0);
		bodySeg2.setXForm(new Vec2(x / Stage.ratio, (y + 66) / Stage.ratio), 0);
		bodySeg3.setXForm(new Vec2(x / Stage.ratio, (y + 99) / Stage.ratio), 0);
		bodySeg4.setXForm(new Vec2(x / Stage.ratio, (y + 132) / Stage.ratio), 0);
	}

	@Override
	public void destroy() {
		super.destroy();
		world.destroyJoint(joint1);
		world.destroyJoint(joint2);
		world.destroyJoint(joint3);
		world.destroyJoint(joint4);

		world.destroyBody(bodyHead);
		world.destroyBody(bodySeg1);
		world.destroyBody(bodySeg2);
		world.destroyBody(bodySeg3);
		world.destroyBody(bodySeg4);
	}

	@Override
	public void draw(Canvas canvas, Camera camera) {
		super.draw(canvas, camera);
		for (Body i : bodies.keySet()) {
			Vec2 p = i.getPosition();
			canvas.drawCircle(camera.transformRelativeX((int) (p.x * Stage.ratio)), camera.transformRelativeY((int) (p.y * Stage.ratio)), bodies.get(i), paint);
		}

		Vec2 p = getMainBody().getPosition();

		int actualX = camera.transformRelativeX((int) (p.x * Stage.ratio));
		int actualY = camera.transformRelativeY((int) (p.y * Stage.ratio));

		canvas.save();
		canvas.rotate((float) Math.toDegrees(bodyHead.getAngle()), actualX, actualY);
		drawableEye.setBounds(actualX - 20, actualY - 20, actualX + 20, actualY + 20);
		drawableEye.draw(canvas);
		canvas.restore();
		//canvas.drawCircle(camera.transformX((int) (p.x * Stage.ratio - camera.getX())), camera.transformY((int) (p.y * Stage.ratio + camera.getY())), 40, paint);

		if (getBoost()) {
			actualX = camera.transformRelativeX((int) (p.x * Stage.ratio + (20 * Math.sin(getMainBody().getAngle()))));
			actualY = camera.transformRelativeY((int) (p.y * Stage.ratio + (20 * Math.cos(3.141592f + getMainBody().getAngle()))));

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
		return bodyHead;
	}

	@Override
	public void applyImpulse(Vec2 impulse) {
		getMainBody().applyImpulse(impulse, getMainBody().getPosition().add(new Vec2((float) (20 * Math.sin(getMainBody().getAngle())) / Stage.ratio, (float) (20 * Math.cos(getMainBody().getAngle())) / Stage.ratio)));
	}

}