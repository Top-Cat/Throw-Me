package thorgaming.throwme.Screens;

import android.app.Activity;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.MotionEvent;

import org.jbox2d.collision.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.ContactListener;
import org.jbox2d.dynamics.contacts.ContactPoint;
import org.jbox2d.dynamics.contacts.ContactResult;

import thorgaming.throwme.Callback;
import thorgaming.throwme.DrawThread;
import thorgaming.throwme.Stage;
import thorgaming.throwme.MouseCallback;
import thorgaming.throwme.R;
import thorgaming.throwme.DispObjs.Character;
import thorgaming.throwme.DispObjs.Cloud;
import thorgaming.throwme.DispObjs.DispGif;
import thorgaming.throwme.DispObjs.DispRes;
import thorgaming.throwme.DispObjs.DispRes_Rel;
import thorgaming.throwme.DispObjs.PhysCircle;
import thorgaming.throwme.DispObjs.Rect;

public class Game extends Screen {
	
	static { @SuppressWarnings("unused") byte dummy[] = new byte[ 8*1024*1024 ]; }	
	int gr[][] = new int[9][3];
	
	DispRes a, b;
	DispRes_Rel box;
	PhysCircle[] dj = new PhysCircle[7];
	Character c;
	Cloud c1;
	
	public Game(Stage stage, Activity activity, Object[] data) {
		super(stage, activity, data);
		
		gr[0][0] = 255;
		gr[0][1] = 255;
		gr[0][2] = 255;
		
		gr[1][0] = 0;
		gr[1][1] = 102;
		gr[1][2] = 204;
		
		gr[2][0] = 255;
		gr[2][1] = 255;
		gr[2][2] = 0;
		
		gr[3][0] = 255;
		gr[3][1] = 153;
		gr[3][2] = 0;
		
		gr[4][0] = 153;
		gr[4][1] = 0;
		gr[4][2] = 51;
		
		gr[5][0] = 51;
		gr[5][1] = 255;
		gr[5][2] = 102;
		
		gr[6][0] = 0;
		gr[6][1] = 51;
		gr[6][2] = 204;
		
		gr[7][0] = 0;
		gr[7][1] = 0;
		gr[7][2] = 0;
		
		gr[8][0] = 0;
		gr[8][1] = 0;
		gr[8][2] = 0;
		
		int[] ng = new int[2];
		ng[0] = Color.rgb(0, 102, 204);
		ng[1] = Color.rgb(255, 255, 255);
		
		DrawThread.setgrad(ng);
		
		a = new DispRes_Rel(stage, R.drawable.bg, activity.getResources(), 879, 240, 0, 300, 255, 0);
		b = new DispRes_Rel(stage, R.drawable.bg, activity.getResources(), 879, 240, 800, 300, 255, 0);
		
		for (int i = 0; i < 7; i++) {
			dj[i] = new PhysCircle(stage, (int) (Math.random() * 80) + 80, 0, (160 * i) + 80, 480, 255, stage.world);
		}
		
		box = new DispRes_Rel(stage, R.drawable.box, activity.getResources(), 150, 150, 325, 105, 255, 0);
		new Rect(stage, 800, 480, 0, 0, 0).setMouseDownEvent(new boxsplode());
		
		c1 = new Cloud(activity.getApplicationContext(), stage, stage.world, 850, -120);
		new Cloud(activity.getApplicationContext(), stage, stage.world, 150, -720);
		new Cloud(activity.getApplicationContext(), stage, stage.world, 600, -620);
		new Cloud(activity.getApplicationContext(), stage, stage.world, 900, -920);
		new Cloud(activity.getApplicationContext(), stage, stage.world, 300, -1120);
		new Cloud(activity.getApplicationContext(), stage, stage.world, 550, -1920);
		new Cloud(activity.getApplicationContext(), stage, stage.world, 800, -1320);
		
		stage.world.setContactListener(new hitListener());
		
		//PhysCircle ychar = new PhysCircle(d, 20, 1, 400, 0, 255, d.world);
		//ychar.getBody().applyImpulse(new Vec2(10, -10), ychar.getBody().getWorldCenter());
		//c = new Character(d, d.world);
		
		stage.draw = new tick();
		
		/*Timer t = new Timer();
		t.schedule(new tick(), 100, 10);*/
	}
	
	class hitListener implements ContactListener {

		@Override
		public void add(ContactPoint arg0) {
			Object i = arg0.shape1.getUserData();
			Object j = arg0.shape2.getUserData();
			Object t = null;
			if (i instanceof Cloud) {
				t = i;
				i = j;
				j = t;
			}
			if (i instanceof Character && j instanceof Cloud) {
				((Cloud) j).animate();
			}
		}

