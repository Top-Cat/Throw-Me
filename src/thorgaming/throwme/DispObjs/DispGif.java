package thorgaming.throwme.DispObjs;

import java.io.InputStream;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import thorgaming.throwme.Camera;
import thorgaming.throwme.DevCard;

public class DispGif extends DispRes {

	InputStream is=null;
	Movie movie;
	long moviestart;
	Paint p = new Paint();
	int reps = -1;
	int treps = 0;
	double speed = 1;
	DevCard d;
	
	public DispGif(Context c, DevCard _d, int did, Resources re, int w, int h, int _x, int _y, int _alpha, int _h, int r, double s) {
		super(_d, did, re, w, h, _x, _y, _alpha, _h);
		
		d = _d;
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
		int relTime = (int)(((now - moviestart) * speed) % movie.duration()) ;
		if (relTime < pr) {
			treps++;
			if (treps >= reps && reps > -1) {
				destroy(d);
			}
		}
		pr = relTime;
		movie.setTime(relTime);
		p.setAlpha(alpha);
		movie.draw(c, x, y, p);
	}

}