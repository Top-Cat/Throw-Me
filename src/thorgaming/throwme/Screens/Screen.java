package thorgaming.throwme.Screens;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import thorgaming.throwme.Stage;
import thorgaming.throwme.ThrowMe;

public class Screen {

	Stage stage;
	ThrowMe activity;
	
	public Screen(Stage stage, Activity activity, Object[] data) {
		this.stage = stage;
		stage.clear();
		this.activity = (ThrowMe) activity;
		this.activity.screen = this;
	}
	
	public boolean onTouch(MotionEvent event) { return false; };
	
	public boolean onKeyDown(int keyCode, KeyEvent event) { return true; };
	
}