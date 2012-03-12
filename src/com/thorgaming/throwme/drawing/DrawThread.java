package com.thorgaming.throwme.drawing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.view.SurfaceHolder;

import com.thorgaming.throwme.MotionEventStore;
import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.animation.Anim;
import com.thorgaming.throwme.displayobjects.DispObj;

/**
 * @author Thomas Cheyney
 * @version 1.0
 */
public class DrawThread extends Thread {

	/**
	 * Background gradient to draw
	 */
	private static int[] gradient = {Color.rgb(0, 0, 0), Color.rgb(0, 0, 0)};
	/**
	 * We're holding the display surface
	 */
	private SurfaceHolder surfaceHolder;
	/**
	 * If the thread should run, can be used to stop it
	 */
	private boolean doRun = false;
	/**
	 * Should physics updates be calculated this tick?
	 */
	private boolean doPhysics = true;
	/**
	 * Used to prevent changes to the world during physics updates
	 */
	public Integer physicsSync = 0;
	/**
	 * Display objects to remove at the end of the tick
	 */
	public static Set<DispObj> toRemove = new HashSet<DispObj>();
	/**
	 * Stops ticks being sent to objects while the game is paused
	 */
	private boolean paused = false;
	/**
	 * Internal variable to remember paused status
	 */
	private boolean pausedInternal = false;
	/**
	 * Runnables to run on the draw thread
	 */
	private List<Runnable> onUi = new ArrayList<Runnable>();
	/**
	 * Lock to the onUi list
	 */
	private Lock onUiLock = new ReentrantLock();

	public DrawThread(SurfaceHolder surfaceHolder, Context context) {
		this.surfaceHolder = surfaceHolder;
	}

	/**
	 * Sets if the thread should run
	 * 
	 * @param running If the thread should run
	 */
	public void setRunning(boolean running) {
		doRun = running;
	}

	/**
	 * Sets if physics updates should be calculated this tick?
	 * 
	 * @param physics If physics updates should be calculated this tick?
	 */
	public void setPhysics(boolean physics) {
		doPhysics = physics;
	}

	/**
	 * Gets if physics updates are being calculated this tick
	 * 
	 * @return If physics updates are being calculated this tick
	 */
	public boolean isPhysicsRunning() {
		return doPhysics;
	}

	/**
	 * Sets if the the game is paused
	 * 
	 * @param paused If the game should be paused
	 */
	public void setPaused(boolean paused) {
		this.paused = paused;
		if (!paused) {
			pausedInternal = false;
		}
	}

	/**
	 * Gets if the game is paused
	 * 
	 * @return If the game is paused
	 */
	public boolean getPaused() {
		return paused;
	}

	/**
	 * Resets the background gradient to the default
	 */
	public static void resetGradient() {
		int[] gr = new int[2];
		gr[0] = Color.rgb(0, 102, 204);
		gr[1] = Color.rgb(255, 255, 255);
		setgrad(gr);
	}

	/**
	 * Sets the background gradient
	 * @param gradient
	 */
	public static void setgrad(int[] gradient) {
		DrawThread.gradient = gradient;
	}

	/**
	 * Add a runnable to be run on the ui thread
	 * 
	 * @param run Runnable to be run
	 */
	public void runOnUi(Runnable run) {
		onUiLock.lock();
		try {
			onUi.add(run);
		} finally {
			onUiLock.unlock();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		while (doRun) {
			Canvas c = null;
			try {
				for (MotionEventStore e : (ArrayList<MotionEventStore>) ThrowMe.getInstance().stage.mcache.clone()) {
					ThrowMe.getInstance().stage.touch(e);
				}
				if (!pausedInternal) {
					ThrowMe.getInstance().stage.mcache.clear();
					c = surfaceHolder.lockCanvas(null);
					if (doPhysics) {
						synchronized (physicsSync) {
							ThrowMe.getInstance().stage.world.step((float) 0.01, 5);
						}
					}

					if (ThrowMe.getInstance().stage.draw != null) {
						ThrowMe.getInstance().stage.draw.sendCallback();
					}

					if (c != null) {
						ThrowMe.getInstance().stage.start = true;

						GradientDrawable g = new GradientDrawable(Orientation.TOP_BOTTOM, gradient);
						g.setBounds(0, 0, ThrowMe.getInstance().stage.camera.getScreenWidth(), ThrowMe.getInstance().stage.camera.getScreenHeight());
						g.draw(c);

						List<Anim> over = new ArrayList<Anim>();
						for (Anim i : ThrowMe.getInstance().stage.animations) {
							i.process(over);
						}
						ThrowMe.getInstance().stage.animations.removeAll(over);

						synchronized (ThrowMe.getInstance().stage.objects) {
							for (int i = 0; i <= 4; i++) {
								if (ThrowMe.getInstance().stage.objects.containsKey(RenderPriority.getRenderPriorityFromId(i))) {
									for (DispObj obj : ThrowMe.getInstance().stage.objects.get(RenderPriority.getRenderPriorityFromId(i))) {
										obj.draw(c, ThrowMe.getInstance().stage.camera);
									}
								}
							}
						}
						for (DispObj obj : toRemove) {
							obj.destroy();
						}
						toRemove.clear();
					}

					onUiLock.lock();
					try {
						for (Runnable run : onUi) {
							run.run();
						}
						onUi.clear();
					} finally {
						onUiLock.unlock();
					}

					pausedInternal = paused;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (c != null) {
					surfaceHolder.unlockCanvasAndPost(c);
				}
			}
		}
	}
}