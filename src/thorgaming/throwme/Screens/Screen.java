package thorgaming.throwme.screens;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import thorgaming.throwme.ThrowMe;

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