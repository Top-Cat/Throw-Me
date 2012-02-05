package com.thorgaming.throwme.displayobjects.shape;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.thorgaming.throwme.drawing.Camera;
import com.thorgaming.throwme.drawing.DrawThread;

public class Text_Rel extends Text {

	@Override
	public void draw(Canvas canvas, Camera camera) {
		Rect bounds = new Rect();
		paint.getTextBounds(getText(), 0, getText().length(), bounds);
		hitBox.set(camera.transformRelativeX(getX()), camera.transformRelativeY(getY()), camera.transformRelativeX(getX()) + bounds.width(), camera.transformRelativeY(getY()) + bounds.height());
		
		canvas.drawText(getText(), camera.transformRelativeX(getX()), camera.transformRelativeY(getY()) + bounds.height(), paint);
		
		if (camera.transformRelativeX(getX()) < -300) {
			DrawThread.toRemove.add(this);
		}
	}
	
}