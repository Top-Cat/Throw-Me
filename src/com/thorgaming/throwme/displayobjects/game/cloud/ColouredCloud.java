package com.thorgaming.throwme.displayobjects.game.cloud;

import java.util.Random;

import org.jbox2d.collision.Shape;

import android.graphics.Color;
import android.graphics.ColorMatrix;

import com.thorgaming.throwme.R;
import com.thorgaming.throwme.RenderPriority;
import com.thorgaming.throwme.displayobjects.game.Character;
import com.thorgaming.throwme.displayobjects.game.Launcher;

public class ColouredCloud extends Cloud {

	private Random random = new Random();
	private boolean hit = false;

	public ColouredCloud() {
		super(R.drawable.colouredcloud);
		randomiseColor();
	}
	
	@Override
	protected void scrollMove() {
		setX(getX() + 2000);
		randomiseColor();
	}
	
	private void randomiseColor() {
		int color = Color.HSVToColor(new float[] {random.nextInt(360), 1, 0.75f + random.nextFloat() / 4});
		float r = Color.red(color) / 255f;
		float g = Color.green(color) / 255f;
		float b = Color.blue(color) / 255f;
		ColorMatrix cm = new ColorMatrix(new float[]
			{	r, r, r, 0, 0,
				g, g, g, 0, 0,
				b, b, b, 0, 0,
				0, 0, 0, 1, 0
		});
		setColorMatrix(cm);
	}

	@Override
	public void hit(Shape character) {
		if (((Character) character.getUserData()).bodyHead.getShapeList() == character && !hit) {
			hit = true;
			new Launcher((Character) character.getUserData(), 0.7f).addToScreen(RenderPriority.Lowest);
		}
	}

}