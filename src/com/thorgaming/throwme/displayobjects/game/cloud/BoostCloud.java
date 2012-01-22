package com.thorgaming.throwme.displayobjects.game.cloud;

import java.io.InputStream;

import org.jbox2d.collision.Shape;
import org.jbox2d.common.Vec2;

import android.graphics.Canvas;
import android.graphics.Movie;

import com.thorgaming.throwme.Camera;
import com.thorgaming.throwme.R;
import com.thorgaming.throwme.stage;
import com.thorgaming.throwme.ThrowMe;

public class BoostCloud extends Cloud {

	private InputStream inputStream = null;
	private Movie movie;
	private long moviestart;

	public BoostCloud() {
		super(R.drawable.cloud);
		inputStream = ThrowMe.getInstance().stage.getResources().openRawResource(R.drawable.cloud);
		movie = Movie.decodeStream(inputStream);
	}

	@Override
	public void draw(Canvas canvas, Camera camera) {
		actualX = camera.transformRelativeX(getX() - 133);
		actualY = camera.transformRelativeY(getY() - 163);

		if (actualX < -266) {
			setX(getX() + 1000);
			physicsBody.setXForm(new Vec2(getX() / Stage.ratio, getY() / Stage.ratio), 0);
			moviestart = 0;
		}

		int relTime = (int) (android.os.SystemClock.uptimeMillis() - moviestart);
		if (relTime > movie.duration()) {
			relTime = 1;
		}
		movie.setTime(relTime);
		movie.draw(canvas, actualX, actualY);
	}

	@Override
	public void hit(Shape character) {
		if (moviestart == 0) {
			moviestart = android.os.SystemClock.uptimeMillis();
		}
	}

	@Override
	public void persistContact(Shape character) {
		character.getBody().applyForce(new Vec2(3, -10), character.getBody().getPosition());
	}

}