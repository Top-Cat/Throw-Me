package com.thorgaming.throwme.displayobjects.scores;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.thorgaming.throwme.drawing.Camera;
import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.displayobjects.DispObj;
import com.thorgaming.throwme.screens.Highs;

public class ScoreRow extends DispObj {

	private String name;
	private int score, position;
	private Date date;
	private Paint paint = new Paint();
	private Paint lpaint = new Paint();
	private Highs screen;
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	public ScoreRow(Highs screen, int position, String name, int score, Date date, int y) {
		this.name = name;
		this.score = score;
		this.date = date;
		this.screen = screen;
		this.position = position;
		setY(y);

		paint.setColor(Color.rgb(255, 255, 255));
		paint.setTextSize(ThrowMe.getInstance().stage.camera.transformY(30));
		paint.setSubpixelText(true);
		paint.setStrokeWidth(3);

		lpaint.setColor(Color.rgb(0, 0, 0));
	}

	@Override
	public void draw(Canvas canvas, Camera camera) {
		int scroll = camera.transformY(screen.scrollDraw);

		canvas.drawText(name, camera.transformX(90), scroll + camera.transformY(getY() + 30), paint);
		canvas.drawText(Integer.toString(score), camera.transformX(90), scroll + camera.transformY(getY() + 60), paint);
		canvas.drawText(Integer.toString(position), camera.transformX(45), scroll + camera.transformY(getY() + 30), paint);
		canvas.drawText(dateFormat.format(date), camera.transformX(430), scroll + camera.transformY(getY() + 30), paint);
		canvas.drawLine(camera.transformX(40), scroll + camera.transformY(getY() + 63), camera.transformX(590), scroll + camera.transformY(getY() + 63), lpaint);
	}

	@Override
	public boolean checkPress(int x, int y) {
		return false;
	}

}