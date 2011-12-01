package com.thorgaming.throwme.displayobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.thorgaming.throwme.Camera;

public class Rect extends DispObj {

	public Paint paint = new Paint();
	protected int actualX;
	protected int actualY;
	
	public Rect() {
		randomiseColor();
	}
	
	public int getScreenX() {
		return actualX;
	}
	
	public int getScreenY() {
		return actualY;
	}
	
	/*public void setSize(int width, int height) {
		setWidth(width);
		setHeight(height);
	}*/
	
	public void randomiseColor() {
		paint.setColor(Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
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
		canvas.drawRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), paint);
	}
	
}