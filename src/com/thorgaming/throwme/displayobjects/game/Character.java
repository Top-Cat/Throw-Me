package com.thorgaming.throwme.displayobjects.game;

import java.util.HashMap;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.DistanceJointDef;
import org.jbox2d.dynamics.joints.Joint;

import com.thorgaming.throwme.Stage;
import com.thorgaming.throwme.ThrowMe;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import com.thorgaming.throwme.Camera;
import com.thorgaming.throwme.R;
import com.thorgaming.throwme.displayobjects.DispObj;

public class Character extends DispObj {
	
	private World world;
	private Body bodyBody;
	public Body bodyHead;
	private Body bodyRightElbow;
	private Body bodyRightHand;
	private Body bodyLeftElbow;
	private Body bodyLeftHand;
	private Body bodyLeftKnee;
	private Body bodyLeftFoot;
	private Body bodyRightKnee;
	private Body bodyRightFoot;
	
	private Joint jointNeck;
	
	private Joint jointUpperRightArm;
	private Joint jointLowerRightArm;
	private Joint jointRightArm;
	private Joint jointUpperLeftArm;
	private Joint jointLowerLeftArm;
	private Joint jointLeftArm;
	
	private Joint jointUpperRightLeg;
	private Joint jointLowerRightLeg;
	private Joint jointRightLeg;
	private Joint jointUpperLeftLeg;
	private Joint jointLowerLeftLeg;
	private Joint jointLeftLeg;
	
	private float mouseX, mouseY;
	public boolean lose = false;
	private boolean balloons = false;
	public boolean end = false;
	private HashMap<Body, Integer> bodies = new HashMap<Body, Integer>();
	private Paint paint = new Paint();
	private Drawable drawableEye;
	
