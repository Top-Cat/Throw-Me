package thorgaming.throwme.Screens;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import thorgaming.throwme.DevCard;
import thorgaming.throwme.ThrowMe;

public class Screen {

	DevCard d;
	ThrowMe ac;
	
	public Screen(DevCard _d, Activity a, Object[] o) {
		d = _d;
		d.clear();
		ac = (ThrowMe) a;
		ac.s = this;
	}
	public boolean onTouch(MotionEvent event) { return false; };
	public boolean onKeyDown(int keyCode, KeyEvent event) { return true; };
	
}