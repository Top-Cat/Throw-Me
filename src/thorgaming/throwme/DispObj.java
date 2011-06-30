package thorgaming.throwme;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;

public abstract class DispObj {
	
	public int x = 0;
	public int y = 0;
	public int alpha = 255;
	
	protected MouseCallback c, d;
	
	public abstract void draw(Canvas c, Camera ca);

	public void setMouseUpEvent(MouseCallback _c) {
		c = _c;
	}
	
	public void setMouseDownEvent(MouseCallback _c) {
		d = _c;
	}
	
	public void setAlpha(int a) {
		alpha = a; 
	}
	
	public int getAlpha() {
		return alpha; 
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void move(int _x, int _y) {
		moveX(_x);
		moveY(_y);
	}
	
	public void moveX(int _x) {
		x = _x;
	}
	
	public void moveY(int _y) {
		y = _y;
	}
	
	public void destroy(DevCard d) {
		List<Anim> over = new ArrayList<Anim>();
		for (Anim i : d.anims) {
			if (i.getObject() == this) {
				over.add(i);
			}
		}
		d.anims.removeAll(over);
		
		synchronized (d.objs) {
			d.objs.remove(this);
		}
	}
	
	public abstract boolean checkPress(int x, int y);
	
}