	public Character() {
		drawableEye = ThrowMe.stage.getResources().getDrawable(R.drawable.eye);
		this.world = ThrowMe.stage.world;
		paint.setColor(Color.rgb(255, 153, 0));
		paint.setTextSize(30);
		paint.setSubpixelText(true);
		paint.setStrokeWidth(1);

		CircleDef head = new CircleDef();  
		head.radius = (float) 20 / Stage.ratio;
		head.filter.groupIndex = -1;
		head.density = (float) 0.5;
		head.friction = (float) 0.5;
		head.restitution = (float) 0.6;
		head.userData = this;
		
		BodyDef headBodyDef = new BodyDef();
		headBodyDef.position.set(new Vec2(0, 0));
		
		bodyHead = world.createBody(headBodyDef);
		bodyHead.createShape(head);
		bodyHead.setMassFromShapes();
		bodyHead.m_angularDamping = 0.7F;
		//bodies.put(h, 20);
		
		PolygonDef body = new PolygonDef();
		body.setAsBox(15 / Stage.ratio, 30 / Stage.ratio);
		body.filter.groupIndex = -1;
		body.density = (float) 0.1;
		body.friction = (float) 0.5;
		body.restitution = (float) 0.6;
		body.userData = this;
		
		BodyDef bodyBodyDef = new BodyDef();
		bodyBodyDef.position.set(new Vec2(0, 60 / Stage.ratio));
		bodyBody = world.createBody(bodyBodyDef);
		bodyBody.createShape(body);
		bodyBody.setMassFromShapes();
		
		DistanceJointDef neck = new DistanceJointDef();
		neck.initialize(bodyHead, bodyBody, new Vec2(0, 0), new Vec2(0, 25 / Stage.ratio));
		jointNeck = world.createJoint(neck);
		
		CircleDef elbow = new CircleDef();
		elbow.radius = (float) 3 / Stage.ratio;
		elbow.filter.groupIndex = -1;
		elbow.density = (float) 0.5;
		elbow.friction = (float) 0.5;
		elbow.restitution = (float) 0.6;
		elbow.userData = this;
		
		BodyDef elbowDef = new BodyDef();
		elbowDef.position.set(new Vec2(40 / Stage.ratio, 30 / Stage.ratio));
		bodyRightElbow = world.createBody(elbowDef);
		bodyRightElbow.createShape(elbow);
		bodyRightElbow.setMassFromShapes();
		bodies.put(bodyRightElbow, 3);
		
		DistanceJointDef uarm1 = new DistanceJointDef();
		uarm1.initialize(bodyBody, bodyRightElbow, new Vec2(15 / Stage.ratio, 30 / Stage.ratio), new Vec2(40 / Stage.ratio, 30 / Stage.ratio));
		jointUpperRightArm = world.createJoint(uarm1);
		
		CircleDef hand = new CircleDef();
		hand.radius = (float) 5 / Stage.ratio;
		hand.filter.groupIndex = -1;
		hand.density = (float) 0.5;
		hand.friction = (float) 0.5;
		hand.restitution = (float) 0.6;
		hand.userData = this;
		
		BodyDef handDef = new BodyDef();
		handDef.position.set(new Vec2(65 / Stage.ratio, 30 / Stage.ratio));
		bodyRightHand = world.createBody(handDef);
		bodyRightHand.createShape(hand);
		bodyRightHand.setMassFromShapes();
		bodies.put(bodyRightHand, 5);
		
		DistanceJointDef larm1 = new DistanceJointDef();
		larm1.initialize(bodyBody, bodyRightHand, new Vec2(15 / Stage.ratio, 30 / Stage.ratio), new Vec2(65 / Stage.ratio, 30 / Stage.ratio));
		jointLowerRightArm = world.createJoint(larm1);
		
		DistanceJointDef arm1 = new DistanceJointDef();
		arm1.initialize(bodyRightElbow, bodyRightHand, new Vec2(40 / Stage.ratio, 30 / Stage.ratio), new Vec2(65 / Stage.ratio, 30 / Stage.ratio));
		jointRightArm = world.createJoint(arm1);
		
		
		
		
		elbowDef.position.set(new Vec2(-40 / Stage.ratio, 30 / Stage.ratio));
		bodyLeftElbow = world.createBody(elbowDef);
		bodyLeftElbow.createShape(elbow);
		bodyLeftElbow.setMassFromShapes();
		bodies.put(bodyLeftElbow, 3);
		
		DistanceJointDef uarm2 = new DistanceJointDef();
		uarm2.initialize(bodyBody, bodyLeftElbow, new Vec2(-15 / Stage.ratio, 30 / Stage.ratio), new Vec2(-40 / Stage.ratio, 30 / Stage.ratio));
		jointUpperLeftArm = world.createJoint(uarm2);
		
		handDef.position.set(new Vec2(-65 / Stage.ratio, 30 / Stage.ratio));
		bodyLeftHand = world.createBody(handDef);
		bodyLeftHand.createShape(hand);
		bodyLeftHand.setMassFromShapes();
		bodies.put(bodyLeftHand, 5);
		
		DistanceJointDef larm2 = new DistanceJointDef();
		larm2.initialize(bodyBody, bodyLeftHand, new Vec2(-15 / Stage.ratio, 30 / Stage.ratio), new Vec2(-65 / Stage.ratio, 30 / Stage.ratio));
		jointLowerLeftArm = world.createJoint(larm2);
		
		DistanceJointDef arm2 = new DistanceJointDef();
		arm2.initialize(bodyLeftElbow, bodyLeftHand, new Vec2(-40 / Stage.ratio, 30 / Stage.ratio), new Vec2(-65 / Stage.ratio, 30 / Stage.ratio));
		jointLeftArm = world.createJoint(arm2);
		
		
		
		
		elbowDef.position.set(new Vec2(-40 / Stage.ratio, 90 / Stage.ratio));
		bodyLeftKnee = world.createBody(elbowDef);
		bodyLeftKnee.createShape(elbow);
		bodyLeftKnee.setMassFromShapes();
		bodies.put(bodyLeftKnee, 3);
		
		DistanceJointDef uarm3 = new DistanceJointDef();
		uarm3.initialize(bodyBody, bodyLeftKnee, new Vec2(-15 / Stage.ratio, 90 / Stage.ratio), new Vec2(-40 / Stage.ratio, 90 / Stage.ratio));
		jointUpperLeftLeg = world.createJoint(uarm3);
		
		handDef.position.set(new Vec2(-65 / Stage.ratio, 90 / Stage.ratio));
		bodyLeftFoot = world.createBody(handDef);
		bodyLeftFoot.createShape(hand);
		bodyLeftFoot.setMassFromShapes();
		bodies.put(bodyLeftFoot, 5);
		
		DistanceJointDef larm3 = new DistanceJointDef();
		larm3.initialize(bodyBody, bodyLeftFoot, new Vec2(-15 / Stage.ratio, 90 / Stage.ratio), new Vec2(-65 / Stage.ratio, 90 / Stage.ratio));
		jointLowerLeftLeg = world.createJoint(larm3);
		
		DistanceJointDef arm3 = new DistanceJointDef();
		arm3.initialize(bodyLeftKnee, bodyLeftFoot, new Vec2(-40 / Stage.ratio, 90 / Stage.ratio), new Vec2(-65 / Stage.ratio, 90 / Stage.ratio));
		jointLeftLeg = world.createJoint(arm3);
		
		
		
		
		
		elbowDef.position.set(new Vec2(40 / Stage.ratio, 90 / Stage.ratio));
		bodyRightKnee = world.createBody(elbowDef);
		bodyRightKnee.createShape(elbow);
		bodyRightKnee.setMassFromShapes();
		bodies.put(bodyRightKnee, 3);
		
		DistanceJointDef uarm4 = new DistanceJointDef();
		uarm4.initialize(bodyBody, bodyRightKnee, new Vec2(15 / Stage.ratio, 90 / Stage.ratio), new Vec2(40 / Stage.ratio, 90 / Stage.ratio));
		jointUpperRightLeg = world.createJoint(uarm4);
		
		handDef.position.set(new Vec2(65 / Stage.ratio, 90 / Stage.ratio));
		bodyRightFoot = world.createBody(handDef);
		bodyRightFoot.createShape(hand);
		bodyRightFoot.setMassFromShapes();
		bodies.put(bodyRightFoot, 5);
		
		DistanceJointDef larm4 = new DistanceJointDef();
		larm4.initialize(bodyBody, bodyRightFoot, new Vec2(15 / Stage.ratio, 90 / Stage.ratio), new Vec2(65 / Stage.ratio, 90 / Stage.ratio));
		jointLowerRightLeg = world.createJoint(larm4);
		
		DistanceJointDef arm4 = new DistanceJointDef();
		arm4.initialize(bodyRightKnee, bodyRightFoot, new Vec2(40 / Stage.ratio, 90 / Stage.ratio), new Vec2(65 / Stage.ratio, 90 / Stage.ratio));
		jointRightLeg = world.createJoint(arm4);
	}
	
