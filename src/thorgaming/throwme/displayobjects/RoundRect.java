package thorgaming.throwme.displayobjects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import thorgaming.throwme.Camera;

public class RoundRect extends Rect {

	public Paint stroke = new Paint();
	private int cornerRadius = 20;
	
	public RoundRect(int cornerRadius) {
		super();
		this.cornerRadius = cornerRadius;
		stroke.setStyle(Style.STROKE);
	}
	
	@Override
	public boolean checkPress(int x, int y) {
		if (x > getX() && x < getX() + getWidth() && y > getY() && y < getY() + getHeight()) {
			return true;
		}
		return false;
	}

	@Override
	public void draw(Canvas canvas, Camera camera) {
		actualX = camera.transformX(getX());
		actualY = camera.transformY(getY());
		
		paint.setAlpha(getAlpha());
		RectF rectangle = new RectF(getX(), getY(), getX() + getWidth(), getY() + getHeight());
		canvas.drawRoundRect(rectangle, cornerRadius, cornerRadius, paint);
		canvas.drawRoundRect(rectangle, cornerRadius, cornerRadius, stroke);
	}
	
}