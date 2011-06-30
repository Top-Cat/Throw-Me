package thorgaming.throwme;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DevCard extends SurfaceView implements SurfaceHolder.Callback {

	public Camera ca = new Camera();
	
    public void clear() {
    	synchronized (objs) {
	    	for (DispObj i : objs) {
	    		i.destroy(this);
	    	}
		}
    }
    
	public List<Anim> anims = new ArrayList<Anim>();
	public List<DispObj> objs = new ArrayList<DispObj>();
	
	DrawThread t;
	int w = 800;
	int h = 480;
	
	private AABB worldAABB;
	public World world;
    static public float ratio = 30;
    
	public DevCard(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		anims.clear();
		objs.clear();
		
		SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        
		t = new DrawThread(holder, context);
		
		worldAABB = new AABB();
        worldAABB.lowerBound.set(new Vec2((float) -1000.0 / ratio, (float) -9000.0 / ratio));
        worldAABB.upperBound.set(new Vec2((float) 90000.0 / ratio, (float) 1000.0 / ratio));
        
        Vec2 gravity = new Vec2((float) 0.0, (float) 9.81);
        boolean doSleep = true;
        world = new World(worldAABB, gravity, doSleep);
    }
	
	List<MotionEvent> mcache = new ArrayList<MotionEvent>();
	
	public void sendtouch(MotionEvent event) {
		mcache.add(event);
	}
	
	public void touch(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			for (DispObj i : objs) {
				if (i.checkPress(x, y)) {
					if (i.d != null) {
						i.d.sendCallback(x, y);
						break;
					}
				}
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			for (DispObj i : objs) {
				if (i.checkPress(x, y)) {
					if (i.c != null) {
						i.c.sendCallback(x, y);
						break;
					}
				}
			}
		}
	}
	
	Callback draw = null;
	
	int[] gr = {Color.rgb(0, 0, 0), Color.rgb(0, 0, 0)};
	
	class DrawThread extends Thread {
		
		SurfaceHolder shold;
		Context co;
		Boolean doRun = false;
		int p;
		int test = (int) (Math.random() * 100);
		
		public DrawThread(SurfaceHolder surfaceHolder, Context context) {
			co = context;
			shold = surfaceHolder;
		}
		
		public void setRunning(boolean b) {
            doRun = b;
        }
		
		public void setgrad(int[] grn) {
			gr = grn;
		}
		
		int height = 0;
		
		@Override
        public void run() {
			while(doRun) {
				Canvas c = null;
                try {
                	for (MotionEvent e : mcache) {
                		touch(e);
                	}
                	mcache.clear();
                    c = shold.lockCanvas(null);
                    world.step((float) 0.01, 5);
                    
                	if (draw != null) {
                		draw.sendCallback();
                	}
                    
                    if (c != null) {
	                    GradientDrawable g = new GradientDrawable(Orientation.TOP_BOTTOM, gr);
	                    g.setBounds(0, 0, w, h);
	                    g.draw(c);
	                    
	                    List<Anim> over = new ArrayList<Anim>();
	                    for (Anim i : anims) {
	                    	i.process(over);
	                    }
	                    anims.removeAll(over);
	                    
	                    synchronized (objs) {
		                    for (DispObj i : objs) {
		                    	i.draw(c, ca);
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
                    	shold.unlockCanvasAndPost(c);
                    }
                }
			}
    	
		}
	}
	
	final Object monitor = new Object();
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		w = width;
		h = height;
		ca.setScreen(w, h);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		//System.out.println(objs.size());
		
		/*if (t.p != 1) {
			t.setRunning(true);
			t.start();
		} else {

			//t.run();
		}
		t.pos(1);*/
		
	}
	
	public void createThread() {
		t.setRunning(false);
		t = new DrawThread(getHolder(), getContext());
		t.setRunning(true);
		t.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
        t.setRunning(false);
        while (retry) {
            try {
                t.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
	}
	
}