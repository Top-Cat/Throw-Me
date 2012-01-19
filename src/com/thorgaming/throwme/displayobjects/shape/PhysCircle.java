package com.thorgaming.throwme.displayobjects.shape;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import com.thorgaming.throwme.Stage;
import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.displayobjects.DispObj;

public class PhysCircle extends Circle {

	private Body physicsBody;
	private World world;
	private CircleDef circle;
	private Shape shape;

	public PhysCircle(int density) {
		super();

		world = ThrowMe.stage.world;

		circle = new CircleDef();
		circle.radius = 1F;
		circle.density = density;
		circle.friction = (float) 0.5;
		circle.restitution = (float) 0.6;

		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(new Vec2(getX() / Stage.ratio, getY() / Stage.ratio));

		physicsBody = world.createBody(groundBodyDef);
		shape = physicsBody.createShape(circle);
		physicsBody.setMassFromShapes();
	}

	@Override
	public Circle setRadius(int radius) {
		super.setRadius(radius);
		if (physicsBody != null) {
			if (shape != null) {
				physicsBody.destroyShape(shape);
			} else {
				System.out.println("null shape D:");
			}
			circle.radius = radius / Stage.ratio;
			shape = physicsBody.createShape(circle);
			physicsBody.setMassFromShapes();
		}
		return this;
	}

	@Override
	public DispObj setX(int x) {
		move(x, (int) (physicsBody.getPosition().y * Stage.ratio));
		return this;
	}

	@Override
	public DispObj setY(int y) {
		move((int) (physicsBody.getPosition().x * Stage.ratio), y);
		return this;
	}

	@Override
	public void move(int x, int y) {
		physicsBody.setXForm(new Vec2(x / Stage.ratio, y / Stage.ratio), 0);
	}

	public Body getBody() {
		return physicsBody;
	}

	@Override
	public void destroy() {
		super.destroy();
		world.destroyBody(physicsBody);
	}

	@Override
	public int getX() {
		if (physicsBody == null) {
			return 0;
		}
		Vec2 position = physicsBody.getPosition();
		return (int) (position.x * Stage.ratio) - ThrowMe.stage.camera.getX();
	}

	@Override
	public int getY() {
		if (physicsBody == null) {
			return 0;
		}
		Vec2 position = physicsBody.getPosition();
		return (int) (position.y * Stage.ratio) + ThrowMe.stage.camera.getY();
	}

}