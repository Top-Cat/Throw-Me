package thorgaming.throwme;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class score extends DispObj {
	
	private String name;
	private int score, y, scroll, pos;
	private Date date;
	Paint paint = new Paint();
	Paint lpaint = new Paint();
	
	public score(DevCard dc, int p, String n, int s, Date d, int _y) {
		name = n;
		score = s;
		date = d;
		pos = p;
		y = _y;
		
		synchronized (dc.objs) {
			dc.objs.add(this);
		}
		
		paint.setColor(Color.rgb(255, 255, 255));
		paint.setTextSize(30);
		paint.setSubpixelText(true);
		paint.setStrokeWidth(3);
		
		lpaint.setColor(Color.rgb(0, 0, 0));
	}
	
	public void setys(int s) {
		scroll = s;
	}

	@Override
	public void draw(Canvas c, Camera ca) {
		c.drawText(name, 210, scroll + y + 30, paint);
		c.drawText(Integer.toString(score), 210, scroll + y + 60, paint);
		c.drawText(Integer.toString(pos), 165, scroll + y + 30, paint);
		c.drawText(new SimpleDateFormat("dd/MM/yyyy").format(date), 480, scroll + y + 30, paint);
		c.drawLine(160, scroll + y + 63, 640, scroll + y + 63, lpaint);
	}

	@Override
	public boolean checkPress(int x, int y) {
		return false;
	}
	
}