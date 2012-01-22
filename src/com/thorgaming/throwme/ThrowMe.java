package com.thorgaming.throwme;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.thorgaming.throwme.screens.Main;
import com.thorgaming.throwme.screens.Screen;

public class ThrowMe extends Activity {

	public Stage stage = null;
	private static ThrowMe activity = null;
	public Screen screen;
	public boolean billingDirty = false;

	public static ThrowMe getInstance() {
		/* Android has to make this :/ */
		return activity;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		stage = (Stage) findViewById(R.id.menu);

		new Main(this, null);
	}

	public static void waiting(int n) {
		long t0, t1;
		t0 = System.currentTimeMillis();
		do {
			t1 = System.currentTimeMillis();
		} while (t1 - t0 < n);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isFinishing()) {
			stage.sendtouch(event);
		}
		return screen.onTouch(event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return screen.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();
		stage.createThread();
	}

}