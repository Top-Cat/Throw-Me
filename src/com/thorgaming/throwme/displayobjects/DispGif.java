package com.thorgaming.throwme.displayobjects;

import java.io.InputStream;

import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;

import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.drawing.Camera;
import com.thorgaming.throwme.drawing.DrawThread;

/**
 * @author Thomas Cheyney
 * @version 1.0
 */
public class DispGif extends DispRes {

	/**
	 * Paint used to set transparency
	 */
	public Paint paint = new Paint();
	/**
	 * The gif's frames to be displayed
	 */
	private Movie movie;
	/**
	 * Time the first frame was displayed, used to calculate which frame to display at a certain time
	 */
	private long movieStart;
	/**
	 * How many times to repeat the sequence, -1 for no limit
	 */
	private int repetitions = -1;
	/**
	 * Repetitions so far, used to check against max repetitions
	 */
	private int totalRepetitions = 0;
	/**
	 * How fast to go through frames
	 */
	private double speed = 1;
	/**
	 * Used to check if we're on a new repetition
	 */
	private int previousTime = 0;

	public DispGif(int drawableId, int repetitions, double speed) {
		super(drawableId);

		this.repetitions = repetitions;
		this.speed = speed;
		InputStream is = ThrowMe.getInstance().stage.getResources().openRawResource(drawableId);
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