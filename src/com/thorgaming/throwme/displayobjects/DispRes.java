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
	private ColorMatrix colorMatrix = new ColorMatrix();

	private int hitPadding = 0;

	public DispRes(int drawableId) {
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

		drawable.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
		drawable.setBounds(actualX, actualY, (getX() + getWidth()) * camera.getScreenWidth() / 800, (getY() + getHeight()) * camera.getScreenHeight() / 480);
		drawable.setAlpha(getAlpha());
		drawable.draw(canvas);
	}

	public static void adjustHue(ColorMatrix cm, float value) {
		value = cleanValue(value, 180f) / 180f * (float) Math.PI;
		if (value == 0) {
			return;
		}
		float cosVal = (float) Math.cos(value);
		float sinVal = (float) Math.sin(value);
		float lumR = 0.213f;
		float lumG = 0.715f;
		float lumB = 0.072f;
		float[] mat = new float[] {lumR + cosVal * (1 - lumR) + sinVal * -lumR, lumG + cosVal * -lumG + sinVal * -lumG, lumB + cosVal * -lumB + sinVal * (1 - lumB), 0, 0, lumR + cosVal * -lumR + sinVal * 0.143f, lumG + cosVal * (1 - lumG) + sinVal * 0.140f, lumB + cosVal * -lumB + sinVal * -0.283f, 0, 0, lumR + cosVal * -lumR + sinVal * -(1 - lumR), lumG + cosVal * -lumG + sinVal * lumG, lumB + cosVal * (1 - lumB) + sinVal * lumB, 0, 0, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f, 1f};
		cm.postConcat(new ColorMatrix(mat));
	}

	protected static float cleanValue(float p_val, float p_limit) {
		return Math.min(p_limit, Math.max(-p_limit, p_val));
	}

}