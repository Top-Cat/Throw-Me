package com.thorgaming.throwme.displayobjects.cloud;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import android.graphics.Canvas;

import com.thorgaming.throwme.Camera;
import com.thorgaming.throwme.Stage;
import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.displayobjects.DispObj;
import com.thorgaming.throwme.displayobjects.DispRes_Rel;
import com.thorgaming.throwme.displayobjects.Sensor;

public class Cloud extends DispRes_Rel implements Sensor {
	
	private World world;
	protected Body physicsBody;
	
	public Cloud(int drawableid) {
		super(drawableid);
		
		this.world = ThrowMe.stage.world;
		
		CircleDef cloud = new CircleDef();
		cloud.isSensor = true;
		cloud.radius = (float) 38 / Stage.ratio;
		cloud.density = (float) 0;
		cloud.localPosition = new Vec2(-33 / Stage.ratio, 0);
		cloud.userData = this;
		
		BodyDef headBodyDef = new BodyDef();
		headBodyDef.position.set(new Vec2(getX() / Stage.ratio, getY() / Stage.ratio));
		
		physicsBody = world.createBody(headBodyDef);
		physicsBody.createShape(cloud);
		cloud.localPosition = new Vec2(33 / Stage.ratio, 0);
		physicsBody.createShape(cloud);
		physicsBody.setMassFromShapes();
	}
	
	@Override
	public DispObj setX(int x) {
		move(x, (int) (physicsBody.getWorldCenter().y * Stage.ratio));
		return super.setX(x);
	}
	
	@Override
	public DispObj setY(int y) {
		move((int) (physicsBody.getWorldCenter().x * Stage.ratio), y);
		return super.setY(y);
	}
	
	@Override
	public void move(int x, int y) {
		physicsBody.setXForm(new Vec2((float) x / Stage.ratio, (float) y / Stage.ratio), 0);
	}
	
	@Override
	public void draw(Canvas canvas, Camera camera) {
		if (camera.transformRelativeX(getX() - 67) < -266) {
			setX(getX() + 1000);
			physicsBody.setXForm(new Vec2((float) getX() / Stage.ratio, (float) getY() / Stage.ratio), 0);
		}
		
		super.draw(canvas, camera);
	}
	
	@Override
	public void destroy() {
		super.destroy();
		world.destroyBody(physicsBody);
	}

	@Override
	public void hit(Shape otherShape) {
		
	}

	@Override
	public void persistContact(Shape otherShape) {
		
	}
	
}