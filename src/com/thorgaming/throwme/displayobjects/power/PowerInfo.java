package com.thorgaming.throwme.displayobjects.power;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;

import com.thorgaming.throwme.Camera;
import com.thorgaming.throwme.displayobjects.DispObj;
import com.thorgaming.throwme.displayobjects.game.characters.Characters;

public class PowerInfo extends DispObj {
	
	private Characters thisInfo; 
	private Paint paint = new Paint();
	
	public PowerInfo(Characters thisInfo) {
		setInfo(thisInfo);
		
		paint.setColor(Color.rgb(255, 255, 255));
		paint.setTextSize(30);
		paint.setSubpixelText(true);
		paint.setTextAlign(Align.CENTER);
	}

	public void setInfo(Characters thisInfo) {
		this.thisInfo = thisInfo;
	}
	
	@Override
	public void draw(Canvas canvas, Camera camera) {
		canvas.drawText(thisInfo.getName(), camera.transformX(getX() + 295), camera.transformY(getY()) + 30, paint);
		//canvas.drawRect(camera.transformX(getX() + 70), camera.transformY(getY() + 70), camera.transformX(getX() + 520), camera.transformY(getY() + 420), paint);
	}

	@Override
	public boolean checkPress(int x, int y) {
		return false;
	}
	
}