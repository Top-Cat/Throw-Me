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

public class Launcher extends DispObj {

	private double step = 0;
	private double bar = 0;
	private Paint paint = new Paint();
	private Paint paintStroke = new Paint();

	public Launcher(Character character) {
		this(character, 1);
	}

	public Launcher(final Character character, final float multiplier) {
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
				character.applyImpulse(new Vec2(55F, -80F).mul((float) bar * multiplier));
				DrawThread.toRemove.add(Launcher.this);
				return false;
			}
		});
	}

	@Override
	public void draw(Canvas canvas, Camera camera) {
		step += 0.15;
		bar = Math.abs(Math.sin(step) / Math.pow(1.12, step / 2.2));

		canvas.drawRect(10, (float) (400 - bar * 300), 50, 400, paint);
		canvas.drawRect(10, 100, 50, 400, paintStroke);
	}

	@Override
	public boolean checkPress(int x, int y) {
		return true;
	}

}