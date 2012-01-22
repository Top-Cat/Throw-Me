package com.thorgaming.throwme.displayobjects;

import java.io.InputStream;

import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;

import com.thorgaming.throwme.Camera;
import com.thorgaming.throwme.DrawThread;
import com.thorgaming.throwme.ThrowMe;

public class DispGif extends DispRes {

	public Paint paint = new Paint();

	private InputStream is = null;
	private Movie movie;
	private long movieStart;
	private int repetitions = -1;
	private int totalRepetitions = 0;
	private double speed = 1;
	private int previousTime = 0;

	public DispGif(int drawableId, int repetitions, double speed) {
		super(drawableId);

		this.repetitions = repetitions;
		this.speed = speed;
		is = ThrowMe.getInstance().stage.getResources().openRawResource(drawableId);
		movie = Movie.decodeStream(is);
	}

	@Override
	public void draw(Canvas canvas, Camera camera) {
		long now = android.os.SystemClock.uptimeMillis();
		if (movieStart == 0) {
			movieStart = now;
		}
		if (movie != null) {
			int relTime = (int) ((now - movieStart) * speed % movie.duration());
			if (relTime < previousTime) {
				totalRepetitions++;
				if (totalRepetitions >= repetitions && repetitions > -1) {
					DrawThread.toRemove.add(this);
				}
			}
			previousTime = relTime;
			movie.setTime(relTime);
			paint.setAlpha(getAlpha());
			movie.draw(canvas, camera.transformX(getX()), camera.transformY(getY()), paint);
		}
	}

}