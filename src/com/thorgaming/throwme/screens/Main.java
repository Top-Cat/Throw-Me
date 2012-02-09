package com.thorgaming.throwme.screens;

import java.util.Timer;
import java.util.TimerTask;

import com.thorgaming.throwme.R;
import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.animation.AlphaAnim;
import com.thorgaming.throwme.animation.XAnim;
import com.thorgaming.throwme.callback.MouseCallback;
import com.thorgaming.throwme.displayobjects.DispObj;
import com.thorgaming.throwme.displayobjects.DispRes;
import com.thorgaming.throwme.drawing.DrawThread;

/**
 * @author Thomas Cheyney
 * @version 1.0
 */
public class Main extends Screen {

	/**
	 * Stores the cards that appear fullscreen when the activity starts
	 */
	private DispObj thorcard;
	/**
	 * Schedules the next card to appear and then the menu
	 */
	private Timer timer = new Timer();
	/**
	 * Stores the value passed from other screens to skip the cards if not first launch
	 */
	private boolean skipIntro;

	public Main(Object[] data) {
		super(data);

		skipIntro = data != null && data[0] != null ? (Boolean) data[0] : false;
		thorcard = new DispRes(R.drawable.thorgamingcard).setWidth(800).setHeight(480).setAlpha(0).addToScreen();
		timer.schedule(new WaitForScreen(), 500);
	}

	public class WaitForScreen extends TimerTask {
		@Override
		public void run() {
			if (ThrowMe.getInstance().stage.start == false) {
				timer.schedule(new WaitForScreen(), 500);
			} else {
				if (skipIntro) {
					new ShowMenu2().run();
				} else {
					new AlphaAnim(thorcard, 0, 255, null, 200);
					timer.schedule(new ShowMenu(), 4000);
				}
			}
		}
	}

	public class ShowMenu2 extends TimerTask {
		@Override
		public void run() {
			thorcard.destroy();
			DrawThread.resetGradient();
			new DispRes(R.drawable.throwmelogo).setWidth(464).setHeight(90).setX(168).setY(15).addToScreen();
			DispObj play = new DispRes(R.drawable.playgame).setWidth(500).setHeight(85).setX(150).setY(160).addToScreen();
			new XAnim(play, -500, 150, null, 500);
			play.setMouseUpEvent(new MouseCallback() {
				@Override
				public boolean sendCallback(int x, int y) {
					new Game(null);
					return true;
				}
			});
			DispObj highs = new DispRes(R.drawable.highscores).setWidth(523).setHeight(85).setX(134).setY(260).addToScreen();
			new XAnim(highs, -543, 134, null, 600);
			highs.setMouseUpEvent(new MouseCallback() {
				@Override
				public boolean sendCallback(int x, int y) {
					new Highs(null);
					return true;
				}
			});
			DispObj power = new DispRes(R.drawable.power).setWidth(475).setHeight(85).setX(162).setY(360).addToScreen();
			new XAnim(power, -515, 162, null, 700);
			power.setMouseUpEvent(new MouseCallback() {
				@Override
				public boolean sendCallback(int x, int y) {
					new Power(null);
					return true;
				}
			});
		}
	}

	public class ShowMenu extends TimerTask {
		@Override
		public void run() {
			thorcard.destroy();
			thorcard = new DispRes(R.drawable.box2dcard).setWidth(800).setHeight(480).addToScreen();

			timer.schedule(new ShowMenu2(), 4000);
		}
	}

}