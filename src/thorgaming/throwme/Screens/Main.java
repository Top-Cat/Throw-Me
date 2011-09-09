package thorgaming.throwme.Screens;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.Color;
import thorgaming.throwme.DevCard;
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
    
    public Main(DevCard d, Activity a, Object[] o) {
        super(d, a, o);
        
        si = o != null && o[0] != null ? (Boolean) o[0] : false;
        thorcard = new DispRes(d, R.drawable.thorgamingcard, ac.getResources(), 800, 480, 0, 0, 0, 0);
        t.schedule(new waitforscreen(), 500);
    }
    
    public class waitforscreen extends TimerTask {

		@Override
		public void run() {
			if (d.start == false) {
				t.schedule(new waitforscreen(), 500);
			} else {
				if (si) {
		        	new showmenu2().run();
		        } else {
		        	new AlphaAnim(d, thorcard, 0, 255, null, 200);
			        
			        t.schedule(new showmenu(), 4000);
		        }
			}
		}
    	
    }
    
    public class showmenu2 extends TimerTask {

		@Override
		public void run() {
			thorcard.destroy(d);
			int[] gr = new int[2];
            gr[0] = Color.rgb(0, 102, 204);
            gr[1] = Color.rgb(255, 255, 255);
			
			d.t.setgrad(gr);
			new DispRes(d, R.drawable.throwmelogo, ac.getResources(), 464, 90, 168, 15, 255, 0);
			DispObj play = new DispRes(d, R.drawable.playgame, ac.getResources(), 500, 85, 150, 160, 255, 0);
			new XAnim(d, play, -500, 150, null, 500);
			play.setMouseUpEvent(new playgame());
			DispObj highs = new DispRes(d, R.drawable.highscores, ac.getResources(), 523, 85, 134, 260, 255, 0);
			new XAnim(d, highs, -543, 134, null, 600);
			highs.setMouseUpEvent(new showhighs());
			DispObj power = new DispRes(d, R.drawable.power, ac.getResources(), 475, 85, 162, 360, 255, 0);
			new XAnim(d, power, -515, 162, null, 700);
		}

    }
    
    public class playgame implements MouseCallback {

		@Override
		public void sendCallback(int x, int y) {
			new Game(d, ac, null);
		}

		@Override
		public void sendCallback() {
			sendCallback(0, 0);
		}

    }
    
    public class showhighs implements MouseCallback {

		@Override
		public void sendCallback(int x, int y) {
			new Highs(d, ac, null);
		}

		@Override
		public void sendCallback() {
			sendCallback(0, 0);
		}

    }
    
    public class showmenu extends TimerTask {

		@Override
		public void run() {
			thorcard.destroy(d);
			thorcard = new DispRes(d, R.drawable.box2dcard, ac.getResources(), 800, 480, 0, 0, 255, 0);
			
	        t.schedule(new showmenu2(), 4000);
		}
    	
    }

}