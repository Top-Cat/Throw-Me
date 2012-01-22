package com.thorgaming.throwme.displayobjects.game.characters;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.thorgaming.throwme.GameState;
import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.callback.MouseCallback;
import com.thorgaming.throwme.displayobjects.DispObj;
import com.thorgaming.throwme.drawing.Camera;
import com.thorgaming.throwme.drawing.Stage;

public abstract class Character extends DispObj {

	private GameState state = GameState.ON_SPRING;
	protected float mouseX, mouseY;

	protected Paint paint = new Paint();

	private int k = 16; // Spring constant
	private double damping = 2;
	private int length = 1;

	private float previousHeadX = 0;
	private float previousHeadY = 0;
	private double avgSpeed = 1;

	private boolean boost = false;
	protected short balloonBar = 0;

	public Character() {
		setMouseMoveEvent(new MouseCallback() {
			@Override
			public boolean sendCallback(int x, int y) {
				mouseX = x;
				mouseY = y;
				return false;
			}
		});

		paint.setColor(Color.rgb(255, 153, 0));
		paint.setStrokeWidth(1.2f);
	}

	@Override
	public DispObj setX(int x) {
		move(x, (int) (getMainBody().getPosition().y * Stage.ratio));
		return this;
	}

	@Override
	public DispObj setY(int y) {
		move((int) (getMainBody().getPosition().x * Stage.ratio), y);
		return this;
	}

	public void setGameState(GameState state) {
		this.state = state;
	}

	public GameState getGameState() {
		return state;
	}

	public abstract Body getMainBody();

	public abstract void applyImpulse(Vec2 impulse);

	@Override
	public void draw(Canvas canvas, Camera camera) {
		if (getGameState() == GameState.ON_SPRING) {
			Vec2 world1 = getMainBody().getPosition();
			Vec2 world2 = new Vec2(mouseX / Stage.ratio, mouseY / Stage.ratio);
			Vec2 d = world2.sub(world1);
			float dlen = d.normalize();
			Vec2 springForce = d.clone();
			springForce = springForce.mul((dlen - length) * k);
			getMainBody().applyForce(springForce, world1);
			Vec2 v1 = getMainBody().getLinearVelocityFromWorldPoint(world1);
			Vec2 v2 = new Vec2(0, 0);
			Vec2 v = v2.sub(v1);
			Vec2 dampingForce = d.clone();
			dampingForce = dampingForce.mul((float) (Vec2.dot(v, d) * damping));
			getMainBody().applyForce(dampingForce, world1);
			canvas.drawLine(camera.transformX((int) (getMainBody().getPosition().x * Stage.ratio)), camera.transformY((int) (getMainBody().getPosition().y * Stage.ratio)), camera.transformX((int) mouseX), camera.transformY((int) mouseY), paint);
		} else {
			if (ThrowMe.getInstance().stage.drawThread.isPhysicsRunning()) {
				float speedX = (getMainBody().getPosition().x - previousHeadX);
				float speedY = (getMainBody().getPosition().y - previousHeadY);
				byte direction = speedX < 0 ? (byte) -1 : 1;
				avgSpeed -= (avgSpeed - Math.sqrt(speedX * speedX + speedY * speedY) * direction) / 100;
				previousHeadX = getMainBody().getPosition().x;
				previousHeadY = getMainBody().getPosition().y;
				if (avgSpeed < 1 / Stage.ratio) {
					setGameState(GameState.END);
				}

				if (getMainBody().getPosition().y < -500 && getMainBody().getLinearVelocity().y < 0) {
					applyImpulse(new Vec2(0, 3));
				}
			}

			int nx = (int) (camera.getX() - (camera.getX() + (-Stage.ratio * getMainBody().getPosition().x + camera.getScreenWidth() / 2)) / 10);
			int ny = (int) (camera.getY() - (camera.getY() - (-Stage.ratio * getMainBody().getPosition().y + camera.getScreenHeight() / 2)) / 10);
			ny = ny < 0 ? camera.getY() : ny;
			nx = nx < camera.getX() ? camera.getX() : nx;
			camera.setCameraXY(nx, ny);
		}
	}

	public void setBoost(boolean boost) {
		this.boost = boost;
	}

	public boolean getBoost() {
		return this.boost;
	}

	public void setBalloonbar(short balloonBar) {
		this.balloonBar = balloonBar;
	}

	@Override
	public boolean checkPress(int x, int y) {
		return true;
	}

}