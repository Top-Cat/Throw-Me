package thorgaming.throwme.DispObjs;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import thorgaming.throwme.Camera;
import thorgaming.throwme.DevCard;

public class RoundRect extends Rect {

	//int width, height;
	public Paint stroke = new Paint();
	int co = 20;
	//int ax, ay;
	
	public RoundRect(DevCard _d, int _w, int _h, int _x, int _y, int _alpha, int _c) {
		super(_d, _w, _h, _x, _y, _alpha);
		co = _c;
		stroke.setStyle(Style.STROKE);
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
		RectF rectangle = new RectF(x, y, x + width, y + height);
		c.drawRoundRect(rectangle, co, co, paint);
		c.drawRoundRect(rectangle, co, co, stroke);
	}
	
}