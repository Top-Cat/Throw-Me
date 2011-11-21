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

	InputStream is = null;
	Movie movie;
	long moviestart;
	Body p_body;
	
	public Cloud(Context c, Stage stage, World world, int x, int y) {
		is=c.getResources().openRawResource(R.drawable.cloud);
		movie=Movie.decodeStream(is);
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
		
		p_body = world.createBody(headBodyDef);
		p_body.createShape(cloud);
		cloud.localPosition = new Vec2(33 / Stage.ratio, 0);
		p_body.createShape(cloud);
		p_body.setMassFromShapes();
		
		stage.objs.add(this);
	}
	
	int ax, ay;
	
	@Override
	public void draw(Canvas c, Camera ca) {
		ax = ((getX() - 200 - ca.getX()) * ca.getScreenWidth()) / 800;
		ay = ((getY() - 200 + ca.getY()) * ca.getScreenHeight()) / 480;
		
		if (ax < -266) {
			setX(getX() + 1000);
			p_body.setXForm(new Vec2((float) getX() / Stage.ratio, (float) getY() / Stage.ratio), 0);
			moviestart = 0;
		}
		
		int relTime = (int)(android.os.SystemClock.uptimeMillis() - moviestart);
		if (relTime > movie.duration()) {
			relTime = 1;
		}
		movie.setTime(relTime);
		movie.draw(c, ax, ay);
	}

	public void animate() {
		if (moviestart == 0) {
			moviestart = android.os.SystemClock.uptimeMillis();
		}
	}
	
	@Override
	public boolean checkPress(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}
	
}