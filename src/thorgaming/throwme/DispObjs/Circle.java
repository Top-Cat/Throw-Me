package thorgaming.throwme.DispObjs;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import thorgaming.throwme.Camera;
import thorgaming.throwme.DevCard;
import thorgaming.throwme.DispObj;

public class Circle extends DispObj {

	int radius = 0;
	Paint paint = new Paint();
	int ax, ay;
	
	public int getScreenX() {
		return ax;
	}
	
	public int getScreenY() {
		return ay;
	}
	
	public Circle(DevCard _d, int r, int _x, int _y, int _alpha) {
		randomiseColor();

		radius = r;
		x = _x;
		y = _y;
		alpha = _alpha;
		
		_d.objs.add(this);
	}
	
	public void setRadius(int r) {
		radius = r;
	}
	
	public void randomiseColor() {
		paint.setColor(Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
	}
	
	@Override
	public boolean checkPress(int x, int y) {
		int d = (int) Math.sqrt(Math.pow(ax - x, 2) + Math.pow(ay - y, 2));
		if (d < radius) {
			return true;
		}
		return false;
	}

	@Override
	public void draw(Canvas c, Camera ca) {
		ax = (x * ca.w) / 800;
		ay = (y * ca.h) / 480;
		
		paint.setAlpha(alpha);
		c.drawCircle(ax, ay, radius, paint);
	}
	
}