package com.thorgaming.throwme.displayobjects.game;

import org.jbox2d.common.Vec2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.thorgaming.throwme.Camera;
import com.thorgaming.throwme.MouseCallback;
import com.thorgaming.throwme.Stage;
import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.displayobjects.DispObj;

public class Launcher extends DispObj {

	private double step = 0;
	private double bar = 0;
	private Paint paint = new Paint();
	private Paint paintStroke = new Paint();

	public Launcher(final Character character) {
		ThrowMe.stage.drawThread.setPhysics(false);
		paint.setColor(Color.rgb(43, 53, 255));
		paintStroke.setColor(Color.rgb(43, 53, 255));
		paintStroke.setStyle(Style.STROKE);

		setMouseDownEvent(new MouseCallback() {
			@Override
			public void sendCallback() {
				ThrowMe.stage.drawThread.setPhysics(true);
				character.bodyHead.applyImpulse(new Vec2(55F, -80F).mul((float) bar), character.bodyHead.getWorldCenter().add(new Vec2((float) (20 * Math.sin(character.bodyHead.getAngle())) / Stage.ratio, (float) (20 * Math.cos(character.bodyHead.getAngle())) / Stage.ratio)));
				destroy();
			}

			@Override
			public void sendCallback(int x, int y) {
				sendCallback();
			}
		});
	}

	@Override
	public void draw(Canvas canvas, Camera camera) {
		step += 0.1;
		bar = Math.abs(Math.sin(step) / Math.pow(1.12, step / 2.2));

		canvas.drawRect(10, (float) (400 - bar * 300), 50, 400, paint);
		canvas.drawRect(10, 100, 50, 400, paintStroke);
	}

	@Override
	public boolean checkPress(int x, int y) {
		return true;
	}

}