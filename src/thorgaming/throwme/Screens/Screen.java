package thorgaming.throwme.Screens;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import thorgaming.throwme.DevCard;

public class Screen {

	DevCard d;
	Activity ac;
	
	public void onCreate(DevCard _d, Activity a) { d = _d; ac = a; };
	public void onTouch(MotionEvent event) {};
	public void onKeyDown(int keyCode, KeyEvent event) {};
	
}