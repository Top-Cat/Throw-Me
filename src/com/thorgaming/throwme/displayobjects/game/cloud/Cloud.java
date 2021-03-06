package com.thorgaming.throwme.displayobjects.game.cloud;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;

import android.graphics.Canvas;

import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.displayobjects.DispObj;
import com.thorgaming.throwme.displayobjects.DispRes_Rel;
import com.thorgaming.throwme.displayobjects.Sensor;
import com.thorgaming.throwme.drawing.Camera;
import com.thorgaming.throwme.drawing.Stage;

/**
 * Represents a cloud that tiles and interacts with the character
 * 
 * @author Thomas Cheyney
 * @version 1.0
 */
public class Cloud extends DispRes_Rel implements Sensor {
	
	/**
	 * Used for hit detection with the character, not drawn
	 */
	protected Body physicsBody;

	public Cloud(int drawableid) {
		super(drawableid);

		CircleDef cloud = new CircleDef();
		cloud.isSensor = true;
		cloud.radius = 33 / Stage.ratio;
		cloud.density = 0;
		cloud.localPosition = new Vec2(33 / Stage.ratio, 19 / Stage.ratio);
		cloud.userData = this;

		BodyDef headBodyDef = new BodyDef();
		headBodyDef.position.set(new Vec2(getX() / Stage.ratio, getY() / Stage.ratio));

		physicsBody = ThrowMe.getInstance().stage.world.createBody(headBodyDef);
		physicsBody.createShape(cloud);
		cloud.localPosition = new Vec2(100 / Stage.ratio, 19 / Stage.ratio);
		physicsBody.createShape(cloud);
		physicsBody.setMassFromShapes();
	}

	@Override
	public DispObj setX(int x) {
		move(x, (int) (physicsBody.getPosition().y * Stage.ratio));
		return super.setX(x);
	}

	@Override
	public DispObj setY(int y) {
		move((int) (physicsBody.getPosition().x * Stage.ratio), y);
		return super.setY(y);
	}

	@Override
	public void move(int x, int y) {
		physicsBody.setXForm(new Vec2(x / Stage.ratio, y / Stage.ratio), 0);
	}

	/**
	 * Method that tiles the cloud, separated so that implementations can change their spread
	 */
	protected void scrollMove() {
		setX(getX() + 1000);
	}

	@Override
	public void draw(Canvas canvas, Camera camera) {
		if (camera.transformRelativeX(getX() - 67) < -266) {
			scrollMove();
			physicsBody.setXForm(new Vec2(getX() / Stage.ratio, getY() / Stage.ratio), 0);
		}

		super.draw(canvas, camera);
	}

	@Override
	public void destroy() {
		super.destroy();
		ThrowMe.getInstance().stage.world.destroyBody(physicsBody);
	}

	@Override
	public void hit(Shape otherShape) {
	}

	@Override
	public void persistContact(Shape otherShape) {
	}

}