package com.thorgaming.throwme.screens;

import com.thorgaming.throwme.ThrowMe;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class Screen {

	protected ThrowMe activity;
	
	public Screen(Activity activity, Object[] data) {
		ThrowMe.stage.clear();
		ThrowMe.stage.draw = null;
		this.activity = (ThrowMe) activity;
		this.activity.screen = this;
	}
	
	public boolean onTouch(MotionEvent event) { return false; };
	
	public boolean onKeyDown(int keyCode, KeyEvent event) { return true; };
	
}