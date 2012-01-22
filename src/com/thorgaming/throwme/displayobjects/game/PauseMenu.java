package com.thorgaming.throwme.displayobjects.game;

import android.app.Activity;

import com.thorgaming.throwme.R;
import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.callback.MouseCallback;
import com.thorgaming.throwme.displayobjects.DispObj;
import com.thorgaming.throwme.displayobjects.DispRes;
import com.thorgaming.throwme.displayobjects.shape.Rect;
import com.thorgaming.throwme.drawing.RenderPriority;
import com.thorgaming.throwme.screens.Main;

public class PauseMenu {

	private final DispObj pauseButton;
	private final Rect pauseBg;
	private final DispObj resumeButton;
	private final DispObj mainMenuButton;

	public PauseMenu(final Activity activity) {
		pauseButton = new DispRes(R.drawable.pause).setWidth(48).setHeight(48).setX(736).setY(416).setMouseDownEvent(new MouseCallback() {
			@Override
			public boolean sendCallback(int x, int y) {
				if (pauseButton.getAlpha() > 0) {
					ThrowMe.getInstance().stage.drawThread.setPaused(true);
					pauseBg.setAlpha(180);
					resumeButton.setAlpha(255);
					mainMenuButton.setAlpha(255);
					pauseButton.setAlpha(0);
				}
				return false;
			}
		}).addToScreen(RenderPriority.Lowest);

		pauseBg = (Rect) new Rect().setWidth(800).setHeight(480).setAlpha(0).addToScreen(RenderPriority.Low);
		pauseBg.paint.setColor(0xB8E8FC);

		resumeButton = new DispRes(R.drawable.resume).setX(130).setY(130).setWidth(266).setHeight(70).setMouseDownEvent(new MouseCallback() {
			@Override
			public boolean sendCallback(int x, int y) {
				if (resumeButton.getAlpha() > 0) {
					ThrowMe.getInstance().stage.drawThread.setPaused(false);
					pauseBg.setAlpha(0);
					resumeButton.setAlpha(0);
					mainMenuButton.setAlpha(0);
					pauseButton.setAlpha(255);
				}
				return false;
			}
		}).setIgnorePause().setAlpha(0).addToScreen(RenderPriority.Lowest);

		mainMenuButton = new DispRes(R.drawable.mainmenu).setX(284).setY(270).setWidth(386).setHeight(70).setMouseDownEvent(new MouseCallback() {
			@Override
			public boolean sendCallback(int x, int y) {
				if (mainMenuButton.getAlpha() > 0) {
					new Main(activity, new Object[] {true});
					return true;
				}
				return false;
			}
		}).setIgnorePause().setAlpha(0).addToScreen(RenderPriority.Lowest);
	}

}