package com.thorgaming.throwme.displayobjects;

import com.thorgaming.throwme.R;

import com.thorgaming.throwme.Camera;
import android.graphics.Canvas;

public class Crane extends DispRes_Rel {
	
	public Crane() {
		super(R.drawable.crane);
		setWidth(200);
		setHeight(214);
	}
	
	@Override
	public void draw(Canvas canvas, Camera camera) {
		super.draw(canvas, camera);
	}

	@Override
	public boolean checkPress(int x, int y) {
		return false;
	}
	
}