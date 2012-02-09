package com.thorgaming.throwme.displayobjects.shape;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.thorgaming.throwme.displayobjects.DispObj;
import com.thorgaming.throwme.drawing.Camera;

/**
 * @author Thomas Cheyney
 * @version 1.0
 */
public class Circle extends DispObj {

	private int radius = 0;
	public Paint paint = new Paint();
	private int actualX;
	private int actualY;
	private Random random = new Random();

	public Circle() {
		randomiseColor();
	}

	public int getScreenX() {
		return actualX;
	}

	public int getScreenY() {
		return actualY;
	}

	public Circle setRadius(int radius) {
		this.radius = radius;
		return this;
	}

	public void randomiseColor() {
		paint.setColor(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
	}

	@Override
	public boolean checkPress(int x, int y) {
		int distance = (int) Math.sqrt(Math.pow(actualX - x, 2) + Math.pow(actualY - y, 2));
		if (distance < radius) {
			return true;
		}
		return false;
	}

	@Override
	public void draw(Canvas canvas, Camera camera) {
		actualX = getX() * camera.getScreenWidth() / 800;
		actualY = getY() * camera.getScreenHeight() / 480;

		paint.setAlpha(getAlpha());
		canvas.drawCircle(actualX, actualY, radius, paint);
	}

}