package com.thorgaming.throwme.displayobjects.game.cloud;

import java.util.Random;

import org.jbox2d.collision.Shape;

import android.graphics.ColorMatrix;

import com.thorgaming.throwme.R;
import com.thorgaming.throwme.displayobjects.DispRes;

public class ColouredCloud extends Cloud {

	private Random random = new Random();

	public ColouredCloud() {
		super(R.drawable.colouredcloud);

		ColorMatrix cm = new ColorMatrix();
		DispRes.adjustHue(cm, random.nextInt(360) - 180);
		setColorMatrix(cm);
	}

	@Override
	public void hit(Shape character) {
		System.out.println("launch!");
	}

}