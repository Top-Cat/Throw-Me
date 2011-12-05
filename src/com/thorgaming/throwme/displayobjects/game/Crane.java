package com.thorgaming.throwme.displayobjects.game;

import java.util.Random;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import com.thorgaming.throwme.R;
import com.thorgaming.throwme.Stage;
import com.thorgaming.throwme.ThrowMe;

import com.thorgaming.throwme.Camera;
import com.thorgaming.throwme.displayobjects.DispRes_Rel;
import com.thorgaming.throwme.displayobjects.Sensor;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

public class Crane extends DispRes_Rel implements Sensor {
	
	private Drawable notes;
	private Random random = new Random();
	private float time = random.nextInt(360);
	private float speed = random.nextFloat() * 2 + 1;
	private World world;
	protected Body physicsBody;
	
	public Crane() {
		super(R.drawable.crane);
		setWidth(200);
		setHeight(292);
		
		this.world = ThrowMe.stage.world;
		
		CircleDef wreckingBall = new CircleDef();
		wreckingBall.isSensor = true;
		wreckingBall.radius = (float) 40 / Stage.ratio;
		wreckingBall.density = (float) 0;
		wreckingBall.userData = this; 	
		
		BodyDef wreckingBallBodyDef = new BodyDef();
		wreckingBallBodyDef.position.set(new Vec2(getX() / Stage.ratio, getY() / Stage.ratio));
		
		physicsBody = world.createBody(wreckingBallBodyDef);
		physicsBody.createShape(wreckingBall);
		physicsBody.setMassFromShapes();
		
		notes = ThrowMe.stage.getResources().getDrawable(R.drawable.swingingnotes);
	}
	
	@Override
	public void draw(Canvas canvas, Camera camera) {
		super.draw(canvas, camera);
		canvas.save();
		float angle = (float) (40 * Math.sin(Math.toRadians(time)));
		canvas.rotate(angle , camera.transformRelativeX(getX()) + 195, camera.transformRelativeY(getY()) + 41);
		
		physicsBody.setXForm(new Vec2((float) (getX() + 207 - 97*Math.sin(Math.toRadians(angle)) - 9*Math.cos(Math.toRadians(angle))) / Stage.ratio, (float) (getY() + 41 + 97*Math.cos(Math.toRadians(angle)) + 9*Math.sin(Math.toRadians(angle))) / Stage.ratio), 0);
		
		time += speed;
		if (time > 360) {
			time = time % 360;
		}
		notes.setBounds(camera.transformRelativeX(getX()) + 173, camera.transformRelativeY(getY()) + 30, camera.transformRelativeX(getX()) + 231, camera.transformRelativeY(getY()) + 170);
		notes.draw(canvas);
		canvas.restore();
	}

	@Override
	public boolean checkPress(int x, int y) {
		return false;
	}

	@Override
	public void hit(Shape otherShape) {
		if (((Character) otherShape.getUserData()).bodyHead.getShapeList() == otherShape) {
			System.out.println("eye hit!");
		}
	}

	@Override
	public void persistContact(Shape otherShape) {
		
	}
	
}