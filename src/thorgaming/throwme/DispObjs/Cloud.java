package thorgaming.throwme.DispObjs;

import java.io.InputStream;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import thorgaming.throwme.Camera;
import thorgaming.throwme.Stage;
import thorgaming.throwme.DispObj;
import thorgaming.throwme.R;

public class Cloud extends DispObj {

	InputStream inputStream = null;
	Movie movie;
	long moviestart;
	Body physicsBody;
	
	public Cloud(Context context, Stage stage, World world, int x, int y) {
		inputStream = context.getResources().openRawResource(R.drawable.cloud);
		movie = Movie.decodeStream(inputStream);
		setX(x);
		setY(y);
		
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
		
		stage.objects.add(this);
	}
	
	int actualX, actualY;
	
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
	public boolean checkPress(int x, int y) {
		return false;
	}
	
}