package com.thorgaming.throwme.drawing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
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
import com.thorgaming.throwme.screens.Highs;
import com.thorgaming.throwme.screens.Main;

public class DrawThread extends Thread {

	private static int[] gradient = {Color.rgb(0, 0, 0), Color.rgb(0, 0, 0)};
	private SurfaceHolder surfaceHolder;
	private boolean doRun = false;
	private Activity goMain = null;
	private boolean activityRet = false;
	private boolean doPhysics = true;
	public Integer physicsSync = 0;
	public static Set<DispObj> toRemove = new HashSet<DispObj>();
	private boolean paused = false;
	private boolean pausedInternal = false;

	public DrawThread(SurfaceHolder surfaceHolder, Context context) {
		this.surfaceHolder = surfaceHolder;
	}

	public void setRunning(boolean running) {
		doRun = running;
	}

	public void setPhysics(boolean physics) {
		doPhysics = physics;
	}

	public boolean isPhysicsRunning() {
		return doPhysics;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
		if (!paused) {
			pausedInternal = false;
		}
	}

	public boolean getPaused() {
		return paused;
	}

	public static void resetGradient() {
		int[] gr = new int[2];
		gr[0] = Color.rgb(0, 102, 204);
		gr[1] = Color.rgb(255, 255, 255);
		setgrad(gr);
	}

	public static void setgrad(int[] gradient) {
		DrawThread.gradient = gradient;
	}

	public void returnMain(Activity activity) {
		returnAct(activity, false);
	}

	public void returnHighs(Activity activity) {
		returnAct(activity, true);
	}

	private void returnAct(Activity activity, boolean a) {
		goMain = activity;
		activityRet = a;
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

					}

					if (goMain != null) {
						if (activityRet) {
							goMain.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									new Highs(goMain, new Object[] {true, ThrowMe.getInstance().stage.camera.getX() / 10});
								}
							});
						} else {
							goMain.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									new Main(goMain, new Object[] {true});
								}
							});
						}
						while (goMain != null) {
						}
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