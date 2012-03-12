package com.thorgaming.throwme.displayobjects.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;

import com.thorgaming.throwme.displayobjects.DispObj;
import com.thorgaming.throwme.drawing.Camera;

/**
 * @author Thomas Cheyney
 * @version 1.0
 */
public class Text extends DispObj {

	/**
	 * Text to be displayed
	 */
	private String text = "";
	/**
	 * Paint used to display the text
	 */
	protected Paint paint = new Paint();
	/**
	 * Alignment for text
	 */
	private Align align = Align.LEFT;

	public Text() {
		paint.setSubpixelText(true);
	}

	/**
	 * Set the text to be displayed by this object 
	 * 
	 * @param text Text to be displayed
	 * @return This object
	 */
	public Text setText(String text) {
		this.text = text;
		return this;
	}

	/**
	 * Get the text being displayed by this object
	 */
	public String getText() {
		return text;
	}

	/**
	 * Set the text size of this object
	 * 
	 * @param size Text size
	 * @return This object
	 */
	public Text setSize(float size) {
		paint.setTextSize(size);
		return this;
	}

	/**
	 * Set the alignment of this object 
	 * 
	 * @param align Text alignment
	 * @return This object
	 */
	public Text setAlign(Align align) {
		this.align = align;
		return this;
	}

	/**
	 * Get the paint object used by this object when displaying text
	 * @return The paint used by this object
	 */
	public Paint getPaint() {
		return paint;
	}

	@Override
	public void draw(Canvas canvas, Camera camera) {
		Rect bounds = new Rect();
		paint.getTextBounds(getText(), 0, getText().length(), bounds);
		int off = align == Align.RIGHT ? bounds.width() : align == Align.CENTER ? bounds.width() / 2 : 0;
		hitBox.set(camera.transformX(getX()) - off, camera.transformY(getY()), camera.transformX(getX()) + bounds.width() - off, camera.transformY(getY()) + (int) paint.getTextSize());

		canvas.drawText(text, camera.transformX(getX()) - off, camera.transformY(getY()) + paint.getTextSize(), paint);
	}

}