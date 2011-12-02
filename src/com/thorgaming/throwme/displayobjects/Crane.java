package com.thorgaming.throwme.displayobjects;

import java.util.Random;

import com.thorgaming.throwme.R;
import com.thorgaming.throwme.ThrowMe;

import com.thorgaming.throwme.Camera;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

public class Crane extends DispRes_Rel {
	
	private Drawable notes;
	private Random random = new Random();
	private float time = random.nextInt(360);
	private float speed = random.nextFloat() * 2 + 1;
	
	public Crane() {
		super(R.drawable.crane);
		setWidth(200);
		setHeight(214);
		notes = ThrowMe.stage.getResources().getDrawable(R.drawable.swingingnotes);
	}
	
	@Override
	public DispObj setX(int x) {
		return super.setX(x);
	}
	
	@Override
	public DispObj setY(int y) {
		return super.setY(y);
	}
	
	@Override
	public void draw(Canvas canvas, Camera camera) {
		super.draw(canvas, camera);
		canvas.save();
		canvas.rotate((float) (40 * Math.sin(Math.toRadians(time))), camera.transformRelativeX(getX()) + 195, camera.transformRelativeY(getY()) + 41);
		time += speed;
		if (time > 360) {
			time = time % 360;
		}
		notes.setBounds(camera.transformRelativeX(getX()) + 173, camera.transformRelativeY(getY()) + 30, camera.transformRelativeX(getX()) + 231, camera.transformRelativeY(getY()) + 170);
		notes.draw(canvas);
		canvas.restore();
	}

	@Override
	public boolean checkPress(int x, int y) {
		return false;
	}
	
}