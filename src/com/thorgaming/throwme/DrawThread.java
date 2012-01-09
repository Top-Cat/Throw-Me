package com.thorgaming.throwme;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.thorgaming.throwme.animation.Anim;
import com.thorgaming.throwme.displayobjects.DispObj;

public class DrawThread extends Thread {

	private static int[] gradient = {Color.rgb(0, 0, 0), Color.rgb(0, 0, 0)};
	private SurfaceHolder surfaceHolder;
	private boolean doRun = false;
	private boolean doPhysics = true;
	public Integer physicsSync = 0;
	public static Set<DispObj> toRemove = new HashSet<DispObj>();

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

	public static void resetGradient() {
		int[] gr = new int[2];
		gr[0] = Color.rgb(0, 102, 204);
		gr[1] = Color.rgb(255, 255, 255);
		setgrad(gr);
	}

	public static void setgrad(int[] gradient) {
		DrawThread.gradient = gradient;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		while (doRun) {
			Canvas c = null;
			try {
				for (MotionEvent e : (ArrayList<MotionEvent>) ThrowMe.stage.mcache.clone()) {
					ThrowMe.stage.touch(e);
				}
				ThrowMe.stage.mcache.clear();
				c = surfaceHolder.lockCanvas(null);
				if (doPhysics) {
					synchronized (physicsSync) {
						ThrowMe.stage.world.step((float) 0.01, 5);
					}
				}

				if (ThrowMe.stage.draw != null) {
					ThrowMe.stage.draw.sendCallback();
				}

				if (c != null) {
					ThrowMe.stage.start = true;

					GradientDrawable g = new GradientDrawable(Orientation.TOP_BOTTOM, gradient);
					g.setBounds(0, 0, ThrowMe.stage.camera.getScreenWidth(), ThrowMe.stage.camera.getScreenHeight());
					g.draw(c);

					List<Anim> over = new ArrayList<Anim>();
					for (Anim i : ThrowMe.stage.animations) {
						i.process(over);
					}
					ThrowMe.stage.animations.removeAll(over);

					synchronized (ThrowMe.stage.objects) {
						for (int i = 0; i <= 4; i++) {
							if (ThrowMe.stage.objects.containsKey(RenderPriority.getRenderPriorityFromId(i))) {
								for (DispObj obj : ThrowMe.stage.objects.get(RenderPriority.getRenderPriorityFromId(i))) {
									obj.draw(c, ThrowMe.stage.camera);
								}
							}
						}
					}
					for (DispObj obj : toRemove) {
						obj.destroy();
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// do this in a finally so that if an exception is thrown
				// during the above, we don't leave the Surface in an
				// inconsistent state
				if (c != null) {
					surfaceHolder.unlockCanvasAndPost(c);
				}
			}
		}

	}
}