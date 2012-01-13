package com.thorgaming.throwme.displayobjects.game;

import org.jbox2d.common.Vec2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;

import com.thorgaming.throwme.Camera;
import com.thorgaming.throwme.MouseCallback;
import com.thorgaming.throwme.R;
import com.thorgaming.throwme.Stage;
import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.displayobjects.DispObj;

public class HUD extends DispObj {

	private Paint barPaint = new Paint();
	private Paint barSurroundPaint = new Paint();
	private int balloonBar = 500;
	private boolean balloons = false;
	private Character character;
	private Drawable drawableBalloons;
	private byte coolDown = 0;
	private Paint paint = new Paint();
	
	public HUD(Character character) {
		setMouseMoveEvent(new MouseCallback() {
			@Override
			public boolean sendCallback(int x, int y) {
				if (HUD.this.character.lose && balloonBar > 0) {
					balloons = true;
				}
				return false;
			}
		});
		
		setMouseDownEvent(new MouseCallback() {
			@Override
			public boolean sendCallback(int x, int y) {
				if (balloonBar > 0 && coolDown == -50) {
					coolDown = 127;
					synchronized (ThrowMe.stage.drawThread.physicsSync) {
						Vec2 linearVelocity = HUD.this.character.bodyHead.getLinearVelocity();
						HUD.this.character.bodyHead.setLinearVelocity(new Vec2(Math.max(linearVelocity.x, 3), Math.min(linearVelocity.y, -3)));
					}
				}
				return false;
			}
		});
		
		this.character = character;
		
		drawableBalloons = ThrowMe.stage.getResources().getDrawable(R.drawable.balloon);
		
		barPaint.setColor(Color.rgb(40, 69, 255));
		barSurroundPaint.setColor(Color.rgb(220, 220, 220));
		barSurroundPaint.setStyle(Style.STROKE);
		barSurroundPaint.setStrokeWidth(4);
		
		paint.setColor(Color.rgb(255, 153, 0));
		paint.setTextSize(30);
		paint.setSubpixelText(true);
		paint.setStrokeWidth(1);
	}
	
	public boolean isActive() {
		return balloons;
	}
	
	@Override
	public void draw(Canvas canvas, Camera camera) {
		if (balloons) {
			Vec2 p = character.bodyHead.getPosition();
			int actualX = camera.transformRelativeX((int) (p.x * Stage.ratio));
			int actualY = camera.transformRelativeY((int) (p.y * Stage.ratio));
			
			drawableBalloons.setBounds(actualX - 19, actualY - 128, actualX + 15, actualY - 18);
			
			canvas.save();
			canvas.rotate((float) (Math.sin((float) balloonBar / 20 - 4.1887902) * 20), actualX, actualY - 20);
			drawableBalloons.draw(canvas);
			canvas.restore();
			canvas.save();
			canvas.rotate((float) (Math.sin((float) balloonBar / 20) * 20), actualX, actualY - 20);
			drawableBalloons.draw(canvas);
			canvas.restore();
			canvas.save();
			canvas.rotate((float) (Math.sin((float) balloonBar / 20 - 2.0943951) * 20), actualX, actualY - 20);
			drawableBalloons.draw(canvas);
			canvas.restore();
			
			character.bodyHead.applyImpulse(new Vec2(0.02F, -0.13F), character.bodyHead.getWorldCenter().add(new Vec2((float) (20 * Math.sin(character.bodyHead.getAngle())) / Stage.ratio, (float) (20 * Math.cos(character.bodyHead.getAngle())) / Stage.ratio)));
			balloonBar--;
			balloons = false;
		}
		
		if (character.lose) {
			canvas.drawRect(camera.transformX(610), camera.transformY(60), camera.transformX((int) (balloonBar / 2.777 + 610)), camera.transformY(75), barPaint);
			canvas.drawRect(camera.transformX(610), camera.transformY(60), camera.transformX(790), camera.transformY(75), barSurroundPaint);
			
			canvas.drawText("Distance: " + camera.getX() / 10 + "m", 10, 40, paint);
			String altitudeText = "Altitude: " + (int) (-(character.bodyHead.getWorldCenter().y - 12) * 1.9) + "m";
			android.graphics.Rect bounds = new android.graphics.Rect();
			paint.getTextBounds(altitudeText, 0, altitudeText.length(), bounds);
			canvas.drawText(altitudeText, camera.getScreenWidth() - bounds.width() - 10, 40, paint);
		}
		
		if (coolDown > -50) {
			coolDown--;
		}
	}
	
	@Override
	public boolean checkPress(int x, int y) {
		return true;
	}
	
}