package thorgaming.throwme.displayobjects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import thorgaming.throwme.Camera;
import thorgaming.throwme.Stage;

public class RoundRect extends Rect {

	public Paint stroke = new Paint();
	private int cornerRadius = 20;
	
	public RoundRect(Stage stage, int width, int height, int x, int y, int alpha, int cornerRadius) {
		super(stage, width, height, x, y, alpha);
		this.cornerRadius = cornerRadius;
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
	public void draw(Canvas canvas, Camera camera) {
		actualX = (getX() * camera.getScreenWidth()) / 800;
		actualY = (getY() * camera.getScreenHeight()) / 480;
		
		paint.setAlpha(getAlpha());
		RectF rectangle = new RectF(getX(), getY(), getX() + width, getY() + height);
		canvas.drawRoundRect(rectangle, cornerRadius, cornerRadius, paint);
		canvas.drawRoundRect(rectangle, cornerRadius, cornerRadius, stroke);
	}
	
}