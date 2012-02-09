package com.thorgaming.throwme.drawing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.thorgaming.throwme.MotionEventStore;
import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.animation.Anim;
import com.thorgaming.throwme.callback.Callback;
import com.thorgaming.throwme.callback.MouseCallback;
import com.thorgaming.throwme.displayobjects.DispObj;

/**
 * @author Thomas Cheyney
 * @version 1.0
 */
public class Stage extends SurfaceView implements SurfaceHolder.Callback {

	public Camera camera = new Camera();

	public List<Anim> animations = new ArrayList<Anim>();
	public HashMap<RenderPriority, Set<DispObj>> objects = new HashMap<RenderPriority, Set<DispObj>>();

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

		drawThread = new DrawThread(holder, context);

		worldAABB = new AABB();
		worldAABB.lowerBound.set(new Vec2((float) -1000.0 / ratio, (float) -20000.0 / ratio));
		worldAABB.upperBound.set(new Vec2((float) 9000000.0 / ratio, (float) 1000.0 / ratio));

		Vec2 gravity = new Vec2((float) 0.0, (float) 9.80665);
		boolean doSleep = true;
		world = new World(worldAABB, gravity, doSleep);
	}

	public void registerForRender(RenderPriority priority, DispObj object) {
		synchronized (ThrowMe.getInstance().stage.objects) {
			if (!objects.containsKey(priority)) {
				objects.put(priority, new HashSet<DispObj>());
			}
			objects.get(priority).add(object);
		}
	}

	public void unregisterForRender(DispObj object) {
		synchronized (ThrowMe.getInstance().stage.objects) {
			for (RenderPriority priority : objects.keySet()) {
				objects.get(priority).remove(object);
			}
		}
	}

	public void clear() {
		synchronized (drawThread.physicsSync) {
			Set<DispObj> allObjects = new HashSet<DispObj>();
			for (RenderPriority priority : objects.keySet()) {
				for (DispObj obj : objects.get(priority)) {
					allObjects.add(obj);
				}
			}
			for (DispObj obj : allObjects) {
				obj.destroy();
			}
			objects.clear();
		}
	}

	ArrayList<MotionEventStore> mcache = new ArrayList<MotionEventStore>();

	public void sendtouch(MotionEvent event) {
		mcache.add(new MotionEventStore(event));
	}

	public void touch(MotionEventStore event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		MouseCallback callback = null;

		if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_UP) {
			findObject: for (int i = 4; i >= 0; i--) {
				if (objects.containsKey(RenderPriority.getRenderPriorityFromId(i))) {
					synchronized (ThrowMe.getInstance().stage.objects) {
						for (DispObj obj : objects.get(RenderPriority.getRenderPriorityFromId(i))) {
							if (obj.checkPress(x, y)) {
								if (event.getAction() == MotionEvent.ACTION_DOWN) {
									callback = obj.getMouseDownEvent();
								} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
									callback = obj.getMouseMoveEvent();
								} else if (event.getAction() == MotionEvent.ACTION_UP) {
									callback = obj.getMouseUpEvent();
								}
								if (callback != null) {
									if (callback.sendCallback(x, y)) {
										break findObject;
									}
								}
							}
						}
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
	public void surfaceCreated(SurfaceHolder holder) {
	}

	public void createThread() {
		drawThread.setRunning(false);
		drawThread = new DrawThread(getHolder(), getContext());
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