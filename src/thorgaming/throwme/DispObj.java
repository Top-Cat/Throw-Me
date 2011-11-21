package thorgaming.throwme;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;

public abstract class DispObj {
	
	private int x = 0;
	private int y = 0;
	private int alpha = 255;
	
	private MouseCallback onMouseUpEvent, onMouseDownEvent;
	
	public abstract void draw(Canvas c, Camera ca);

	public void setMouseUpEvent(MouseCallback event) {
		onMouseUpEvent = event;
	}
	
	public MouseCallback getMouseUpEvent() {
		return onMouseUpEvent;
	}
	
	public void setMouseDownEvent(MouseCallback event) {
		onMouseDownEvent = event;
	}
	
	public MouseCallback getMouseDownEvent() {
		return onMouseDownEvent;
	}
	
	public void setAlpha(int alpha) {
		this.alpha = alpha; 
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
	
	public void move(int x, int y) {
		setX(x);
		setY(y);
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void destroy(Stage d) {
		List<Anim> over = new ArrayList<Anim>();
		for (Anim i : d.animations) {
			if (i.getObject() == this) {
				over.add(i);
			}
		}
		d.animations.removeAll(over);
		
		synchronized (d.objects) {
			d.objects.remove(this);
		}
	}
	
	public abstract boolean checkPress(int x, int y);
	
}