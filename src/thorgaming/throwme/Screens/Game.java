package thorgaming.throwme.Screens;

import android.app.Activity;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.MotionEvent;

import thorgaming.throwme.Callback;
import thorgaming.throwme.DrawThread;
import thorgaming.throwme.HitListener;
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
	private int gradient[][] = new int[9][3];
	
	private DispRes hills1, hills2;
	private DispRes_Rel box;
	private PhysCircle[] randomHills = new PhysCircle[7];
	private Character character;
	private int hillDistance = 6;
	private boolean ended = false;
	
	private int cameraX;
	private int cameraY;
	private int downX;
	private int downY;
	private int mouseX;
	private int mouseY;
	
	public Game(Stage stage, Activity activity, Object[] data) {
		super(stage, activity, data);
		
		gradient[0][0] = 255;
		gradient[0][1] = 255;
		gradient[0][2] = 255;
		
		gradient[1][0] = 0;
		gradient[1][1] = 102;
		gradient[1][2] = 204;
		
		gradient[2][0] = 255;
		gradient[2][1] = 255;
		gradient[2][2] = 0;
		
		gradient[3][0] = 255;
		gradient[3][1] = 153;
		gradient[3][2] = 0;
		
		gradient[4][0] = 153;
		gradient[4][1] = 0;
		gradient[4][2] = 51;
		
		gradient[5][0] = 51;
		gradient[5][1] = 255;
		gradient[5][2] = 102;
		
		gradient[6][0] = 0;
		gradient[6][1] = 51;
		gradient[6][2] = 204;
		
		gradient[7][0] = 0;
		gradient[7][1] = 0;
		gradient[7][2] = 0;
		
		gradient[8][0] = 0;
		gradient[8][1] = 0;
		gradient[8][2] = 0;
		
		int[] ng = new int[2];
		ng[0] = Color.rgb(0, 102, 204);
		ng[1] = Color.rgb(255, 255, 255);
		
		DrawThread.setgrad(ng);
		
		hills1 = new DispRes_Rel(stage, R.drawable.bg, activity.getResources(), 879, 240, 0, 300, 255, 0);
		hills2 = new DispRes_Rel(stage, R.drawable.bg, activity.getResources(), 879, 240, 800, 300, 255, 0);
		
		for (int i = 0; i < 7; i++) {
			randomHills[i] = new PhysCircle(stage, (int) (Math.random() * 80) + 80, 0, (160 * i) + 80, 480, 255, stage.world);
		}
		
		box = new DispRes_Rel(stage, R.drawable.box, activity.getResources(), 150, 150, 325, 105, 255, 0);
		new Rect(stage, 800, 480, 0, 0, 0).setMouseDownEvent(new boxsplode());
		
		new Cloud(activity.getApplicationContext(), stage, stage.world, 850, -120);
		new Cloud(activity.getApplicationContext(), stage, stage.world, 150, -720);
		new Cloud(activity.getApplicationContext(), stage, stage.world, 600, -620);
		new Cloud(activity.getApplicationContext(), stage, stage.world, 900, -920);
		new Cloud(activity.getApplicationContext(), stage, stage.world, 300, -1120);
		new Cloud(activity.getApplicationContext(), stage, stage.world, 550, -1920);
		new Cloud(activity.getApplicationContext(), stage, stage.world, 800, -1320);
		
		stage.world.setContactListener(new HitListener());
		
		stage.draw = new tick();
	}
	
	public class boxsplode implements MouseCallback {

		@Override
		public void sendCallback() {
			if (character == null) {
				box.destroy(stage);
				character = new Character(stage, activity.getResources(), R.drawable.eye, stage.world, 400, 240);
				new DispGif(activity.getApplicationContext(), stage, R.drawable.explosion, activity.getResources(), 764, 556, 18, -38, 255, 0, 1, 4);
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
			if (character != null && character.end && !ended) {
				activity.runOnUiThread(new Runnable() {
					public void run() {
						new Highs(stage, activity, new Object[] {true, stage.camera.getX() / 10});
					}
				});
				ended = true;
			}
			if (!ended) {
				if (hills1.getScreenX() < -stage.camera.getScreenWidth()) {
					hills1.setX(stage.camera.getX() + stage.camera.getScreenWidth());
				}
				if (hills2.getScreenX() < -stage.camera.getScreenWidth()) {
					hills2.setX(stage.camera.getX() + stage.camera.getScreenWidth());
				}
				for (int i = 0; i < 7; i++) {
					if (randomHills[i].getScreenX() < -160) {
						hillDistance++;
						randomHills[i].move((hillDistance * 160) + 80, 480);
						randomHills[i].randomiseColor();
						randomHills[i].setRadius((int) (Math.random() * 80) + 80);
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
					ng[0] = blend(gradient[(int) Math.floor(stage.camera.getY() / 1000) + 1], gradient[(int) Math.floor(stage.camera.getY() / 1000)], ny % 1000);
					ng[1] = blend(gradient[(int) Math.floor(stage.camera.getY() / 1000) + 1], gradient[(int) Math.floor(stage.camera.getY() / 1000)], stage.camera.getY() % 1000);
					
				} else {
					ng[0] = Color.rgb(255, 255, 255);
					ng[1] = Color.rgb(255, 255, 255);
				}
				DrawThread.setgrad(ng);
			}
		}
		
	}
	
	public static int blend (int[] rgb1, int[] rgb2, double ratio) {
		float r  = (float) ratio / 1000;
		float ir = (float) 1.0 - r;

		int color = Color.rgb((int) ((rgb1[0] * r) + (rgb2[0] * ir)), 
							(int) ((rgb1[1] * r) + (rgb2[1] * ir)), 
							(int) ((rgb1[2] * r) + (rgb2[2] * ir)));
		return color;
	}
	
	@Override
	public boolean onTouch(MotionEvent event) {
		mouseX = (int) event.getX();
		mouseY = (int) event.getY();
		
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			downX = mouseX;
			downY = mouseY;
			cameraX = stage.camera.getX();
			cameraY = stage.camera.getY();
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			int newX = (int) (cameraX + (downX - event.getX()));
			int newY = (int) (cameraY + (event.getY() - downY));
			if (newY < 0) {
				newY = 0;
			}
			if (newX < stage.camera.getX()) {
				newX = stage.camera.getX();
			}
			
			if (character != null) {
				character.mouse(event.getX(), event.getY());
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			if (character != null) {
				character.lose = true;
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