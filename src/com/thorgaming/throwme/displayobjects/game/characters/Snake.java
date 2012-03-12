package com.thorgaming.throwme.displayobjects.game.characters;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.drawing.Camera;
import com.thorgaming.throwme.drawing.Stage;

/**
 * Throwable snake character
 * 
 * @author Thomas Cheyney
 * @version 1.0
 */
public class Snake extends Character {

	/**
	 * Head of the snake
	 */
	private Body bodyHead;
	/**
	 * First body segment
	 */
	private Body bodySeg1;
	/**
	 * Second body segment
	 */
	private Body bodySeg2;
	/**
	 * Third body segment
	 */
	private Body bodySeg3;
	/**
	 * Fourth body segment
	 */
	private Body bodySeg4;
	/**
	 * Drawable used to display the head of the snake as en eye
	 */
	private Drawable drawableEye;
	/**
	 * Joint connecting the head and the first body segment
	 */
	private Joint joint1;
	/**
	 * Joint connecting the first and second body segment
	 */
	private Joint joint2;
	/**
	 * Joint connecting the second and third body segment
	 */
	private Joint joint3;
	/**
	 * Joint connecting the third and fourth body segment
	 */
	private Joint joint4;
	/**
	 * List of shapes to draw
	 */
	private List<Body> bodies = new ArrayList<Body>();

	public Snake() {
		super();
		World world = ThrowMe.getInstance().stage.world;

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

		head.radius = 15 / Stage.ratio;

		headBodyDef.position.set(new Vec2(0, 33 / Stage.ratio));
		bodySeg1 = world.createBody(headBodyDef);
		bodySeg1.createShape(head);
		bodySeg1.setMassFromShapes();
		bodies.add(bodySeg1);

		headBodyDef.position.set(new Vec2(0, 66 / Stage.ratio));
		bodySeg2 = world.createBody(headBodyDef);
		bodySeg2.createShape(head);
		bodySeg2.setMassFromShapes();
		bodies.add(bodySeg2);

		headBodyDef.position.set(new Vec2(0, 99 / Stage.ratio));
		bodySeg3 = world.createBody(headBodyDef);
		bodySeg3.createShape(head);
		bodySeg3.setMassFromShapes();
		bodies.add(bodySeg3);

		headBodyDef.position.set(new Vec2(0, 132 / Stage.ratio));
		bodySeg4 = world.createBody(headBodyDef);
		bodySeg4.createShape(head);
		bodySeg4.setMassFromShapes();
		bodies.add(bodySeg4);

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
		World world = ThrowMe.getInstance().stage.world;
		
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
		for (Body i : bodies) {
			Vec2 p = i.getPosition();
			canvas.drawCircle(camera.transformRelativeX((int) (p.x * Stage.ratio)), camera.transformRelativeY((int) (p.y * Stage.ratio)), 15, paint);
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
			actualX = camera.transformRelativeX((int) (p.x * Stage.ratio + 20 * Math.sin(getMainBody().getAngle())));
			actualY = camera.transformRelativeY((int) (p.y * Stage.ratio + 20 * Math.cos(3.141592f + getMainBody().getAngle())));

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

			applyImpulse(new Vec2(0.025F, -0.14F));
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