package thorgaming.throwme.DispObjs;

import thorgaming.throwme.Camera;
import thorgaming.throwme.DevCard;
import android.content.res.Resources;
import android.graphics.Canvas;

public class DispRes_Rel extends DispRes {

	public DispRes_Rel(DevCard _d, int did, Resources re, int w, int h, int _x, int _y, int _alpha, int _h) {
		super(_d, did, re, w, h, _x, _y, _alpha, _h);
	}
	
	@Override
	public void draw(Canvas c, Camera ca) {
		ax = ((x - ca.x) * ca.w) / 800;
		ay = ((y + ca.y) * ca.h) / 480;
		
		d.setBounds(ax, ay, (((x - ca.x) + width) * ca.w) / 800, (((y + ca.y) + height) * ca.h) / 480);
		d.setAlpha(alpha);
		d.draw(c);
	}
	
}