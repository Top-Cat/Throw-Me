package com.thorgaming.throwme.displayobjects.scores;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.displayobjects.DispObj;
import com.thorgaming.throwme.drawing.Camera;
import com.thorgaming.throwme.screens.Highs;

/**
 * @author Thomas Cheyney
 * @version 1.0
 */
public class ScoreRow extends DispObj {

	/**
	 * Name of the user who got the score
	 */
	private String name;
	/**
	 * Score the user got
	 */
	private int score;
	/**
	 * Position in the rankings this user is
	 */
	private int position;
	/**
	 * Date the score was submitted, in a string to be displayed
	 */
	private String date;
	/**
	 * Paint used for text in the row
	 */
	private Paint paint = new Paint();
	/**
	 * Paint used for line below the row
	 */
	private Paint lpaint = new Paint();
	/**
	 * Paint used for background if the row belongs to this phone
	 */
	private Paint bgpaint = new Paint();
	/**
	 * Formatter for outputting the date
	 */
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	/**
	 * Does the score belong to this phone?
	 */
	private boolean isOwn = false;

	public ScoreRow(Highs screen, int position, String name, int score, Date date, boolean isOwn) {
		this.name = name;
		this.score = score;
		this.position = position;
		this.isOwn = isOwn;
		
		this.date = dateFormat.format(date);

		paint.setColor(Color.rgb(255, 255, 255));
		paint.setTextSize(ThrowMe.getInstance().stage.camera.transformY(30));
		paint.setSubpixelText(true);
		paint.setStrokeWidth(3);

		lpaint.setColor(Color.rgb(0, 0, 0));

		bgpaint.setColor(Color.argb(128, 255, 234, 50));

	}

	@Override
	public void draw(Canvas canvas, Camera camera) {
		int scroll = camera.transformY(((Highs) ThrowMe.getInstance().screen).scrollDraw);

		if (isOwn) {
			canvas.drawRect(camera.transformX(40), scroll + camera.transformY(getY() + 3), camera.transformX(590), scroll + camera.transformY(getY() + 63), bgpaint);
		}
		canvas.drawText(name, camera.transformX(90), scroll + camera.transformY(getY() + 30), paint);
		canvas.drawText(Integer.toString(score), camera.transformX(90), scroll + camera.transformY(getY() + 60), paint);
		canvas.drawText(Integer.toString(position), camera.transformX(45), scroll + camera.transformY(getY() + 30), paint);
		canvas.drawText(date, camera.transformX(430), scroll + camera.transformY(getY() + 30), paint);
		canvas.drawLine(camera.transformX(40), scroll + camera.transformY(getY() + 63), camera.transformX(590), scroll + camera.transformY(getY() + 63), lpaint);
	}

	@Override
	public boolean checkPress(int x, int y) {
		return false;
	}

}