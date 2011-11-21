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
	
	World w;
	Body b, h, e, ha, e2, ha2, e3, ha3, e4, ha4;
	public boolean end = false;
	HashMap<Body, Integer> bodies = new HashMap<Body, Integer>();
	Paint paint = new Paint();
	MouseJoint mj;
	Drawable eye_d;
	
	public Character(Stage stage, Resources r, int eye, World world, int x, int y) {
		eye_d = r.getDrawable(eye);
		w = world;
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
		
		h = w.createBody(headBodyDef);
		h.createShape(head);
		h.setMassFromShapes();
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
		b = w.createBody(bodyBodyDef);
		b.createShape(body);
		b.setMassFromShapes();
		
		DistanceJointDef neck = new DistanceJointDef();
		neck.initialize(h, b, new Vec2(x / Stage.ratio, y / Stage.ratio), new Vec2(x / Stage.ratio, (y + 25) / Stage.ratio));
		w.createJoint(neck);
		
		CircleDef elbow = new CircleDef();
		elbow.radius = (float) 3 / Stage.ratio;
		elbow.filter.groupIndex = -1;
		elbow.density = (float) 0.5;
		elbow.friction = (float) 0.5;
		elbow.restitution = (float) 0.6;
		elbow.userData = this;
		
		BodyDef elbowDef = new BodyDef();
		elbowDef.position.set(new Vec2((x + 40) / Stage.ratio, (y + 30) / Stage.ratio));
		e = w.createBody(elbowDef);
		e.createShape(elbow);
		e.setMassFromShapes();
		bodies.put(e, 3);
		
		DistanceJointDef uarm1 = new DistanceJointDef();
		uarm1.initialize(b, e, new Vec2((x + 15) / Stage.ratio, (y + 30) / Stage.ratio), new Vec2((x + 40) / Stage.ratio, (y + 30) / Stage.ratio));
		w.createJoint(uarm1);
		
		CircleDef hand = new CircleDef();
		hand.radius = (float) 5 / Stage.ratio;
		hand.filter.groupIndex = -1;
		hand.density = (float) 0.5;
		hand.friction = (float) 0.5;
		hand.restitution = (float) 0.6;
		hand.userData = this;
		
		BodyDef handDef = new BodyDef();
		handDef.position.set(new Vec2((x + 65) / Stage.ratio, (y + 30) / Stage.ratio));
		ha = w.createBody(handDef);
		ha.createShape(hand);
		ha.setMassFromShapes();
		bodies.put(ha, 5);
		
		DistanceJointDef larm1 = new DistanceJointDef();
		larm1.initialize(b, ha, new Vec2((x + 15) / Stage.ratio, (y + 30) / Stage.ratio), new Vec2((x + 65) / Stage.ratio, (y + 30) / Stage.ratio));
		w.createJoint(larm1);
		
		DistanceJointDef arm1 = new DistanceJointDef();
		arm1.initialize(e, ha, new Vec2((x + 40) / Stage.ratio, (y + 30) / Stage.ratio), new Vec2((x + 65) / Stage.ratio, (y + 30) / Stage.ratio));
		w.createJoint(arm1);
		
		
		
		
		elbowDef.position.set(new Vec2((x - 40) / Stage.ratio, (y + 30) / Stage.ratio));
		e2 = w.createBody(elbowDef);
		e2.createShape(elbow);
		e2.setMassFromShapes();
		bodies.put(e2, 3);
		
		DistanceJointDef uarm2 = new DistanceJointDef();
		uarm2.initialize(b, e2, new Vec2((x - 15) / Stage.ratio, (y + 30) / Stage.ratio), new Vec2((x - 40) / Stage.ratio, (y + 30) / Stage.ratio));
		w.createJoint(uarm2);
		
		handDef.position.set(new Vec2((x - 65) / Stage.ratio, (y + 30) / Stage.ratio));
		ha2 = w.createBody(handDef);
		ha2.createShape(hand);
		ha2.setMassFromShapes();
		bodies.put(ha2, 5);
		
		DistanceJointDef larm2 = new DistanceJointDef();
		larm2.initialize(b, ha2, new Vec2((x - 15) / Stage.ratio, (y + 30) / Stage.ratio), new Vec2((x - 65) / Stage.ratio, (y + 30) / Stage.ratio));
		w.createJoint(larm2);
		
		DistanceJointDef arm2 = new DistanceJointDef();
		arm2.initialize(e2, ha2, new Vec2((x - 40) / Stage.ratio, (y + 30) / Stage.ratio), new Vec2((x - 65) / Stage.ratio, (y + 30) / Stage.ratio));
		w.createJoint(arm2);
		
		
		
		
		elbowDef.position.set(new Vec2((x - 40) / Stage.ratio, (y + 90) / Stage.ratio));
		e3 = w.createBody(elbowDef);
		e3.createShape(elbow);
		e3.setMassFromShapes();
		bodies.put(e3, 3);
		
		DistanceJointDef uarm3 = new DistanceJointDef();
		uarm3.initialize(b, e3, new Vec2((x - 15) / Stage.ratio, (y + 90) / Stage.ratio), new Vec2((x - 40) / Stage.ratio, (y + 90) / Stage.ratio));
		w.createJoint(uarm3);
		
		handDef.position.set(new Vec2((x - 65) / Stage.ratio, (y + 90) / Stage.ratio));
		ha3 = w.createBody(handDef);
		ha3.createShape(hand);
		ha3.setMassFromShapes();
		bodies.put(ha3, 5);
		
		DistanceJointDef larm3 = new DistanceJointDef();
		larm3.initialize(b, ha3, new Vec2((x - 15) / Stage.ratio, (y + 90) / Stage.ratio), new Vec2((x - 65) / Stage.ratio, (y + 90) / Stage.ratio));
		w.createJoint(larm3);
		
		DistanceJointDef arm3 = new DistanceJointDef();
		arm3.initialize(e3, ha3, new Vec2((x - 40) / Stage.ratio, (y + 90) / Stage.ratio), new Vec2((x - 65) / Stage.ratio, (y + 90) / Stage.ratio));
		w.createJoint(arm3);
		
		
		
		
		
		elbowDef.position.set(new Vec2((x + 40) / Stage.ratio, (y + 90) / Stage.ratio));
		e4 = w.createBody(elbowDef);
		e4.createShape(elbow);
		e4.setMassFromShapes();
		bodies.put(e4, 3);
		
		DistanceJointDef uarm4 = new DistanceJointDef();
		uarm4.initialize(b, e4, new Vec2((x + 15) / Stage.ratio, (y + 90) / Stage.ratio), new Vec2((x + 40) / Stage.ratio, (y + 90) / Stage.ratio));
		w.createJoint(uarm4);
		
		handDef.position.set(new Vec2((x + 65) / Stage.ratio, (y + 90) / Stage.ratio));
		ha4 = w.createBody(handDef);
		ha4.createShape(hand);
		ha4.setMassFromShapes();
		bodies.put(ha4, 5);
		
		DistanceJointDef larm4 = new DistanceJointDef();
		larm4.initialize(b, ha4, new Vec2((x + 15) / Stage.ratio, (y + 90) / Stage.ratio), new Vec2((x + 65) / Stage.ratio, (y + 90) / Stage.ratio));
		w.createJoint(larm4);
		
		DistanceJointDef arm4 = new DistanceJointDef();
		arm4.initialize(e4, ha4, new Vec2((x + 40) / Stage.ratio, (y + 90) / Stage.ratio), new Vec2((x + 65) / Stage.ratio, (y + 90) / Stage.ratio));
		w.createJoint(arm4);
		
		synchronized (stage.objs) {
			stage.objs.add(this);
		}
	}
	
	@Override
	public void destroy(Stage d) {
		// TODO
		super.destroy(d);
	}
	
	int k = 15;
	double damping = .5;
	int length = 1;
	int backwards = 0;
	
	@Override
	public void draw(Canvas c, Camera ca) {
		if (!lose) {
			Vec2 world1 = h.getPosition();
			Vec2 world2 = new Vec2(mx / Stage.ratio, my / Stage.ratio);
			Vec2 d = world2.sub(world1);
			float dlen = d.normalize();
			Vec2 springForce = d.clone();
			springForce = springForce.mul( (dlen - length) * k );
			h.applyForce(springForce, world1);
			Vec2 v1 = h.getLinearVelocityFromWorldPoint(world1);
			Vec2 v2 = new Vec2(0,0);
			Vec2 v = v2.sub(v1);
			Vec2 dampingForce = d.clone();
			dampingForce = dampingForce.mul((float) (Vec2.dot(v,d) * damping));
			h.applyForce(dampingForce, world1);
			c.drawLine(h.getWorldCenter().x * Stage.ratio, h.getWorldCenter().y * Stage.ratio, mx, my, paint);
		} else { 
			int nx = (int) (ca.getX() - (ca.getX() + (-(Stage.ratio)*h.getWorldCenter().x + ca.getScreenWidth()/2))/12);
			if (nx < ca.getX()) {
				backwards++;
				nx = ca.getX();
			} else {
				backwards = 0;
			}
			int ny = (int) (ca.getY() - (ca.getY() - (-(Stage.ratio)*h.getWorldCenter().y + ca.getScreenHeight()/2))/12);
			ny = ny < 0 ? ca.getY() : ny;
			ca.setCameraXY(nx, ny);
			
			c.drawText("Distance: " + (nx / 10), 10, 40, paint);
			
			if (backwards > 10) {
				end = true;
			}
		}
		for (Body i : bodies.keySet()) {
			Vec2 p = i.getPosition();
			c.drawCircle((((p.x * Stage.ratio) - ca.getX()) * ca.getScreenWidth()) / 800, (((p.y * Stage.ratio) + ca.getY()) * ca.getScreenHeight()) / 480, bodies.get(i), paint);
		}
		
		Vec2 p = h.getPosition();
		
		int ax = (int) ((((p.x * Stage.ratio) - ca.getX()) * ca.getScreenWidth()) / 800);
		int ay = (int) ((((p.y * Stage.ratio) + ca.getY()) * ca.getScreenHeight()) / 480);
		
		c.rotate((float) Math.toDegrees(h.getAngle()), ax, ay);
		eye_d.setBounds(ax - 20, ay - 20, ax + 20, ay + 20);
		eye_d.draw(c);
		c.restore();
	}

	float mx, my;
	public boolean lose = false;
	
	public void mouse(float x, float y) {
		mx = x;
		my = y;
	}
	
	@Override
	public boolean checkPress(int x, int y) {
		return false;
	}
	
}