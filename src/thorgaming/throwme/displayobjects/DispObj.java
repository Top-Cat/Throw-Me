package thorgaming.throwme.displayobjects;

import java.util.ArrayList;
import java.util.List;

import thorgaming.throwme.animation.Anim;
import thorgaming.throwme.Camera;
import thorgaming.throwme.MouseCallback;
import thorgaming.throwme.Stage;

import android.graphics.Canvas;

public abstract class DispObj {
	
	private int x = 0;
	private int y = 0;
	private int alpha = 255;
	
	protected void addToScreen(Stage stage) {
		synchronized (stage.objects) {
			stage.objects.add(this);
		}
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
	
	public DispObj setX(int x) {
		this.x = x;
		return this;
	}
	
	public DispObj setY(int y) {
		this.y = y;
		return this;
	}
	
	public void destroy(Stage stage) {
		List<Anim> over = new ArrayList<Anim>();
		for (Anim i : stage.animations) {
			if (i.getObject() == this) {
				over.add(i);
			}
		}
		stage.animations.removeAll(over);
		
		synchronized (stage.objects) {
			stage.objects.remove(this);
		}
	}
	
	public abstract boolean checkPress(int x, int y);
	
}