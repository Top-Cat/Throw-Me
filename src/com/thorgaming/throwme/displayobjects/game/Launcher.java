package com.thorgaming.throwme.displayobjects.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.thorgaming.throwme.Camera;
import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.displayobjects.DispObj;

public class Launcher extends DispObj {
	
	private int step = 0;
	private Paint paint = new Paint();
	private Paint paintStroke = new Paint();
	
	public Launcher() {
		ThrowMe.stage.drawThread.setPhysics(false);
		paint.setColor(Color.rgb(43, 53, 255));
		paintStroke.setColor(Color.rgb(43, 53, 255));
		paintStroke.setStyle(Style.STROKE);
	}
	
	@Override
	public void draw(Canvas canvas, Camera camera) {
		step++;
		double bar = Math.sin(step) / -(Math.pow(1.12, step / 2.2));
		
		canvas.drawRect(10, (float) (600 - (bar * 400)), 50, 600, paint);
		canvas.drawRect(10, 200, 50, 600, paintStroke);
	}

	@Override
	public boolean checkPress(int x, int y) {
		return false;
	}
	
}