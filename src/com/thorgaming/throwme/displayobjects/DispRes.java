package com.thorgaming.throwme.displayobjects;

import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;

import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.drawing.Camera;

/**
 * @author Thomas Cheyney
 * @version 1.0
 */
public class DispRes extends DispObj {

	/**
	 * Drawable to be drawn by this object
	 */
	protected Drawable drawable;
	/**
	 * ColorMatrix to adjust colour of this object
	 */
	protected ColorMatrix colorMatrix = new ColorMatrix();
	/**
	 * Rotation of this object, around it's center
	 */
	private float angle = 0;

	public DispRes(int drawableId) {
		setDrawable(drawableId);
	}

	/**
	 * Change the drawable being displayed
	 * 
	 * @param drawableId ID of the new drawable
	 */
	public void setDrawable(int drawableId) {
		drawable = ThrowMe.getInstance().stage.getResources().getDrawable(drawableId);
	}

	/**
	 * Get x position on the screen of this object
	 * 
	 * @return Screen X position
	 */
	public int getScreenX() {
		return hitBox.left;
	}

	/**
	 * Get y position on the screen of this object
	 * 
	 * @return Screen Y position
	 */
	public int getScreenY() {
		return hitBox.top;
	}

	/**
	 * Set the color matrix being used to adjust the colour of this object
	 * 
	 * @param colorMatrix
	 */
	public void setColorMatrix(ColorMatrix colorMatrix) {
		this.colorMatrix = colorMatrix;
	}

	/**
	 * Set rotation of this object, around it's center
	 * 
	 * @param angle Rotation of this object, around it's center
	 * @return This object
	 */
	public DispRes setAngle(float angle) {
		this.angle = angle;
		return this;
	}

	@Override
	public void draw(Canvas canvas, Camera camera) {
		hitBox.set(camera.transformX(getX()), camera.transformY(getY()), camera.transformX(getX() + getWidth()), camera.transformY(getY() + getHeight()));
		
		canvas.save();
		canvas.rotate(angle, hitBox.exactCenterX(), hitBox.exactCenterY());
		drawable.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
		drawable.setBounds(hitBox);
		drawable.setAlpha(getAlpha());
		drawable.draw(canvas);
		canvas.restore();
	}
	
}