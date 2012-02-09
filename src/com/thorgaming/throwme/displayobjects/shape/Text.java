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

	private String text = "";
	protected Paint paint = new Paint();
	private Align align = Align.LEFT;

	public Text() {
		paint.setSubpixelText(true);
	}

	public Text setText(String text) {
		this.text = text;
		return this;
	}

	public String getText() {
		return text;
	}

	public Text setSize(float size) {
		paint.setTextSize(size);
		return this;
	}

	public Text setAlign(Align align) {
		this.align = align;
		return this;
	}

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