	@Override
	public DispObj setX(int x) {
		move(x, (int) (bodyHead.getWorldCenter().y * Stage.ratio));
		return this;
	}
	
	@Override
	public DispObj setY(int y) {
		move((int) (bodyHead.getWorldCenter().x * Stage.ratio), y);
		return this;
	}
	
	@Override
	public void move(int x, int y) {
		bodyHead.setXForm(new Vec2((float) x / Stage.ratio, (float) y / Stage.ratio), 0);
		bodyBody.setXForm(new Vec2((float) x / Stage.ratio, (float) (y + 60) / Stage.ratio), 0);
		bodyRightElbow.setXForm(new Vec2((float) (x + 40) / Stage.ratio, (float) (y + 30) / Stage.ratio), 0);
		bodyRightHand.setXForm(new Vec2((float) (x + 65) / Stage.ratio, (float) (y + 30) / Stage.ratio), 0);
		bodyLeftElbow.setXForm(new Vec2((float) (x - 40) / Stage.ratio, (float) (y + 30) / Stage.ratio), 0);
		bodyLeftHand.setXForm(new Vec2((float) (x - 65) / Stage.ratio, (float) (y + 30) / Stage.ratio), 0);
		
		bodyRightKnee.setXForm(new Vec2((float) (x + 40) / Stage.ratio, (float) (y + 90) / Stage.ratio), 0);
		bodyRightFoot.setXForm(new Vec2((float) (x + 65) / Stage.ratio, (float) (y + 90) / Stage.ratio), 0);
		bodyLeftKnee.setXForm(new Vec2((float) (x - 40) / Stage.ratio, (float) (y + 90) / Stage.ratio), 0);
		bodyLeftFoot.setXForm(new Vec2((float) (x - 65) / Stage.ratio, (float) (y + 90) / Stage.ratio), 0);
	}
	
	@Override
	public void destroy() {
		super.destroy();
		world.destroyJoint(jointNeck);
		world.destroyJoint(jointUpperRightArm);
		world.destroyJoint(jointLowerRightArm);
		world.destroyJoint(jointRightArm);
		world.destroyJoint(jointUpperLeftArm);
		world.destroyJoint(jointLowerLeftArm);
		world.destroyJoint(jointLeftArm);
		
		world.destroyJoint(jointUpperRightLeg);
		world.destroyJoint(jointLowerRightLeg);
		world.destroyJoint(jointRightLeg);
		world.destroyJoint(jointUpperLeftLeg);
		world.destroyJoint(jointLowerLeftLeg);
		world.destroyJoint(jointLeftLeg);
		
		world.destroyBody(bodyBody);
		world.destroyBody(bodyHead);
		world.destroyBody(bodyRightElbow);
		world.destroyBody(bodyRightHand);
		world.destroyBody(bodyLeftElbow);
		world.destroyBody(bodyLeftHand);
		world.destroyBody(bodyLeftKnee);
		world.destroyBody(bodyLeftFoot);
		world.destroyBody(bodyRightKnee);
		world.destroyBody(bodyRightFoot);
	}
	
