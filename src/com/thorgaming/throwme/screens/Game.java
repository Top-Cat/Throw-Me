package com.thorgaming.throwme.screens;

import java.util.Random;

import com.thorgaming.throwme.DrawThread;
import com.thorgaming.throwme.HitListener;
import com.thorgaming.throwme.MouseCallback;
import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.displayobjects.game.Character;
import com.thorgaming.throwme.displayobjects.cloud.BoostCloud;
import com.thorgaming.throwme.displayobjects.cloud.ColouredCloud;
import com.thorgaming.throwme.displayobjects.cloud.LightningCloud;
import com.thorgaming.throwme.displayobjects.game.Crane;
import com.thorgaming.throwme.displayobjects.DispGif;
import com.thorgaming.throwme.displayobjects.DispRes;
import com.thorgaming.throwme.displayobjects.DispRes_Rel;
import com.thorgaming.throwme.displayobjects.shape.PhysCircle;
import com.thorgaming.throwme.displayobjects.shape.Rect;

import android.app.Activity;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.thorgaming.throwme.Callback;
import com.thorgaming.throwme.R;
import com.thorgaming.throwme.RenderPriority;

public class Game extends Screen {
	
	static { @SuppressWarnings("unused") byte dummy[] = new byte[ 8*1024*1024 ]; }	
	private int gradient[][] = new int[9][3];
	
	private DispRes hills1, hills2;
	private DispRes_Rel box;
	private PhysCircle[] randomHills = new PhysCircle[7];
	private Character character;
	private int hillDistance = 6;
	private int lastCrane = 6;
	private boolean ended = false;
	private Random random = new Random();
	
	private int cameraX;
	private int cameraY;
	private int downX;
	private int downY;
	private int mouseX;
	private int mouseY;
	
	public Game(Activity activity, Object[] data) {
		super(activity, data);
		
		ThrowMe.stage.camera.setCameraXY(0, 0);
		
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
		
		hills1 = (DispRes) new DispRes_Rel(R.drawable.bg).setWidth(879).setHeight(240).setY(300).addToScreen(RenderPriority.Highest);
		hills2 = (DispRes) new DispRes_Rel(R.drawable.bg).setWidth(879).setHeight(240).setX(800).setY(300).addToScreen(RenderPriority.Highest);
		
		for (int i = 0; i < 7; i++) {
			randomHills[i] = (PhysCircle) new PhysCircle(0).setRadius(random.nextInt(80) + 80).setX((160 * i) + 80).setY(480).addToScreen();
		}
		
		box = (DispRes_Rel) new DispRes_Rel(R.drawable.box).setWidth(150).setHeight(150).setX(325).setY(105).addToScreen();
		new Rect().setWidth(800).setHeight(480).setAlpha(0).addToScreen().setMouseDownEvent(new boxsplode());
		
		new BoostCloud().setX(850).setY(-120).addToScreen(RenderPriority.High);
		new BoostCloud().setX(150).setY(-720).addToScreen(RenderPriority.High);
		new BoostCloud().setX(600).setY(-620).addToScreen(RenderPriority.High);
		new BoostCloud().setX(900).setY(-920).addToScreen(RenderPriority.High);
		new BoostCloud().setX(300).setY(-1120).addToScreen(RenderPriority.High);
		new BoostCloud().setX(550).setY(-1920).addToScreen(RenderPriority.High);
		new BoostCloud().setX(800).setY(-1320).addToScreen(RenderPriority.High);
		new ColouredCloud().setWidth(133).setHeight(75).setX(800).setY(-2500).addToScreen(RenderPriority.High);
		new LightningCloud().setWidth(133).setHeight(75).setX(500).setY(-2800).addToScreen(RenderPriority.High);
		
		ThrowMe.stage.world.setContactListener(new HitListener());
		
		ThrowMe.stage.draw = new tick();
	}
	
	public class boxsplode implements MouseCallback {

		@Override
		public void sendCallback() {
			if (character == null) {
				box.destroy();
				character = (Character) new Character().setX(400).setY(240).addToScreen();
				new DispGif(R.drawable.explosion, 1, 4).setWidth(764).setHeight(556).setX(18).setY(-38).addToScreen();
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
						new Highs(activity, new Object[] {true, ThrowMe.stage.camera.getX() / 10});
					}
				});
				ended = true;
			}
			if (!ended) {
				if (hills1.getScreenX() < -ThrowMe.stage.camera.getScreenWidth()) {
					hills1.setX(ThrowMe.stage.camera.getX() + ThrowMe.stage.camera.getScreenWidth());
				}
				if (hills2.getScreenX() < -ThrowMe.stage.camera.getScreenWidth()) {
					hills2.setX(ThrowMe.stage.camera.getX() + ThrowMe.stage.camera.getScreenWidth());
				}
				for (int i = 0; i < 7; i++) {
					if (randomHills[i].getScreenX() < -160) {
						hillDistance++;
						randomHills[i].move((hillDistance * 160) + 80, 480);
						randomHills[i].randomiseColor();
						randomHills[i].setRadius((int) random.nextInt(80) + 80);
						
						if (lastCrane < hillDistance) {
							if (random.nextInt(40) < 10) {
								lastCrane = hillDistance + 3;
								new Crane().setX((hillDistance * 160) + random.nextInt(80)).setY(90 + random.nextInt(40)).addToScreen(RenderPriority.High);
							}
						}
					}
				}
				
				int ng[] = new int[2];
				if (ThrowMe.stage.camera.getY() > 7999) {
					
					ng[0] = Color.rgb(0, 0, 0);
					ng[1] = Color.rgb(0, 0, 0);
					
				} else if (ThrowMe.stage.camera.getY() > 0) {
					
					int ny = ThrowMe.stage.camera.getY() + 10;
					if (ny % 1000 < ThrowMe.stage.camera.getY() % 1000) {
						ny -= (ny % 1000) + 1;
					}
					ng[0] = blend(gradient[(int) Math.floor(ThrowMe.stage.camera.getY() / 1000) + 1], gradient[(int) Math.floor(ThrowMe.stage.camera.getY() / 1000)], ny % 1000);
					ng[1] = blend(gradient[(int) Math.floor(ThrowMe.stage.camera.getY() / 1000) + 1], gradient[(int) Math.floor(ThrowMe.stage.camera.getY() / 1000)], ThrowMe.stage.camera.getY() % 1000);
					
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
			cameraX = ThrowMe.stage.camera.getX();
			cameraY = ThrowMe.stage.camera.getY();
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			int newX = (int) (cameraX + (downX - event.getX()));
			int newY = (int) (cameraY + (event.getY() - downY));
			if (newY < 0) {
				newY = 0;
			}
			if (newX < ThrowMe.stage.camera.getX()) {
				newX = ThrowMe.stage.camera.getX();
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
			new Main(activity, new Object[]{true});
		}
		return super.onKeyDown(keyCode, event);
	}
	
}