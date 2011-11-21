package thorgaming.throwme.DispObjs;

import java.util.HashMap;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.DistanceJointDef;
import org.jbox2d.dynamics.joints.MouseJoint;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import thorgaming.throwme.Camera;
import thorgaming.throwme.Stage;
import thorgaming.throwme.DispObj;

public class Character extends DispObj {
	
	World world;
	Body bodyBody, bodyHead, bodyRightElbow, bodyRightHand, bodyLeftElbow, bodyLeftHand, bodyLeftKnee, bodyLeftFoot, bodyRightKnee, bodyRightFoot;
	public boolean end = false;
	HashMap<Body, Integer> bodies = new HashMap<Body, Integer>();
	Paint paint = new Paint();
	MouseJoint mouseJoint;
	Drawable drawableEye;
	
	public Character(Stage stage, Resources r, int eye, World world, int x, int y) {
		drawableEye = r.getDrawable(eye);
		this.world = world;
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
		headBodyDef.position.set(new Vec2(x / Stage.ratio, y / Stage.ratio));
		
		bodyHead = world.createBody(headBodyDef);
		bodyHead.createShape(head);
		bodyHead.setMassFromShapes();
		//bodies.put(h, 20);
		
		PolygonDef body = new PolygonDef();
		body.setAsBox(15 / Stage.ratio, 30 / Stage.ratio);
		body.filter.groupIndex = -1;
		body.density = (float) 0.1;
		body.friction = (float) 0.5;
		body.restitution = (float) 0.6;
		body.userData = this;
		
		BodyDef bodyBodyDef = new BodyDef();
		bodyBodyDef.position.set(new Vec2(x / Stage.ratio, (y + 60) / Stage.ratio));
		bodyBody = world.createBody(bodyBodyDef);
		bodyBody.createShape(body);
		bodyBody.setMassFromShapes();
		
		DistanceJointDef neck = new DistanceJointDef();
		neck.initialize(bodyHead, bodyBody, new Vec2(x / Stage.ratio, y / Stage.ratio), new Vec2(x / Stage.ratio, (y + 25) / Stage.ratio));
		world.createJoint(neck);
		
		CircleDef elbow = new CircleDef();
		elbow.radius = (float) 3 / Stage.ratio;
		elbow.filter.groupIndex = -1;
		elbow.density = (float) 0.5;
		elbow.friction = (float) 0.5;
		elbow.restitution = (float) 0.6;
		elbow.userData = this;
		
		BodyDef elbowDef = new BodyDef();
		elbowDef.position.set(new Vec2((x + 40) / Stage.ratio, (y + 30) / Stage.ratio));
		bodyRightElbow = world.createBody(elbowDef);
		bodyRightElbow.createShape(elbow);
		bodyRightElbow.setMassFromShapes();
		bodies.put(bodyRightElbow, 3);
		
		DistanceJointDef uarm1 = new DistanceJointDef();
		uarm1.initialize(bodyBody, bodyRightElbow, new Vec2((x + 15) / Stage.ratio, (y + 30) / Stage.ratio), new Vec2((x + 40) / Stage.ratio, (y + 30) / Stage.ratio));
		world.createJoint(uarm1);
		
		CircleDef hand = new CircleDef();
		hand.radius = (float) 5 / Stage.ratio;
		hand.filter.groupIndex = -1;
		hand.density = (float) 0.5;
		hand.friction = (float) 0.5;
		hand.restitution = (float) 0.6;
		hand.userData = this;
		
		BodyDef handDef = new BodyDef();
		handDef.position.set(new Vec2((x + 65) / Stage.ratio, (y + 30) / Stage.ratio));
		bodyRightHand = world.createBody(handDef);
		bodyRightHand.createShape(hand);
		bodyRightHand.setMassFromShapes();
		bodies.put(bodyRightHand, 5);
		
		DistanceJointDef larm1 = new DistanceJointDef();
		larm1.initialize(bodyBody, bodyRightHand, new Vec2((x + 15) / Stage.ratio, (y + 30) / Stage.ratio), new Vec2((x + 65) / Stage.ratio, (y + 30) / Stage.ratio));
		world.createJoint(larm1);
		
		DistanceJointDef arm1 = new DistanceJointDef();
		arm1.initialize(bodyRightElbow, bodyRightHand, new Vec2((x + 40) / Stage.ratio, (y + 30) / Stage.ratio), new Vec2((x + 65) / Stage.ratio, (y + 30) / Stage.ratio));
		world.createJoint(arm1);
		
		
		
		
		elbowDef.position.set(new Vec2((x - 40) / Stage.ratio, (y + 30) / Stage.ratio));
		bodyLeftElbow = world.createBody(elbowDef);
		bodyLeftElbow.createShape(elbow);
		bodyLeftElbow.setMassFromShapes();
		bodies.put(bodyLeftElbow, 3);
		
		DistanceJointDef uarm2 = new DistanceJointDef();
		uarm2.initialize(bodyBody, bodyLeftElbow, new Vec2((x - 15) / Stage.ratio, (y + 30) / Stage.ratio), new Vec2((x - 40) / Stage.ratio, (y + 30) / Stage.ratio));
		world.createJoint(uarm2);
		
		handDef.position.set(new Vec2((x - 65) / Stage.ratio, (y + 30) / Stage.ratio));
		bodyLeftHand = world.createBody(handDef);
		bodyLeftHand.createShape(hand);
		bodyLeftHand.setMassFromShapes();
		bodies.put(bodyLeftHand, 5);
		
		DistanceJointDef larm2 = new DistanceJointDef();
		larm2.initialize(bodyBody, bodyLeftHand, new Vec2((x - 15) / Stage.ratio, (y + 30) / Stage.ratio), new Vec2((x - 65) / Stage.ratio, (y + 30) / Stage.ratio));
		world.createJoint(larm2);
		
		DistanceJointDef arm2 = new DistanceJointDef();
		arm2.initialize(bodyLeftElbow, bodyLeftHand, new Vec2((x - 40) / Stage.ratio, (y + 30) / Stage.ratio), new Vec2((x - 65) / Stage.ratio, (y + 30) / Stage.ratio));
		world.createJoint(arm2);
		
		
		
		
		elbowDef.position.set(new Vec2((x - 40) / Stage.ratio, (y + 90) / Stage.ratio));
		bodyLeftKnee = world.createBody(elbowDef);
		bodyLeftKnee.createShape(elbow);
		bodyLeftKnee.setMassFromShapes();
		bodies.put(bodyLeftKnee, 3);
		
		DistanceJointDef uarm3 = new DistanceJointDef();
		uarm3.initialize(bodyBody, bodyLeftKnee, new Vec2((x - 15) / Stage.ratio, (y + 90) / Stage.ratio), new Vec2((x - 40) / Stage.ratio, (y + 90) / Stage.ratio));
		world.createJoint(uarm3);
		
		handDef.position.set(new Vec2((x - 65) / Stage.ratio, (y + 90) / Stage.ratio));
		bodyLeftFoot = world.createBody(handDef);
		bodyLeftFoot.createShape(hand);
		bodyLeftFoot.setMassFromShapes();
		bodies.put(bodyLeftFoot, 5);
		
		DistanceJointDef larm3 = new DistanceJointDef();
		larm3.initialize(bodyBody, bodyLeftFoot, new Vec2((x - 15) / Stage.ratio, (y + 90) / Stage.ratio), new Vec2((x - 65) / Stage.ratio, (y + 90) / Stage.ratio));
		world.createJoint(larm3);
		
		DistanceJointDef arm3 = new DistanceJointDef();
		arm3.initialize(bodyLeftKnee, bodyLeftFoot, new Vec2((x - 40) / Stage.ratio, (y + 90) / Stage.ratio), new Vec2((x - 65) / Stage.ratio, (y + 90) / Stage.ratio));
		world.createJoint(arm3);
		
		
		
		
		
		elbowDef.position.set(new Vec2((x + 40) / Stage.ratio, (y + 90) / Stage.ratio));
		bodyRightKnee = world.createBody(elbowDef);
		bodyRightKnee.createShape(elbow);
		bodyRightKnee.setMassFromShapes();
		bodies.put(bodyRightKnee, 3);
		
		DistanceJointDef uarm4 = new DistanceJointDef();
		uarm4.initialize(bodyBody, bodyRightKnee, new Vec2((x + 15) / Stage.ratio, (y + 90) / Stage.ratio), new Vec2((x + 40) / Stage.ratio, (y + 90) / Stage.ratio));
		world.createJoint(uarm4);
		
		handDef.position.set(new Vec2((x + 65) / Stage.ratio, (y + 90) / Stage.ratio));
		bodyRightFoot = world.createBody(handDef);
		bodyRightFoot.createShape(hand);
		bodyRightFoot.setMassFromShapes();
		bodies.put(bodyRightFoot, 5);
		
		DistanceJointDef larm4 = new DistanceJointDef();
		larm4.initialize(bodyBody, bodyRightFoot, new Vec2((x + 15) / Stage.ratio, (y + 90) / Stage.ratio), new Vec2((x + 65) / Stage.ratio, (y + 90) / Stage.ratio));
		world.createJoint(larm4);
		
		DistanceJointDef arm4 = new DistanceJointDef();
		arm4.initialize(bodyRightKnee, bodyRightFoot, new Vec2((x + 40) / Stage.ratio, (y + 90) / Stage.ratio), new Vec2((x + 65) / Stage.ratio, (y + 90) / Stage.ratio));
		world.createJoint(arm4);
		
		synchronized (stage.objects) {
			stage.objects.add(this);
		}
	}
	
