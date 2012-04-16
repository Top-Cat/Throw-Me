package com.thorgaming.throwme.screens;

import java.util.UUID;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.view.KeyEvent;

import com.thorgaming.throwme.R;
import com.thorgaming.throwme.ScoreSubmitThread;
import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.callback.MouseCallback;
import com.thorgaming.throwme.displayobjects.DispGif;
import com.thorgaming.throwme.displayobjects.DispObj;
import com.thorgaming.throwme.displayobjects.DispRes;
import com.thorgaming.throwme.displayobjects.shape.RoundRect;
import com.thorgaming.throwme.displayobjects.shape.Text;
import com.thorgaming.throwme.drawing.DrawThread;
import com.thorgaming.throwme.drawing.RenderPriority;

/**
 * @author Thomas Cheyney
 * @version 1.0
 */
public class Submit extends Screen {

	/**
	 * Keyboard layout used to make text objects
	 */
	private String keyboard = "QWERTYUIOP ASDFGHJKL ZXCVBNM";
	/**
	 * Stores the currently entered name
	 */
	private String name = "";
	/**
	 * Object used to display currently entered name
	 */
	private Text nameText;
	/**
	 * Object used to show spinning ticker while trying to submit the score
	 */
	private DispObj ajaxGif;
	/**
	 * Submit button, when pressed score will be submitted
	 */
	private final DispObj submitRes;

	public Submit(Object[] data) {
		super(data);

		DrawThread.resetGradient();

		RoundRect rr = (RoundRect) new RoundRect(20).setHeight(80).setWidth(750).setAlpha(50).setX(20).setY(100).addToScreen(RenderPriority.High);
		rr.paint.setARGB(50, 0, 0, 0);
		rr.stroke.setARGB(150, 0, 0, 0);

		final int score = data != null && data[0] != null ? (Integer) data[0] : 0;
		final SharedPreferences settings = ThrowMe.getInstance().getSharedPreferences("throwmedevicekey", 0);
		new Text().setText("Score: " + score).setSize(40).setX(30).setY(20).addToScreen();

		nameText = (Text) new Text().setText(settings.getString("userName", "")).setSize(40).setX(30).setY(115).addToScreen();
		nameText.getPaint().setARGB(255, 255, 255, 255);
		
		name = settings.getString("userName", "");

		submitRes = new DispRes(R.drawable.submit).setWidth(222).setHeight(60).setX(350).setY(20).setMouseDownEvent(new MouseCallback() {
			@Override
			public boolean sendCallback(int x, int y) {
				if (nameText.getText().length() > 0) {
					submitRes.setAlpha(0);
					ajaxGif.setAlpha(255);

					String deviceid = settings.getString("deviceid", UUID.randomUUID().toString());
					Editor editor = settings.edit();
					editor.putString("deviceid", deviceid);
					editor.putString("userName", nameText.getText());
					editor.commit();

					new ScoreSubmitThread(nameText.getText(), score, deviceid).start();
				}
				return false;
			}
		}).addToScreen();
		ajaxGif = new DispGif(R.drawable.ajax2, -1, 1).setX(450).setY(35).setAlpha(0).addToScreen();
		new DispRes(R.drawable.cancel).setWidth(197).setHeight(60).setX(590).setY(20).setMouseDownEvent(new MouseCallback() {
			@Override
			public boolean sendCallback(int x, int y) {
				ThrowMe.getInstance().stage.drawThread.runOnUi(new Runnable() {
					@Override
					public void run() {
						final Activity act = ThrowMe.getInstance();
						int check = Screen.checkCount;
						act.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								new Highs(null);
							}
						});
						while (check == Screen.checkCount) {
						}
					}
				});
				return false;
			}
		}).addToScreen();

		byte line = 1;
		byte pos = 0;
		short[] offset = {30, 70, 120};
		for (final char key : keyboard.toCharArray()) {
			if (key == ' ') {
				line++;
				pos = 0;
			} else {
				new Text().setText("" + key).setSize(50).setAlign(Align.CENTER).setMouseDownEvent(new MouseCallback() {
					@Override
					public boolean sendCallback(int x, int y) {
						if (name.length() < 30 && nameText.getHitBox().width() < 700) {
							name += key;
						}
						nameText.setText(name);
						return false;
					}
				}).setX(offset[line - 1] + 80 * pos++).setY(130 + 70 * line).setHitPadding(new Rect(0, 0, 40, 22)).addToScreen();
			}
		}
		new Text().setText(String.valueOf(Character.toChars(8592))).setSize(50).setX(720).setY(340).setMouseDownEvent(new MouseCallback() {
			@Override
			public boolean sendCallback(int x, int y) {
				if (name.length() > 0) {
					name = name.substring(0, name.length() - 1);
				}
				nameText.setText(name);
				return false;
			}
		}).setHitPadding(new Rect(0, 0, 70, 20)).addToScreen();
		new Text().setText("_____").setSize(50).setX(350).setY(410).setMouseDownEvent(new MouseCallback() {
			@Override
			public boolean sendCallback(int x, int y) {
				if (name.length() < 30 && nameText.getHitBox().width() < 700) {
					name += " ";
				}
				nameText.setText(name);
				return false;
			}
		}).setHitPadding(new Rect(0, 0, 70, 20)).addToScreen();
	}

	/**
	 * Called on a submission failure to reset the screen
	 */
	public void failSubmit() {
		ajaxGif.setAlpha(0);
		submitRes.setAlpha(255);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new Highs(null);
		}
		return super.onKeyDown(keyCode, event);
	}

}