package thorgaming.throwme;

import java.util.ArrayList;
import java.util.List;

import thorgaming.throwme.animation.Anim;
import thorgaming.throwme.displayobjects.DispObj;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
	
public class DrawThread extends Thread {
	
	private static int[] gradient = {Color.rgb(0, 0, 0), Color.rgb(0, 0, 0)};
	private SurfaceHolder surfaceHolder;
	private boolean doRun = false;
	private Stage stage;
	public Object physicsSync = 0;
	
	public DrawThread(SurfaceHolder surfaceHolder, Context context, Stage stage) {
		this.surfaceHolder = surfaceHolder;
		this.stage = stage;
	}
	
	public void setRunning(boolean running) {
		doRun = running;
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
	
	@Override
	public void run() {
		while(doRun) {
			Canvas c = null;
			try {
				for (MotionEvent e : stage.mcache) {
					stage.touch(e);
				}
				stage.mcache.clear();
				c = surfaceHolder.lockCanvas(null);
				synchronized (physicsSync) {
					stage.world.step((float) 0.01, 5);					
				}
				
				if (stage.draw != null) {
					stage.draw.sendCallback();
				}
				
				if (c != null) {
					stage.start = true;
					
					GradientDrawable g = new GradientDrawable(Orientation.TOP_BOTTOM, gradient);
					g.setBounds(0, 0, stage.camera.getScreenWidth(), stage.camera.getScreenHeight());
					g.draw(c);
					
					List<Anim> over = new ArrayList<Anim>();
					for (Anim i : stage.animations) {
						i.process(over);
					}
					stage.animations.removeAll(over);
					
					synchronized (stage.objects) {
						for (DispObj i : stage.objects) {
							i.draw(c, stage.camera);
						}
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