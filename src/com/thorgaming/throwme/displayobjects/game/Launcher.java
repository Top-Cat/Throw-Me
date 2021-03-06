package com.thorgaming.throwme.displayobjects.game;

import org.jbox2d.common.Vec2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.callback.MouseCallback;
import com.thorgaming.throwme.displayobjects.DispObj;
import com.thorgaming.throwme.displayobjects.game.characters.Character;
import com.thorgaming.throwme.drawing.Camera;
import com.thorgaming.throwme.drawing.DrawThread;

/**
 * Controls the launch bar and launching the character
 * 
 * @author Thomas Cheyney
 * @version 1.0
 */
public class Launcher extends DispObj {

	/**
	 * Used to calculate the current value of the boost bar
	 */
	private double step = 0;
	/**
	 * Current bar value
	 */
	private double bar = 0;
	/**
	 * Paint used to draw the bar
	 */
	private Paint paint = new Paint();
	/**
	 * Paint used to draw the border around the bar
	 */
	private Paint paintStroke = new Paint();
	/**
	 * Used to make the speed the same on any device
	 */
	private long lastTime = 0;

	public Launcher(Character character) {
		this(character, 1);
	}

	public Launcher(final Character character, final float multiplier) {
		lastTime = System.currentTimeMillis();
		ThrowMe.getInstance().stage.drawThread.setPhysics(false);
		paint.setColor(Color.rgb(255, 32, 20));
		paintStroke.setColor(Color.rgb(220, 220, 220));
		paintStroke.setStyle(Style.STROKE);
		paintStroke.setStrokeWidth(10);

		character.setBalloonPre(20);

		setMouseDownEvent(new MouseCallback() {
			@Override
			public boolean sendCallback(int x, int y) {
				ThrowMe.getInstance().stage.drawThread.setPhysics(true);
				character.applyImpulse(new Vec2(45F, -60F).mul((float) bar * multiplier));
				DrawThread.toRemove.add(Launcher.this);
				return false;
			}
		});
	}

	@Override
	public void draw(Canvas canvas, Camera camera) {
		step += (System.currentTimeMillis() - lastTime) / 140f;
		lastTime = System.currentTimeMillis();
		bar = Math.abs(Math.sin(step) / Math.pow(1.12, step / 2.2));

		canvas.drawRect(10, (float) (400 - bar * 300), 50, 400, paint);
		canvas.drawRect(10, 100, 50, 400, paintStroke);
	}

	@Override
	public boolean checkPress(int x, int y) {
		return true;
	}

}