package thorgaming.throwme.screens;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import thorgaming.throwme.DrawThread;
import thorgaming.throwme.ThrowMe;
import thorgaming.throwme.displayobjects.DispObj;
import thorgaming.throwme.MouseCallback;
import thorgaming.throwme.R;
import thorgaming.throwme.animation.AlphaAnim;
import thorgaming.throwme.animation.XAnim;
import thorgaming.throwme.displayobjects.DispRes;

public class Main extends Screen {
	
	private DispObj thorcard;
	private Timer timer = new Timer();
	private boolean skipIntro;
	
	public Main(Activity activity, Object[] data) {
		super(activity, data);
		
		skipIntro = data != null && data[0] != null ? (Boolean) data[0] : false;
		skipIntro = true;
		thorcard = new DispRes(R.drawable.thorgamingcard).setWidth(800).setHeight(480).setAlpha(0).addToScreen();
		timer.schedule(new waitforscreen(), 500);
	}
	
	public class waitforscreen extends TimerTask {

		@Override
		public void run() {
			if (ThrowMe.stage.start == false) {
				timer.schedule(new waitforscreen(), 500);
			} else {
				if (skipIntro) {
					new showmenu2().run();
				} else {
					new AlphaAnim(thorcard, 0, 255, null, 200);
					
					timer.schedule(new showmenu(), 4000);
				}
			}
		}
		
	}
	
	public class showmenu2 extends TimerTask {

		@Override
		public void run() {
			thorcard.destroy();
			DrawThread.resetGradient();
			new DispRes(R.drawable.throwmelogo).setWidth(464).setHeight(90).setX(168).setY(15).addToScreen();
			DispObj play = new DispRes(R.drawable.playgame).setWidth(500).setHeight(85).setX(150).setY(160).addToScreen();
			new XAnim(play, -500, 150, null, 500);
			play.setMouseUpEvent(new playgame());
			DispObj highs = new DispRes(R.drawable.highscores).setWidth(523).setHeight(85).setX(134).setY(260).addToScreen();
			new XAnim(highs, -543, 134, null, 600);
			highs.setMouseUpEvent(new showhighs());
			DispObj power = new DispRes(R.drawable.power).setWidth(475).setHeight(85).setX(162).setY(360).addToScreen();
			new XAnim(power, -515, 162, null, 700);
		}

	}
	
	public class playgame implements MouseCallback {

		@Override
		public void sendCallback(int x, int y) {
			new Game(activity, null);
		}

		@Override
		public void sendCallback() {
			sendCallback(0, 0);
		}

	}
	
	public class showhighs implements MouseCallback {

		@Override
		public void sendCallback(int x, int y) {
			new Highs(activity, null);
		}

		@Override
		public void sendCallback() {
			sendCallback(0, 0);
		}

	}
	
	public class showmenu extends TimerTask {

		@Override
		public void run() {
			thorcard.destroy();
			thorcard = new DispRes(R.drawable.box2dcard).setWidth(800).setHeight(480).addToScreen();
			
			timer.schedule(new showmenu2(), 4000);
		}
		
	}

}