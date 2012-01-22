package com.thorgaming.throwme.displayobjects.game.characters;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.thorgaming.throwme.Camera;
import com.thorgaming.throwme.R;
import com.thorgaming.throwme.stage;
import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.displayobjects.game.characters.Character;

public class Car extends Character {

	private World world;
	private Body bodyBody;
	private Body bodyTop;
	private Body bodyWheels;
	private Drawable drawableBalloons;
	private Drawable drawableCar;
	
	private Joint joint1;
	private Joint joint2;
	private Joint joint3;
	private Joint joint4;
	
	public Car() {
		super();
		drawableBalloons = ThrowMe.getInstance().stage.getResources().getDrawable(R.drawable.balloon);
		drawableCar = ThrowMe.getInstance().stage.getResources().getDrawable(R.drawable.car);
		
		world = ThrowMe.getInstance().stage.world;

		PolygonDef base = new PolygonDef();
		base.setAsBox(50 / Stage.ratio, 20 / Stage.ratio);
		base.filter.groupIndex = -1;
		base.density = (float) 0.14;
		base.friction = (float) 0.5;
		base.restitution = (float) 0.5;
		base.userData = this;
		
		BodyDef headBodyDef = new BodyDef();
		headBodyDef.position.set(new Vec2(0, 0));

		bodyBody = world.createBody(headBodyDef);
		bodyBody.createShape(base);
		bodyBody.setMassFromShapes();
		bodyBody.m_angularDamping = 0.4F;
		
		PolygonDef top = new PolygonDef();
		top.setAsBox(25 / Stage.ratio, 8 / Stage.ratio);
		top.filter.groupIndex = -1;
		top.density = (float) 0.14;
		top.friction = (float) 0.5;
		top.restitution = (float) 0.6;
		top.userData = this;
		
		headBodyDef.position.set(new Vec2(0, -26 / Stage.ratio));
		bodyTop = world.createBody(headBodyDef);
		bodyTop.createShape(top);
		bodyTop.setMassFromShapes();
		
		CircleDef wheel = new CircleDef();
		wheel.radius = 15 / Stage.ratio;
		wheel.filter.groupIndex = -1;
		wheel.density = (float) 0.14;
		wheel.friction = (float) 0.5;
		wheel.restitution = (float) 0.8;
		wheel.userData = this;
		
		headBodyDef.position.set(new Vec2(-20 / Stage.ratio, 19 / Stage.ratio));
		bodyWheels = world.createBody(headBodyDef);
		bodyWheels.createShape(wheel);
		wheel.localPosition = new Vec2(40 / Stage.ratio, 0);
		bodyWheels.createShape(wheel);
		bodyWheels.setMassFromShapes();
		
		RevoluteJointDef jointDef = new RevoluteJointDef();
		jointDef.initialize(bodyBody, bodyTop, new Vec2(-10 / Stage.ratio, -21 / Stage.ratio));
		joint1 = world.createJoint(jointDef);
		
		RevoluteJointDef jointDef2 = new RevoluteJointDef();
		jointDef2.initialize(bodyBody, bodyTop, new Vec2(10 / Stage.ratio, -21 / Stage.ratio));
		joint2 = world.createJoint(jointDef2);
		
		RevoluteJointDef jointDef3 = new RevoluteJointDef();
		jointDef3.initialize(bodyBody, bodyWheels, new Vec2(-20 / Stage.ratio, 19 / Stage.ratio));
		joint3 = world.createJoint(jointDef3);
		
		RevoluteJointDef jointDef4 = new RevoluteJointDef();
		jointDef4.initialize(bodyBody, bodyWheels, new Vec2(20 / Stage.ratio, 19 / Stage.ratio));
		joint4 = world.createJoint(jointDef4);
	}

	@Override
	public void move(int x, int y) {
		bodyBody.setXForm(new Vec2(x / Stage.ratio, y / Stage.ratio), 0);
		bodyTop.setXForm(new Vec2(x / Stage.ratio, (y - 48) / Stage.ratio), 0);
		bodyWheels.setXForm(new Vec2((x - 20) / Stage.ratio, (y + 19) / Stage.ratio), 0);
	}

	@Override
	public void destroy() {
		super.destroy();
		world.destroyJoint(joint1);
		world.destroyJoint(joint2);
		world.destroyJoint(joint3);
		world.destroyJoint(joint4);
		
		world.destroyBody(bodyBody);
		world.destroyBody(bodyTop);
		world.destroyBody(bodyWheels);
	}

	@Override
	public void draw(Canvas canvas, Camera camera) {
		super.draw(canvas, camera);
		
		Vec2 p = bodyBody.getPosition();
		
		int actualX = camera.transformRelativeX((int) (p.x * Stage.ratio));
		int actualY = camera.transformRelativeY((int) (p.y * Stage.ratio));
		
		canvas.save();
		canvas.rotate((float) Math.toDegrees(bodyTop.getAngle()), actualX, actualY);
		drawableCar.setBounds(actualX - 52, actualY - 34, actualX + 52, actualY + 34);
		drawableCar.draw(canvas);
		canvas.restore();
		
		if (getBoost()) {
			float angle = (float) (-getMainBody().getAngle() + 1.570796);
			int x2 = (int) (actualX + Math.sin(angle) * 20);
			int y2 = (int) (actualY + Math.cos(angle) * 20);
			
			drawableBalloons.setBounds(x2 - 19, y2 - 108, x2 + 15, y2 + 2);
			
			canvas.save();
			canvas.rotate((float) (Math.sin((float) balloonBar / 20 - 4.1887902) * 15), x2, y2);
			drawableBalloons.draw(canvas);
			canvas.restore();
			canvas.save();
			canvas.rotate((float) (Math.sin((float) balloonBar / 20) * 15), x2, y2);
			drawableBalloons.draw(canvas);
			canvas.restore();
			
			angle += 3.141592;
			
			x2 = (int) (actualX + Math.sin(angle) * 20);
			y2 = (int) (actualY + Math.cos(angle) * 20);
			
			drawableBalloons.setBounds(x2 - 19, y2 - 108, x2 + 15, y2 + 2);
			
			canvas.save();
			canvas.rotate((float) (Math.sin((float) balloonBar / 20 - 4.1887902) * 15), x2, y2);
			drawableBalloons.draw(canvas);
			canvas.restore();
			canvas.save();
			canvas.rotate((float) (Math.sin((float) balloonBar / 20 - 2.0943951) * 15), x2, y2);
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
		float angle = (float) (-getMainBody().getAngle() + 1.570796);
		
		getMainBody().applyImpulse(impulse.mul(0.5f), getMainBody().getWorldCenter().add(new Vec2((float) (20 * Math.sin(angle)) / Stage.ratio, (float) (20 * Math.cos(angle)) / Stage.ratio)));
		angle += 3.141592;
		getMainBody().applyImpulse(impulse.mul(0.5f), getMainBody().getWorldCenter().add(new Vec2((float) (20 * Math.sin(angle)) / Stage.ratio, (float) (20 * Math.cos(angle)) / Stage.ratio)));
	}

}