package thorgaming.throwme.Screens;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import thorgaming.throwme.Stage;
import thorgaming.throwme.DispObj;
import thorgaming.throwme.MouseCallback;
import thorgaming.throwme.R;
import thorgaming.throwme.Anims.AlphaAnim;
import thorgaming.throwme.Anims.XAnim;
import thorgaming.throwme.DispObjs.DispRes;

public class Main extends Screen {
	
	DispObj thorcard;
	Timer t = new Timer();
	boolean si;
	
	public Main(Stage stage, Activity a, Object[] o) {
		super(stage, a, o);
		
		si = o != null && o[0] != null ? (Boolean) o[0] : false;
		si = true;
		thorcard = new DispRes(stage, R.drawable.thorgamingcard, activity.getResources(), 800, 480, 0, 0, 0, 0);
		t.schedule(new waitforscreen(), 500);
	}
	
	public class waitforscreen extends TimerTask {

		@Override
		public void run() {
			if (stage.start == false) {
				t.schedule(new waitforscreen(), 500);
			} else {
				if (si) {
					new showmenu2().run();
				} else {
					new AlphaAnim(stage, thorcard, 0, 255, null, 200);
					
					t.schedule(new showmenu(), 4000);
				}
			}
		}
		
	}
	
	public class showmenu2 extends TimerTask {

		@Override
		public void run() {
			thorcard.destroy(stage);
			stage.t.resetGradient();
			new DispRes(stage, R.drawable.throwmelogo, activity.getResources(), 464, 90, 168, 15, 255, 0);
			DispObj play = new DispRes(stage, R.drawable.playgame, activity.getResources(), 500, 85, 150, 160, 255, 0);
			new XAnim(stage, play, -500, 150, null, 500);
			play.setMouseUpEvent(new playgame());
			DispObj highs = new DispRes(stage, R.drawable.highscores, activity.getResources(), 523, 85, 134, 260, 255, 0);
			new XAnim(stage, highs, -543, 134, null, 600);
			highs.setMouseUpEvent(new showhighs());
			DispObj power = new DispRes(stage, R.drawable.power, activity.getResources(), 475, 85, 162, 360, 255, 0);
			new XAnim(stage, power, -515, 162, null, 700);
		}

	}
	
	public class playgame implements MouseCallback {

		@Override
		public void sendCallback(int x, int y) {
			new Game(stage, activity, null);
		}

		@Override
		public void sendCallback() {
			sendCallback(0, 0);
		}

	}
	
	public class showhighs implements MouseCallback {

		@Override
		public void sendCallback(int x, int y) {
			new Highs(stage, activity, null);
		}

		@Override
		public void sendCallback() {
			sendCallback(0, 0);
		}

	}
	
	public class showmenu extends TimerTask {

		@Override
		public void run() {
			thorcard.destroy(stage);
			thorcard = new DispRes(stage, R.drawable.box2dcard, activity.getResources(), 800, 480, 0, 0, 255, 0);
			
			t.schedule(new showmenu2(), 4000);
		}
		
	}

}