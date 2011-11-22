package thorgaming.throwme.displayobjects;

import java.text.SimpleDateFormat;
import java.util.Date;

import thorgaming.throwme.Camera;
import thorgaming.throwme.Stage;
import thorgaming.throwme.screens.Highs;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class ScoreRow extends DispObj {
	
	private String name;
	private int score, y, position;
	private Date date;
	private Paint paint = new Paint();
	private Paint lpaint = new Paint();
	private Highs screen;
	
	public ScoreRow(Stage stage, Highs screen, int position, String name, int score, Date date, int y) {
		super(stage);
		this.name = name;
		this.score = score;
		this.date = date;
		this.screen = screen;
		this.position = position;
		this.y = y;
		
		paint.setColor(Color.rgb(255, 255, 255));
		paint.setTextSize(30);
		paint.setSubpixelText(true);
		paint.setStrokeWidth(3);
		
		lpaint.setColor(Color.rgb(0, 0, 0));
	}

	@Override
	public void draw(Canvas canvas, Camera camera) {
		int scroll = screen.scroll;
		
		canvas.drawText(name, 210, scroll + y + 30, paint);
		canvas.drawText(Integer.toString(score), 210, scroll + y + 60, paint);
		canvas.drawText(Integer.toString(position), 165, scroll + y + 30, paint);
		canvas.drawText(new SimpleDateFormat("dd/MM/yyyy").format(date), 480, scroll + y + 30, paint);
		canvas.drawLine(160, scroll + y + 63, 640, scroll + y + 63, lpaint);
	}

	@Override
	public boolean checkPress(int x, int y) {
		return false;
	}
	
}