	@Override
	public void destroy(Stage d) {
		super.destroy(d);
	}
	
	int k = 15; // Spring constant
	double damping = .5;
	int length = 1;
	int backwards = 0;
	
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
			int nx = (int) (camera.getX() - (camera.getX() + (-(Stage.ratio)*bodyHead.getWorldCenter().x + camera.getScreenWidth()/2))/12);
			if (nx < camera.getX()) {
				backwards++;
				nx = camera.getX();
			} else {
				backwards = 0;
			}
			int ny = (int) (camera.getY() - (camera.getY() - (-(Stage.ratio)*bodyHead.getWorldCenter().y + camera.getScreenHeight()/2))/12);
			ny = ny < 0 ? camera.getY() : ny;
			camera.setCameraXY(nx, ny);
			
			canvas.drawText("Distance: " + (nx / 10), 10, 40, paint);
			
			if (backwards > 10) {
				end = true;
			}
		}
		for (Body i : bodies.keySet()) {
			Vec2 p = i.getPosition();
			canvas.drawCircle((((p.x * Stage.ratio) - camera.getX()) * camera.getScreenWidth()) / 800, (((p.y * Stage.ratio) + camera.getY()) * camera.getScreenHeight()) / 480, bodies.get(i), paint);
		}
		
		Vec2 p = bodyHead.getPosition();
		
		int ax = (int) ((((p.x * Stage.ratio) - camera.getX()) * camera.getScreenWidth()) / 800);
		int ay = (int) ((((p.y * Stage.ratio) + camera.getY()) * camera.getScreenHeight()) / 480);
		
		canvas.rotate((float) Math.toDegrees(bodyHead.getAngle()), ax, ay);
		drawableEye.setBounds(ax - 20, ay - 20, ax + 20, ay + 20);
		drawableEye.draw(canvas);
		canvas.restore();
	}

	float mouseX, mouseY;
	public boolean lose = false;
	
	public void mouse(float x, float y) {
		mouseX = x;
		mouseY = y;
	}
	
	@Override
	public boolean checkPress(int x, int y) {
		return false;
	}
	
}