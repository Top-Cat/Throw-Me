package com.thorgaming.throwme.displayobjects.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;

import com.thorgaming.throwme.drawing.Camera;

/**
 * @author Thomas Cheyney
 * @version 1.0
 */
public class RoundRect extends Rect {

	/**
	 * Paint used to outline the rectangle
	 */
	public Paint stroke = new Paint();
	/**
	 * How round the corners are
	 */
	private int cornerRadius = 20;

	public RoundRect(int cornerRadius) {
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
		RectF rectangle = new RectF(actualX, actualY, actualX + camera.transformX(getWidth()), actualY + camera.transformY(getHeight()));
		canvas.drawRoundRect(rectangle, cornerRadius, cornerRadius, paint);
		canvas.drawRoundRect(rectangle, cornerRadius, cornerRadius, stroke);
	}

}