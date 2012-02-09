package com.thorgaming.throwme.displayobjects.game;

import org.jbox2d.common.Vec2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

import com.thorgaming.throwme.GameState;
import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.callback.MouseCallback;
import com.thorgaming.throwme.displayobjects.DispObj;
import com.thorgaming.throwme.displayobjects.game.characters.Character;
import com.thorgaming.throwme.drawing.Camera;

/**
 * @author Thomas Cheyney
 * @version 1.0
 */
public class HUD extends DispObj {

	private Paint barPaint = new Paint();
	private Paint barSurroundPaint = new Paint();
	private short balloonBar = 500;
	private Character character;
	private byte coolDown = 0;
	private Paint paint = new Paint();

	public HUD(Character character) {
		setMouseMoveEvent(new MouseCallback() {
			@Override
			public boolean sendCallback(int x, int y) {
				if (HUD.this.character.getGameState() == GameState.LOOSE && balloonBar > 0) {
					HUD.this.character.setBalloonbar(balloonBar);
					HUD.this.character.setBoost(true);
				}
				return false;
			}
		});

		setMouseDownEvent(new MouseCallback() {
			@Override
			public boolean sendCallback(int x, int y) {
				if (balloonBar > 0 && coolDown == -50) {
					coolDown = 127;
					synchronized (ThrowMe.getInstance().stage.drawThread.physicsSync) {
						Vec2 linearVelocity = HUD.this.character.getMainBody().getLinearVelocity();
						HUD.this.character.getMainBody().setLinearVelocity(new Vec2(Math.max(linearVelocity.x, 3), Math.min(linearVelocity.y, -3)));
					}
				}
				return false;
			}
		});

		this.character = character;

		barPaint.setColor(Color.rgb(40, 69, 255));
		barSurroundPaint.setColor(Color.rgb(220, 220, 220));
		barSurroundPaint.setStyle(Style.STROKE);
		barSurroundPaint.setStrokeWidth(4);

		paint.setColor(Color.rgb(255, 153, 0));
		paint.setTextSize(30);
		paint.setSubpixelText(true);
		paint.setStrokeWidth(1);
	}

	@Override
	public void draw(Canvas canvas, Camera camera) {
		if (character.getGameState() == GameState.LOOSE) {
			canvas.drawRect(camera.transformX(610), camera.transformY(60), camera.transformX((int) (balloonBar / 2.777 + 610)), camera.transformY(75), barPaint);
			canvas.drawRect(camera.transformX(610), camera.transformY(60), camera.transformX(790), camera.transformY(75), barSurroundPaint);

			canvas.drawText("Distance: " + camera.getX() / 10 + "m", 10, 40, paint);
			String altitudeText = "Altitude: " + (int) (-(character.getMainBody().getPosition().y - 12) * 1.9) + "m";
			Rect bounds = new Rect();
			paint.getTextBounds(altitudeText, 0, altitudeText.length(), bounds);
			canvas.drawText(altitudeText, camera.getScreenWidth() - bounds.width() - 10, 40, paint);
		}

		if (character.getBoost()) {
			balloonBar--;
			character.setBoost(false);
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