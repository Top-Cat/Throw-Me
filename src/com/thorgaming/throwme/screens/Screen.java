package com.thorgaming.throwme.screens;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.thorgaming.throwme.ThrowMe;

public class Screen {

	protected ThrowMe activity;

	public Screen(Activity activity, Object[] data) {
		ThrowMe.getInstance().stage.clear();
		ThrowMe.getInstance().stage.draw = null;
		ThrowMe.getInstance().stage.drawThread.returnMain(null);
		ThrowMe.getInstance().stage.drawThread.setPaused(false);
		this.activity = (ThrowMe) activity;
		this.activity.screen = this;
	}

	public boolean onTouch(MotionEvent event) {
		return false;
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return true;
	};

}