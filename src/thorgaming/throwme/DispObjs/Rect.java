package thorgaming.throwme.DispObjs;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import thorgaming.throwme.Camera;
import thorgaming.throwme.DevCard;
import thorgaming.throwme.DispObj;

public class Rect extends DispObj {

	int width, height;
	public Paint paint = new Paint();
	int ax, ay;
	
	public int getScreenX() {
		return ax;
	}
	
	public int getScreenY() {
		return ay;
	}
	
	public Rect(DevCard _d, int _w, int _h, int _x, int _y, int _alpha) {
		randomiseColor();

		width = _w;
		height = _h;
		x = _x;
		y = _y;
		alpha = _alpha;
		
		_d.objs.add(this);
	}
	
	public void setSize(int _w, int _h) {
		width = _w;
		height = _h;
	}
	
	public void randomiseColor() {
		paint.setColor(Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
	}
	
	@Override
	public boolean checkPress(int x, int y) {
		if (x > this.x && x < this.x + width && y > this.y && y < this.y + height) {
			return true;
		}
		return false;
	}

	@Override
	public void draw(Canvas c, Camera ca) {
		ax = (x * ca.w) / 800;
		ay = (y * ca.h) / 480;
		
		paint.setAlpha(alpha);
		c.drawRect(x, y, x + width, y + height, paint);
	}
	
}