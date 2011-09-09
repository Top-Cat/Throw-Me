package thorgaming.throwme;

import java.sql.Connection;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import thorgaming.throwme.DevCard;
import thorgaming.throwme.Screens.Main;
import thorgaming.throwme.Screens.Screen;

public class ThrowMe extends Activity {
	
	DevCard d;
	DispObj thorcard;
    Connection conn;
    public Screen s;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.main);
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        d = (DevCard) findViewById(R.id.menu);
        
        new Main(d, this, null);
    }
    
    public static void waiting(int n){
        long t0, t1;
        t0 =  System.currentTimeMillis();
        do{
            t1 = System.currentTimeMillis();
        }
        while ((t1 - t0) < n);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if (!this.isFinishing()) {
	    	d.sendtouch(event);
    	}
		return s.onTouch(event);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		return s.onKeyDown(keyCode, event);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	d.createThread();
    }

}