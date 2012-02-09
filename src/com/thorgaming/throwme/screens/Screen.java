package com.thorgaming.throwme.screens;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.thorgaming.throwme.ThrowMe;

/**
 * @author Thomas Cheyney
 * @version 1.0
 */
public class Screen {

	/**
	 * Allows things to check when a screen has been constructed and wait for it
	 */
	public static int checkCount = 0;

	/**
	 * Constructs the screen
	 * @param data Data passed by other screens
	 */
	public Screen(Object[] data) {
		ThrowMe.getInstance().stage.clear();
		ThrowMe.getInstance().stage.draw = null;
		checkCount++;
		ThrowMe.getInstance().stage.drawThread.setPaused(false);
		ThrowMe.getInstance().screen = this;
		ThrowMe.getInstance().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ThrowMe.getInstance().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			}
		});
	}

	/**
	 * Called when user touches the screen
	 * @param event MotionEvent created by android
	 * @return True if event was handled
	 */
	public boolean onTouch(MotionEvent event) {
		return false;
	};

	/**
	 * Called when a key is pressed on the phone
	 * @param keyCode A key code that represents the button pressed, from {@link KeyEvent}
	 * @param event The KeyEvent object that defines the button action.
	 * @return True if event was handled
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	};

}