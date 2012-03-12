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
 * Keeps track of objects to draw and the physics world
 * 
 * @author Thomas Cheyney
 * @version 1.0
 */
public class Stage extends SurfaceView implements SurfaceHolder.Callback {

	/**
	 * The camera object used when drawing
	 */
	public Camera camera = new Camera();
	/**
	 * Animations in progress to be ticked
	 */
	public List<Anim> animations = new ArrayList<Anim>();
	/**
	 * Objects to be drawn each tick
	 */
	public HashMap<RenderPriority, Set<DispObj>> objects = new HashMap<RenderPriority, Set<DispObj>>();
	/**
	 * Drawing thread
	 */
	public DrawThread drawThread;
	/**
	 * Physics world size constraint
	 */
	private AABB worldAABB;
	/**
	 * Physics world
	 */
	public World world;
	/**
	 * Ratio between code co-ordinates and physics co-ordinates
	 */
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

	/**
	 * Registers an object to be drawn by the draw thread 
	 * 
	 * @param priority Render priority, allowing objects to be drawn on top of others
	 * @param object Object to be drawn
	 */
	public void registerForRender(RenderPriority priority, DispObj object) {
		synchronized (ThrowMe.getInstance().stage.objects) {
			if (!objects.containsKey(priority)) {
				objects.put(priority, new HashSet<DispObj>());
			}
			objects.get(priority).add(object);
		}
	}

	/**
	 * Stops an object being drawn
	 * 
	 * @param object Object to be removed from the screen
	 */
	public void unregisterForRender(DispObj object) {
		synchronized (ThrowMe.getInstance().stage.objects) {
			for (RenderPriority priority : objects.keySet()) {
				objects.get(priority).remove(object);
			}
		}
	}

	/**
	 * Clear the screen
	 */
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

	/**
	 * Motion event store, holds touch events until they are processed
	 */
	ArrayList<MotionEventStore> mcache = new ArrayList<MotionEventStore>();

	/**
	 * Adds a touch event to the store list 
	 * 
	 * @param event Motion event store representing the touch event
	 */
	public void sendtouch(MotionEvent event) {
		mcache.add(new MotionEventStore(event));
	}

	/**
	 * Processes a touch event
	 * 
	 * @param event Motion event store representing the touch event
	 */
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

	/**
	 * Method to be called every draw tick
	 */
	public Callback draw = null;
	/**
	 * True when the draw thread has drawn a screen
	 */
	public boolean start = false;

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		camera.setScreen(width, height);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
	}

	/**
	 * Create a draw thread
	 */
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