package com.thorgaming.throwme.displayobjects.shape;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.thorgaming.throwme.displayobjects.DispObj;
import com.thorgaming.throwme.drawing.Camera;

/**
 * @author Thomas Cheyney
 * @version 1.0
 */
public class Rect extends DispObj {

	public Paint paint = new Paint();
	/**
	 * Screen X location of rectangle
	 */
	protected int actualX;
	/**
	 * Screen Y location of rectangle
	 */
	protected int actualY;

	/**
	 * Get screen Y location of rectangle
	 * 
	 * @return Screen Y
	 */
	public int getScreenX() {
		return actualX;
	}

	/**
	 * Get screen Y location of rectangle
	 * 
	 * @return Screen Y
	 */
	public int getScreenY() {
		return actualY;
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
		canvas.drawRect(actualX, actualY, camera.transformX(getX() + getWidth()), camera.transformY(getY() + getHeight()), paint);
	}

}