	int k = 16; // Spring constant
	double damping = 2;
	int length = 1;
	int backwards = 0;
	float previousHeadX = 0;
	double avgSpeed = 1;
	
	@Override
	public void draw(Canvas canvas, Camera camera) {
		if (!lose) {
			Vec2 world1 = bodyHead.getPosition();
			Vec2 world2 = new Vec2(mouseX / Stage.ratio, mouseY / Stage.ratio);
			Vec2 d = world2.sub(world1);
			float dlen = d.normalize();
			Vec2 springForce = d.clone();
			springForce = springForce.mul( (dlen - length) * k );
			bodyHead.applyForce(springForce, world1);
			Vec2 v1 = bodyHead.getLinearVelocityFromWorldPoint(world1);
			Vec2 v2 = new Vec2(0,0);
			Vec2 v = v2.sub(v1);
			Vec2 dampingForce = d.clone();
			dampingForce = dampingForce.mul((float) (Vec2.dot(v,d) * damping));
			bodyHead.applyForce(dampingForce, world1);
			canvas.drawLine(bodyHead.getWorldCenter().x * Stage.ratio, bodyHead.getWorldCenter().y * Stage.ratio, mouseX, mouseY, paint);
		} else {
			avgSpeed -= (avgSpeed - (bodyHead.getWorldCenter().x - previousHeadX)) / 100;
			previousHeadX = bodyHead.getWorldCenter().x;
			if (avgSpeed < (1/Stage.ratio)) {
				end = true;
			}

			int nx = (int) (camera.getX() - (camera.getX() + (-(Stage.ratio)*bodyHead.getWorldCenter().x + camera.getScreenWidth()/2))/10);
			int ny = (int) (camera.getY() - (camera.getY() - (-(Stage.ratio)*bodyHead.getWorldCenter().y + camera.getScreenHeight()/2))/10);
			ny = ny < 0 ? camera.getY() : ny;
			nx = nx < camera.getX() ? camera.getX() : nx;
			camera.setCameraXY(nx, ny);
			
			canvas.drawText("Distance: " + (nx / 10) + "m", 10, 40, paint);
			String altitudeText = "Altitude: " + ((int) -(bodyHead.getWorldCenter().y - 12)*2) + "m";
			android.graphics.Rect bounds = new android.graphics.Rect();
			paint.getTextBounds(altitudeText, 0, altitudeText.length(), bounds);
			canvas.drawText(altitudeText, camera.getScreenWidth() - bounds.width() - 10, 40, paint);
		}
		for (Body i : bodies.keySet()) {
			Vec2 p = i.getPosition();
			canvas.drawCircle((((p.x * Stage.ratio) - camera.getX()) * camera.getScreenWidth()) / 800, (((p.y * Stage.ratio) + camera.getY()) * camera.getScreenHeight()) / 480, bodies.get(i), paint);
		}
		
		Vec2 p = bodyHead.getPosition();
		
		int actualX = (int) ((((p.x * Stage.ratio) - camera.getX()) * camera.getScreenWidth()) / 800);
		int actualY = (int) ((((p.y * Stage.ratio) + camera.getY()) * camera.getScreenHeight()) / 480);
		
		canvas.rotate((float) Math.toDegrees(bodyHead.getAngle()), actualX, actualY);
		drawableEye.setBounds(actualX - 20, actualY - 20, actualX + 20, actualY + 20);
		drawableEye.draw(canvas);
		canvas.restore();
		
		if (balloons) {
			bodyHead.applyImpulse(new Vec2(0.02F, -0.13F), bodyHead.getWorldCenter().add(new Vec2((float) (20 * Math.cos(bodyHead.getAngle())) / Stage.ratio, (float) (20 * Math.sin(bodyHead.getAngle()))  / Stage.ratio)));
			balloons = false;
		}
	} 
	
	public void mouse(float x, float y) {
		mouseX = x;
		mouseY = y;
		
		if (lose) {
			balloons = true;
		}
	}
	
	public void mouseDown(float x, float y) {
		synchronized (ThrowMe.stage.drawThread.physicsSync) {
			Vec2 linearVelocity = bodyHead.getLinearVelocity();
			bodyHead.setLinearVelocity(new Vec2(Math.max(linearVelocity.x, 3), Math.min(linearVelocity.y, -3)));
		}
	}
	
	@Override
	public boolean checkPress(int x, int y) {
		return false;
	}
	
}