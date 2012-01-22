package com.thorgaming.throwme.displayobjects;

import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;

import com.thorgaming.throwme.drawing.Camera;

public class DispRes_Rel extends DispRes {

	public DispRes_Rel(int drawableId) {
		super(drawableId);
	}

	@Override
	public void draw(Canvas canvas, Camera camera) {
		hitBox.set(camera.transformX(getX() - camera.getX()), camera.transformY(getY() + camera.getY()), camera.transformX(getX() - camera.getX() + getWidth()), camera.transformY(getY() + camera.getY() + getHeight()));

		drawable.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
		drawable.setBounds(hitBox.left, hitBox.top, camera.transformX(getX() - camera.getX() + getWidth()), camera.transformY(getY() + camera.getY() + getHeight()));
		drawable.setAlpha(getAlpha());
		drawable.draw(canvas);
	}

}