		@Override
		public void persist(ContactPoint arg0) {
			Shape i = arg0.shape1;
			Shape j = arg0.shape2;
			Shape t = null;
			if (i.getUserData() instanceof Cloud) {
				t = i;
				i = j;
				j = t;
			}
			if (i.getUserData() instanceof Character && j.getUserData() instanceof Cloud) {
				i.getBody().applyForce(new Vec2(3, -10), i.getBody().getWorldCenter());
			}
		}

		@Override
		public void remove(ContactPoint arg0) {
			
		}

		@Override
		public void result(ContactResult arg0) {
			
		}
		
	}
	
	int dist = 6;
	boolean ended = false;
	
	public class boxsplode implements MouseCallback {

		@Override
		public void sendCallback() {
			if (c == null) {
				box.destroy(stage);
				c = new Character(stage, activity.getResources(), R.drawable.eye, stage.world, 400, 240);
				new DispGif(activity.getApplicationContext(), stage, R.drawable.explosion, activity.getResources(), 764, 556, 18, -38, 255, 0, 1, 4);
			} else {
				c1.animate();
			}
		}

		@Override
		public void sendCallback(int x, int y) {
			sendCallback();
		}
		
	}
	
	public class tick implements Callback {
		
		@Override
		public void sendCallback() {
			if (c != null && c.end && !ended) {
				activity.runOnUiThread(new Runnable() {
					public void run() {
						new Highs(stage, activity, new Object[] {true, stage.camera.getX() / 10});
					}
				});
				ended = true;
			}
			if (!ended) {
				if (a.getScreenX() < -stage.camera.getScreenWidth()) {
					a.setX(stage.camera.getX() + stage.camera.getScreenWidth());
				}
				if (b.getScreenX() < -stage.camera.getScreenWidth()) {
					b.setX(stage.camera.getX() + stage.camera.getScreenWidth());
				}
				for (int i = 0; i < 7; i++) {
					if (dj[i].getScreenX() < -160) {
						dist++;
						dj[i].move((dist * 160) + 80, 480);
						dj[i].randomiseColor();
						dj[i].setRadius((int) (Math.random() * 80) + 80);
					}
				}
				
				int ng[] = new int[2];
				if (stage.camera.getY() > 7999) {
					
					ng[0] = Color.rgb(0, 0, 0);
					ng[1] = Color.rgb(0, 0, 0);
					
				} else if (stage.camera.getY() > 0) {
					
					int ny = stage.camera.getY() + 10;
					if (ny % 1000 < stage.camera.getY() % 1000) {
						ny -= (ny % 1000) + 1;
					}
					ng[0] = blend(gr[(int) Math.floor(stage.camera.getY() / 1000) + 1], gr[(int) Math.floor(stage.camera.getY() / 1000)], ny % 1000);
					ng[1] = blend(gr[(int) Math.floor(stage.camera.getY() / 1000) + 1], gr[(int) Math.floor(stage.camera.getY() / 1000)], stage.camera.getY() % 1000);
					
				} else {
					ng[0] = Color.rgb(255, 255, 255);
					ng[1] = Color.rgb(255, 255, 255);
				}
				DrawThread.setgrad(ng);
			}
		}
		
	}
	
	  public static int blend (int[] rgb1, int[] rgb2, double ratio)
	  {
		float r  = (float) ratio / 1000;
		float ir = (float) 1.0 - r;

		int color = Color.rgb((int) ((rgb1[0] * r) + (rgb2[0] * ir)), 
							(int) ((rgb1[1] * r) + (rgb2[1] * ir)), 
							(int) ((rgb1[2] * r) + (rgb2[2] * ir)));
		return color;
	  }
	
	int sx, sy, x, y, mx, my;
	
	@Override
	public boolean onTouch(MotionEvent event) {
		mx = (int) event.getX();
		my = (int) event.getY();
		
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			x = mx;
			y = my;
			sx = stage.camera.getX();
			sy = stage.camera.getY();
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			int nx = (int) (sx + (x - event.getX()));
			int ny = (int) (sy + (event.getY() - y));
			if (ny < 0) {
				ny = 0;
			}
			if (nx < stage.camera.getX()) {
				nx = stage.camera.getX();
			}
			
			if (c != null) {
				c.mouse(event.getX(), event.getY());
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			if (c != null) {
				c.lose = true;
			}
		}
		
		return false;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			new Main(stage, activity, new Object[]{true});
		}
		return super.onKeyDown(keyCode, event);
	}
	
}