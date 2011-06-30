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
import thorgaming.throwme.DevCard;
import thorgaming.throwme.DispObj;
import thorgaming.throwme.R;

public class Cloud extends DispObj {

	InputStream is=null;
	Movie movie;
	long moviestart;
	Body p_body;
	
	public Cloud(Context c, DevCard _d, World w, int _x, int _y) {
		is=c.getResources().openRawResource(R.drawable.cloud);
		movie=Movie.decodeStream(is);
		x = _x;
		y = _y;
		
		CircleDef cloud = new CircleDef();
		cloud.isSensor = true;
		cloud.radius = (float) 38 / DevCard.ratio;
		//cloud.filter.groupIndex = -1;
		cloud.density = (float) 0;
		cloud.localPosition = new Vec2(-33 / DevCard.ratio, 0);
		cloud.userData = this;
		
		BodyDef headBodyDef = new BodyDef();
        headBodyDef.position.set(new Vec2(_x / DevCard.ratio, _y / DevCard.ratio));
        
        p_body = w.createBody(headBodyDef);
        p_body.createShape(cloud);
        cloud.localPosition = new Vec2(33 / DevCard.ratio, 0);
        p_body.createShape(cloud);
        p_body.setMassFromShapes();
		
		_d.objs.add(this);
	}
	
	int ax, ay;
	
	@Override
	public void draw(Canvas c, Camera ca) {
		ax = ((x - 200 - ca.x) * ca.w) / 800;
		ay = ((y - 200 + ca.y) * ca.h) / 480;
		
		if (ax < -266) {
			//x += 935;
			x += 1000;
			p_body.setXForm(new Vec2((float) x / DevCard.ratio, (float) y / DevCard.ratio), 0);
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