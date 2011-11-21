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

	InputStream is=null;
	Movie movie;
	long moviestart;
	Paint p = new Paint();
	int reps = -1;
	int treps = 0;
	double speed = 1;
	Stage stage;
	
	public DispGif(Context c, Stage stage, int did, Resources re, int width, int height, int x, int y, int alpha, int _h, int r, double s) {
		super(stage, did, re, width, height, x, y, alpha, _h);
		
		this.stage = stage;
		reps = r;
		speed = s;
		is=c.getResources().openRawResource(did);
		movie=Movie.decodeStream(is);
		
	}
	
	int pr = 0;
	
	@Override
	public void draw(Canvas c, Camera ca) {
		long now=android.os.SystemClock.uptimeMillis();
		if (moviestart == 0) {   // first time  
			moviestart = now;  
		}
		if (movie != null) {
			int relTime = (int)(((now - moviestart) * speed) % movie.duration()) ;
			if (relTime < pr) {
				treps++;
				if (treps >= reps && reps > -1) {
					destroy(stage);
				}
			}
			pr = relTime;
			movie.setTime(relTime);
			p.setAlpha(getAlpha());
			movie.draw(c, getX(), getY(), p);
		}
	}

}