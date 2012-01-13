package com.thorgaming.throwme;

import android.view.MotionEvent;

public class MotionEventStore {

	private float x;
	private float y;
	private int action;

	public MotionEventStore(float x, float y, int action) {
		this.x = x;
		this.y = y;
		this.action = action;
	}

	public MotionEventStore(MotionEvent event) {
		x = event.getX();
		y = event.getY();
		action = event.getAction();
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public int getAction() {
		return action;
	}

}