package thorgaming.throwme.DispObjs;

import java.io.InputStream;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import thorgaming.throwme.Camera;
import thorgaming.throwme.Stage;

public class DispGif extends DispRes {

	public Paint paint = new Paint();
	
	private InputStream is = null;
	private Movie movie;
	private long movieStart;
	private int repetitions = -1;
	private int totalRepetitions = 0;
	private double speed = 1;
	private Stage stage;
	private int previousTime = 0;
	
	public DispGif(Context context, Stage stage, int drawableId, Resources resources, int width, int height, int x, int y, int alpha, int hitPadding, int repetitions, double speed) {
		super(stage, drawableId, resources, width, height, x, y, alpha, hitPadding);
		
		this.stage = stage;
		this.repetitions = repetitions;
		this.speed = speed;
		is = context.getResources().openRawResource(drawableId);
		movie = Movie.decodeStream(is);
		
	}
	
	@Override
	public void draw(Canvas canvas, Camera camera) {
		long now = android.os.SystemClock.uptimeMillis();
		if (movieStart == 0) {
			movieStart = now;  
		}
		if (movie != null) {
			int relTime = (int)(((now - movieStart) * speed) % movie.duration()) ;
			if (relTime < previousTime) {
				totalRepetitions++;
				if (totalRepetitions >= repetitions && repetitions > -1) {
					destroy(stage);
				}
			}
			previousTime = relTime;
			movie.setTime(relTime);
			paint.setAlpha(getAlpha());
			movie.draw(canvas, getX(), getY(), paint);
		}
	}

}