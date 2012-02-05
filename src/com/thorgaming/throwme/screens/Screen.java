package com.thorgaming.throwme.screens;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.thorgaming.throwme.ThrowMe;

public class Screen {

	protected ThrowMe activity;
	public static int checkCount = 0;

	public Screen(Activity activity, Object[] data) {
		ThrowMe.getInstance().stage.clear();
		ThrowMe.getInstance().stage.draw = null;
		checkCount++;
		ThrowMe.getInstance().stage.drawThread.setPaused(false);
		this.activity = (ThrowMe) activity;
		this.activity.screen = this;
		ThrowMe.getInstance().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ThrowMe.getInstance().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			}
		});
	}

	public boolean onTouch(MotionEvent event) {
		return false;
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return true;
	};

}