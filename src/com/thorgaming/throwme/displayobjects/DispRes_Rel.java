package com.thorgaming.throwme.displayobjects;

import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;

import com.thorgaming.throwme.Camera;

public class DispRes_Rel extends DispRes {

	public DispRes_Rel(int drawableId) {
		super(drawableId);
	}

	@Override
	public void draw(Canvas canvas, Camera camera) {
		actualX = camera.transformX(getX() - camera.getX());
		actualY = camera.transformY(getY() + camera.getY());

		drawable.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
		drawable.setBounds(actualX, actualY, camera.transformX(getX() - camera.getX() + getWidth()), camera.transformY(getY() + camera.getY() + getHeight()));
		drawable.setAlpha(getAlpha());
		drawable.draw(canvas);
	}

}