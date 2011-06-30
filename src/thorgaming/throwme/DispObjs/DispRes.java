package thorgaming.throwme.DispObjs;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import thorgaming.throwme.Camera;
import thorgaming.throwme.DevCard;
import thorgaming.throwme.DispObj;

public class DispRes extends DispObj {

	int width = 0;
	int height = 0;
	int did;
	Drawable d;
	int ax = 0, ay = 0, hit = 0;
	
	public int getScreenX() {
		return ax;
	}
	
	public int getScreenY() {
		return ay;
	}
	
	public DispRes(DevCard _d, int did, Resources re, int w, int h, int _x, int _y, int _alpha, int _h) {
		height = h;
		width = w;
		x = _x;
		y = _y;
		d = re.getDrawable(did);
		alpha = _alpha;
		this.did = did;
		hit = _h;
		
		_d.objs.add(this);
	}
	
	@Override
	public boolean checkPress(int x, int y) {
		if (ax <= x + hit && ax + width >= x - hit && ay <= y + hit && ay + height >= y - hit) {
			return true;
		}
		return false;
	}

	@Override
	public void draw(Canvas c, Camera ca) {
		ax = (x * ca.w) / 800;
		ay = (y * ca.h) / 480;
		
		d.setBounds(ax, ay, ((x + width) * ca.w) / 800, ((y + height) * ca.h) / 480);
		d.setAlpha(alpha);
		d.draw(c);
	}
	
}