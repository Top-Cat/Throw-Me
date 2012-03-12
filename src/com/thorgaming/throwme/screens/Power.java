package com.thorgaming.throwme.screens;

import android.view.KeyEvent;

import com.thorgaming.throwme.R;
import com.thorgaming.throwme.callback.MouseCallback;
import com.thorgaming.throwme.displayobjects.DispRes;
import com.thorgaming.throwme.displayobjects.game.characters.Characters;
import com.thorgaming.throwme.displayobjects.power.PowerInfo;
import com.thorgaming.throwme.displayobjects.shape.RoundRect;
import com.thorgaming.throwme.drawing.RenderPriority;

/**
 * Power-up selection and purchase screen
 * 
 * @author Thomas Cheyney
 * @version 1.0
 */
public class Power extends Screen {

	/**
	 * Button to view character power-ups
	 * Currently the only power-ups
	 */
	private final DispRes charButton;
	/**
	 * Index of the current character so we can go to the previous and next
	 */
	private int enum_index = 0;
	/**
	 * Displays information on the currently selected power-up
	 */
	private PowerInfo powInfo;

	public Power(Object[] data) {
		super(data);

		new DispRes(R.drawable.back).setHitPadding(16).setWidth(48).setHeight(48).setX(736).setY(416).setMouseDownEvent(new MouseCallback() {
			@Override
			public boolean sendCallback(int x, int y) {
				new Main(new Object[] {true});
				return true;
			}
		}).addToScreen();

		RoundRect rr = (RoundRect) new RoundRect(20).setHeight(480).setWidth(600).setAlpha(50).setX(185).addToScreen(RenderPriority.High);
		rr.paint.setARGB(50, 0, 0, 0);
		rr.stroke.setARGB(150, 0, 0, 0);

		charButton = (DispRes) new DispRes(R.drawable.power_char_b).setWidth(213).setHeight(53).setX(5).setY(30).setMouseDownEvent(new MouseCallback() {
			@Override
			public boolean sendCallback(int x, int y) {
				charButton.setDrawable(R.drawable.power_char_b);
				//In future change the stage?
				return false;
			}
		}).addToScreen();
		new DispRes(R.drawable.arrow).setWidth(68).setHeight(150).setX(160).setY(165).setMouseDownEvent(new MouseCallback() {
			@Override
			public boolean sendCallback(int x, int y) {
				enum_index--;
				if (enum_index < 0) {
					enum_index = Characters.values().length - 1;
				}
				powInfo.setInfo(Characters.getFromId(enum_index));
				return false;
			}
		}).addToScreen();
		new DispRes(R.drawable.arrow).setAngle(180).setWidth(68).setHeight(150).setX(732).setY(165).setMouseDownEvent(new MouseCallback() {
			@Override
			public boolean sendCallback(int x, int y) {
				enum_index++;
				if (enum_index >= Characters.values().length) {
					enum_index = 0;
				}
				powInfo.setInfo(Characters.getFromId(enum_index));
				return false;
			}
		}).addToScreen();

		powInfo = (PowerInfo) new PowerInfo(Characters.getFromId(enum_index)).setX(185).setY(20).addToScreen();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new Main(new Object[] {true});
		}
		return super.onKeyDown(keyCode, event);
	}

}