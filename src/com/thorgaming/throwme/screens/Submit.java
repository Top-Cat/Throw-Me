package com.thorgaming.throwme.screens;

import com.thorgaming.throwme.callback.MouseCallback;
import com.thorgaming.throwme.displayobjects.shape.RoundRect;
import com.thorgaming.throwme.displayobjects.shape.Text;
import com.thorgaming.throwme.drawing.RenderPriority;

import android.app.Activity;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.view.KeyEvent;

public class Submit extends Screen {
	
	private String keyboard = "QWERTYUIOP ASDFGHJKL ZXCVBNM";
	private String name = "";
	private Text nameText;
	
	public Submit(Activity activity, Object[] data) {
		super(activity, data);
		
		RoundRect rr = (RoundRect) new RoundRect(20).setHeight(80).setWidth(550).setAlpha(50).setX(20).setY(100).addToScreen(RenderPriority.High);
		rr.paint.setARGB(50, 0, 0, 0);
		rr.stroke.setARGB(150, 0, 0, 0);
		
		new Text().setText("Score: 000000").setSize(40).setX(30).setY(20).addToScreen();
		
		nameText = (Text) new Text().setSize(40).setX(30).setY(115).addToScreen();
		nameText.getPaint().setARGB(255, 255, 255, 255);
		
		byte line = 1;
		byte pos = 0;
		short[] offset = {30, 70, 120};
		for (final char key : keyboard.toCharArray()) {
			if (key == ' ') {
				line++;
				pos = 0;
			} else {
				new Text().setText(""+key).setSize(50).setAlign(Align.CENTER).setMouseDownEvent(new MouseCallback() {
					@Override
					public boolean sendCallback(int x, int y) {
						if (name.length() < 30 && nameText.getHitBox().width() < 500) {
							name += key;
						}
						nameText.setText(name);
						return false;
					}
				}).setX(offset[line - 1]  + 80 * pos++).setY(130 + 70 * line).setHitPadding(new Rect(0, 0, 40, 22)).addToScreen();
			}
		}
		new Text().setText("←").setSize(50).setX(720).setY(340).setMouseDownEvent(new MouseCallback() {
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
				if (name.length() < 30 && nameText.getHitBox().width() < 500) {
					name += " ";
				}
				nameText.setText(name);
				return false;
			}
		}).setHitPadding(new Rect(0, 0, 70, 20)).addToScreen();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new Highs(activity, null);
		}
		return super.onKeyDown(keyCode, event);
	}
	
}