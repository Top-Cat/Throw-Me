package com.thorgaming.throwme.displayobjects.scores;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.thorgaming.throwme.displayobjects.DispObj;
import com.thorgaming.throwme.screens.Highs;

import com.thorgaming.throwme.Camera;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

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
		paint.setTextSize(30);
		paint.setSubpixelText(true);
		paint.setStrokeWidth(3);
		
		lpaint.setColor(Color.rgb(0, 0, 0));
	}

	@Override
	public void draw(Canvas canvas, Camera camera) {
		int scroll = screen.scroll;
		
		canvas.drawText(name, 90, scroll + getY() + 30, paint);
		canvas.drawText(Integer.toString(score), 90, scroll + getY() + 60, paint);
		canvas.drawText(Integer.toString(position), 45, scroll + getY() + 30, paint);
		canvas.drawText(dateFormat.format(date), 430, scroll + getY() + 30, paint);
		canvas.drawLine(40, scroll + getY() + 63, 590, scroll + getY() + 63, lpaint);
	}

	@Override
	public boolean checkPress(int x, int y) {
		return false;
	}
	
}