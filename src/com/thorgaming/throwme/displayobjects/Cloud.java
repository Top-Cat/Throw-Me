package com.thorgaming.throwme.displayobjects;

import java.io.InputStream;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import com.thorgaming.throwme.Stage;
import com.thorgaming.throwme.ThrowMe;

import android.graphics.Canvas;
import android.graphics.Movie;
import com.thorgaming.throwme.Camera;
import com.thorgaming.throwme.R;

public class Cloud extends DispObj {

	private InputStream inputStream = null;
	private Movie movie;
	private long moviestart;
	private World world;
	private Body physicsBody;
	private int actualX;
	private int actualY;
	
	public Cloud() {
		inputStream = ThrowMe.stage.getResources().openRawResource(R.drawable.cloud);
		movie = Movie.decodeStream(inputStream);
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
	public void draw(Canvas canvas, Camera camera) {
		actualX = ((getX() - 200 - camera.getX()) * camera.getScreenWidth()) / 800;
		actualY = ((getY() - 200 + camera.getY()) * camera.getScreenHeight()) / 480;
		
		if (actualX < -266) {
			setX(getX() + 1000);
			physicsBody.setXForm(new Vec2((float) getX() / Stage.ratio, (float) getY() / Stage.ratio), 0);
			moviestart = 0;
		}
		
		int relTime = (int)(android.os.SystemClock.uptimeMillis() - moviestart);
		if (relTime > movie.duration()) {
			relTime = 1;
		}
		movie.setTime(relTime);
		movie.draw(canvas, actualX, actualY);
	}

	public void animate() {
		if (moviestart == 0) {
			moviestart = android.os.SystemClock.uptimeMillis();
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
		world.destroyBody(physicsBody);
	}
	
	@Override
	public boolean checkPress(int x, int y) {
		return false;
	}
	
}