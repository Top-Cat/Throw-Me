package com.thorgaming.throwme.displayobjects;

import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;

import com.thorgaming.throwme.Camera;
import com.thorgaming.throwme.ThrowMe;

public class DispRes extends DispObj {

	protected Drawable drawable;
	protected int actualX = 0;
	protected int actualY = 0;
	protected ColorMatrix colorMatrix = new ColorMatrix();

	private int hitPadding = 0;
	private float angle = 0;

	public DispRes(int drawableId) {
		setDrawable(drawableId);
	}
	
	public void setDrawable(int drawableId) {
		drawable = ThrowMe.stage.getResources().getDrawable(drawableId);
	}

	public int getScreenX() {
		return actualX;
	}

	public int getScreenY() {
		return actualY;
	}

	public int getHitPadding() {
		return hitPadding;
	}

	public DispRes setHitPadding(int hitPadding) {
		this.hitPadding = hitPadding;
		return this;
	}

	public void setColorMatrix(ColorMatrix colorMatrix) {
		this.colorMatrix = colorMatrix;
	}

	public DispRes setAngle(float angle) {
		this.angle = angle;
		return this;
	}
	
	@Override
	public boolean checkPress(int x, int y) {
		if (actualX <= x + hitPadding && actualX + getWidth() >= x - hitPadding && actualY <= y + hitPadding && actualY + getHeight() >= y - hitPadding) {
			return true;
		}
		return false;
	}

	@Override
	public void draw(Canvas canvas, Camera camera) {
		actualX = camera.transformX(getX());
		actualY = camera.transformY(getY());

		canvas.save();
		canvas.rotate(angle, actualX + getWidth() / 2, actualY + getHeight() / 2);
		drawable.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
		drawable.setBounds(actualX, actualY, (getX() + getWidth()) * camera.getScreenWidth() / 800, (getY() + getHeight()) * camera.getScreenHeight() / 480);
		drawable.setAlpha(getAlpha());
		drawable.draw(canvas);
		canvas.restore();
	}

}