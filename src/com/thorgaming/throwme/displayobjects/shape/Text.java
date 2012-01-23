package com.thorgaming.throwme.displayobjects.shape;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.thorgaming.throwme.displayobjects.DispObj;
import com.thorgaming.throwme.drawing.Camera;
import com.thorgaming.throwme.drawing.DrawThread;

public class Text extends DispObj {

	private String text;
	private Paint paint = new Paint();
	
	public Text() {
		paint.setSubpixelText(true);
	}
	
	public Text setText(String text) {
		this.text = text;
		return this;
	}
	
	public Text setSize(float size) {
		paint.setTextSize(size);
		return this;
	}
	
	@Override
	public void draw(Canvas canvas, Camera camera) {
		canvas.drawText(text, camera.transformRelativeX(getX()), camera.transformRelativeY(getY()), paint);
		if (camera.transformRelativeX(getX()) < -300) {
			DrawThread.toRemove.add(this);
		}
	}

	@Override
	public boolean checkPress(int x, int y) {
		return false;
	}
	
}