package com.thorgaming.throwme.displayobjects;

import java.util.ArrayList;
import java.util.List;

import com.thorgaming.throwme.MouseCallback;
import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.animation.Anim;

import com.thorgaming.throwme.Camera;
import com.thorgaming.throwme.RenderPriority;

import android.graphics.Canvas;

public abstract class DispObj {
	
	private int width = 0;
	private int height = 0;
	private int x = 0;
	private int y = 0;
	private int alpha = 255;
	
	public DispObj addToScreen(RenderPriority priority) {
		synchronized (ThrowMe.stage.objects) {
			ThrowMe.stage.registerForRender(priority, this);
		}
		return this;
	}
	
	public DispObj addToScreen() {
		return addToScreen(RenderPriority.Normal);
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
			ThrowMe.stage.unregisterForRender(this);
		}
	}
	
	public abstract boolean checkPress(int x, int y);
	
}