package thorgaming.throwme.DispObjs;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import thorgaming.throwme.Camera;
import thorgaming.throwme.Stage;

public class RoundRect extends Rect {

	public Paint stroke = new Paint();
	int co = 20;
	
	public RoundRect(Stage stage, int width, int height, int x, int y, int alpha, int _c) {
		super(stage, width, height, x, y, alpha);
		co = _c;
		stroke.setStyle(Style.STROKE);
	}
	
	@Override
	public boolean checkPress(int x, int y) {
		if (x > getX() && x < getX() + width && y > getY() && y < getY() + height) {
			return true;
		}
		return false;
	}

	@Override
	public void draw(Canvas c, Camera ca) {
		actualX = (getX() * ca.getScreenWidth()) / 800;
		actualY = (getY() * ca.getScreenHeight()) / 480;
		
		paint.setAlpha(getAlpha());
		RectF rectangle = new RectF(getX(), getY(), getX() + width, getY() + height);
		c.drawRoundRect(rectangle, co, co, paint);
		c.drawRoundRect(rectangle, co, co, stroke);
	}
	
}