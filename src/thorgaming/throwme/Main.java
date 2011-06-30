package thorgaming.throwme;

import java.sql.Connection;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import thorgaming.throwme.DevCard;
import thorgaming.throwme.Anims.AlphaAnim;
import thorgaming.throwme.Anims.XAnim;
import thorgaming.throwme.DispObjs.DispRes;

public class Main extends Activity {
	
	DevCard d;
	DispObj thorcard;
    Connection conn;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.main);
        
        d = (DevCard) findViewById(R.id.menu);
        boolean si = getIntent().getBooleanExtra("skipintro", true);
        thorcard = new DispRes(d, R.drawable.thorgamingcard, getResources(), 800, 480, 0, 0, 0, 0);
        if (si) {
        	new showmenu2().run();
        } else {
        	new AlphaAnim(d, thorcard, 0, 255, null, 200);
	        
	        Timer t = new Timer();
	        t.schedule(new showmenu(), 4000);
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if (!this.isFinishing()) {
	    	d.sendtouch(event);
    	}
		return false;
    }
    
    public class showmenu2 extends TimerTask {

		@Override
		public void run() {
			thorcard.destroy(d);
			int[] gr = new int[2];
            gr[0] = Color.rgb(0, 102, 204);
            gr[1] = Color.rgb(255, 255, 255);
			
			d.t.setgrad(gr);
			new DispRes(d, R.drawable.throwmelogo, getResources(), 464, 90, 168, 15, 255, 0);
			DispObj play = new DispRes(d, R.drawable.playgame, getResources(), 500, 85, 150, 160, 255, 0);
			new XAnim(d, play, -500, 150, null, 500);
			play.setMouseUpEvent(new playgame());
			DispObj highs = new DispRes(d, R.drawable.highscores, getResources(), 523, 85, 134, 260, 255, 0);
			new XAnim(d, highs, -543, 134, null, 600);
			highs.setMouseUpEvent(new showhighs());
			DispObj power = new DispRes(d, R.drawable.power, getResources(), 475, 85, 162, 360, 255, 0);
			new XAnim(d, power, -515, 162, null, 700);
		}

    }
    
    public class playgame implements MouseCallback {

		@Override
		public void sendCallback(int x, int y) {
			//System.out.println("Play game!");
			Intent intent = new Intent(Main.this, Game.class);
	        startActivity(intent);
	        finish();
		}

		@Override
		public void sendCallback() {
			sendCallback(0, 0);
		}

    }
    
    public class showhighs implements MouseCallback {

		@Override
		public void sendCallback(int x, int y) {
			//System.out.println("Show scores!");
			Intent intent = new Intent(Main.this, Highs.class);
	        startActivity(intent);
	        finish();
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
			thorcard = new DispRes(d, R.drawable.box2dcard, getResources(), 800, 480, 0, 0, 255, 0);
			
			Timer t = new Timer();
	        t.schedule(new showmenu2(), 4000);
		}
    	
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	d.createThread();
    }

}