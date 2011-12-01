package thorgaming.throwme.displayobjects;

import java.util.ArrayList;
import java.util.List;

import thorgaming.throwme.animation.Anim;
import thorgaming.throwme.Camera;
import thorgaming.throwme.MouseCallback;
import thorgaming.throwme.ThrowMe;

import android.graphics.Canvas;

public abstract class DispObj {
	
	private int width = 0;
	private int height = 0;
	private int x = 0;
	private int y = 0;
	private int alpha = 255;
	
	public DispObj addToScreen() {
		synchronized (ThrowMe.stage.objects) {
			ThrowMe.stage.objects.add(this);
		}
		return this;
	}
	
	private MouseCallback onMouseUpEvent, onMouseDownEvent;
	
	public abstract void draw(Canvas canvas, Camera camera);

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
	
	public DispObj setAlpha(int alpha) {
		this.alpha = alpha;
		return this;
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
	
	public DispObj setX(int x) {
		this.x = x;
		return this;
	}
	
	public DispObj setY(int y) {
		this.y = y;
		return this;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public DispObj setWidth(int width) {
		this.width = width;
		return this;
	}
	
	public DispObj setHeight(int height) {
		this.height = height;
		return this;
	}
	
	public void destroy() {
		List<Anim> over = new ArrayList<Anim>();
		for (Anim i : ThrowMe.stage.animations) {
			if (i.getObject() == this) {
				over.add(i);
			}
		}
		ThrowMe.stage.animations.removeAll(over);
		
		synchronized (ThrowMe.stage.objects) {
			ThrowMe.stage.objects.remove(this);
		}
	}
	
	public abstract boolean checkPress(int x, int y);
	
}