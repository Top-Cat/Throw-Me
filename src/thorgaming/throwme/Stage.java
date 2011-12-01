package thorgaming.throwme;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import thorgaming.throwme.animation.Anim;
import thorgaming.throwme.displayobjects.DispObj;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Stage extends SurfaceView implements SurfaceHolder.Callback {
	
	public Camera camera = new Camera();
	
	public List<Anim> animations = new ArrayList<Anim>();
	public ArrayList<DispObj> objects = new ArrayList<DispObj>();
	//private static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4GbR3FqjQIqFkxFBWoKqCmIXAEMwdK8E13+AQuMU4i0fVw8kLMFZbk7T1YPezQnBm6ozwJSBrQA+M4HOdKguqnGE+hDtFzWCq5/mZh7VM8/9Sow7EFvlbQll2DR/8OQE1aXGcRKEf51H9a7i5VswOsqwiTAP7BqtbGo/aujo1NxtwX/OYDGIIEx/V7r1lBQCfgNEM9+dn6Ahr4ETPVU9QLhyP2F99vKBhgJ4euQj0/zpaA0jjItMhrfTRAwPXVvWnh65+ECOlpQ6WNZZF2kHBjr5ocHH+zEJDGKrs0DOQ3WDiraoaqmBXRB85vHtQQRV/8KxJHpjtWC2k0eLrfoH4wIDAQAB";
	
	public DrawThread drawThread;
	
	private AABB worldAABB;
	public World world;
	static public float ratio = 30;
	
	public Stage(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		animations.clear();
		objects.clear();
		
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		
		drawThread = new DrawThread(holder, context, this);
		
		worldAABB = new AABB();
		worldAABB.lowerBound.set(new Vec2((float) -1000.0 / ratio, (float) -9000.0 / ratio));
		worldAABB.upperBound.set(new Vec2((float) 90000.0 / ratio, (float) 1000.0 / ratio));
		
		Vec2 gravity = new Vec2((float) 0.0, (float) 9.81);
		boolean doSleep = true;
		world = new World(worldAABB, gravity, doSleep);
	}
	
	@SuppressWarnings("unchecked")
	public void clear() {
		synchronized (drawThread.physicsSync) {
			for (DispObj obj : (ArrayList<DispObj>) objects.clone()) {
				obj.destroy();
			}
		}
	}
	
	ArrayList<MotionEvent> mcache = new ArrayList<MotionEvent>();
	
	public void sendtouch(MotionEvent event) {
		mcache.add(event);
	}
	
	@SuppressWarnings("unchecked")
	public void touch(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			for (DispObj i : (ArrayList<DispObj>) objects.clone()) {
				if (i.checkPress(x, y)) {
					MouseCallback callback = i.getMouseDownEvent();
					if (callback != null) {
						callback.sendCallback(x, y);
						break;
					}
				}
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			for (DispObj i : (ArrayList<DispObj>) objects.clone()) {
				if (i.checkPress(x, y)) {
					MouseCallback callback = i.getMouseUpEvent();
					if (callback != null) {
						callback.sendCallback(x, y);
						break;
					}
				}
			}
		}
	}
	
	public Callback draw = null;
	public boolean start = false;
	
	final Object monitor = new Object();
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		camera.setScreen(width, height);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) { }
	
	public void createThread() {
		drawThread.setRunning(false);
		drawThread = new DrawThread(getHolder(), getContext(), this);
		drawThread.setRunning(true);
		drawThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		drawThread.setRunning(false);
		while (retry) {
			try {
				drawThread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
	}
	
}