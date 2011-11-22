package thorgaming.throwme.screens;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import thorgaming.throwme.Stage;
import thorgaming.throwme.ThrowMe;

public class Screen {

	protected Stage stage;
	protected ThrowMe activity;
	
	public Screen(Stage stage, Activity activity, Object[] data) {
		this.stage = stage;
		stage.clear();
		stage.draw = null;
		this.activity = (ThrowMe) activity;
		this.activity.screen = this;
	}
	
	public boolean onTouch(MotionEvent event) { return false; };
	
	public boolean onKeyDown(int keyCode, KeyEvent event) { return true; };
	
}