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

/**
 * Abstract class that characters extend
 * 
 * @author Thomas Cheyney
 * @version 1.0
 */
public abstract class Character extends DispObj {

	/**
	 * The game state of the character
	 */
	private GameState state = GameState.ON_SPRING;
	/**
	 * Last mouse X position, used to calculate spring
	 */
	protected float mouseX = 400;
	/**
	 * Last mouse Y position, used to calculate spring
	 */
	protected float mouseY = 100;
	/**
	 * Prevents the character from just falling if you tap the screen at the start of the game
	 */
	public float throwTimeout = 5;
	/**
	 * Paint used to draw the "spring" line
	 */
	protected Paint paint = new Paint();
	/**
	 * Spring constant
	 */
	private int k = 16;
	/**
	 * Damping on the spring
	 */
	private double damping = 2;
	/**
	 * Length of the spring
	 */
	private int length = 1;
	/**
	 * Previous X location of the character, used to calculate speed
	 */
	private float previousHeadX = 0;
	/**
	 * Previous Y location of the character, used to calculate speed
	 */
	private float previousHeadY = 0;
	/**
	 * Current average speed
	 */
	private double avgSpeed = 1;
	/**
	 * If the balloon bar is currently being used, this will be true
	 */
	private boolean boost = false;
	/**
	 * The current value of the balloon bar, used to generate
	 * positions for the balloons above the character
	 */
	protected short balloonBar = 0;
	/**
	 * Prevents balloons being used briefly after being launched or similar
	 */
	private int balloonPre = 20;

	public Character() {
		setMouseMoveEvent(new MouseCallback() {
			@Override
			public boolean sendCallback(int x, int y) {
				mouseX = x;
				mouseY = y;
				throwTimeout--;
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

	/**
	 * Set the current game state
	 * 
	 * @param state New state of the game
	 */
	public void setGameState(GameState state) {
		this.state = state;
	}

	/**
	 * Gets the current game state
	 * 
	 * @return The state of the game
	 */
	public GameState getGameState() {
		return state;
	}

	/**
	 * Gets the body of the character that can be thought
	 * to represent it's position and can have forces or
	 * impulses applied
	 * 
	 * @return Physics body
	 */
	public abstract Body getMainBody();

	/**
	 * Allows impulses to be applied to the character
	 * 
	 * @param impulse Impulse to be applied
	 */
	public abstract void applyImpulse(Vec2 impulse);

	@Override
	public void draw(Canvas canvas, Camera camera) {
		if (getGameState() == GameState.ON_SPRING) {
			Vec2 world1 = getMainBody().getPosition();
			Vec2 world2 = new Vec2(mouseX / Stage.ratio, mouseY / Stage.ratio);
			Vec2 d = world2.sub(world1);
			float dlen = d.normalize();
			Vec2 springForce = d.clone().mul((dlen - length) * k);
			getMainBody().applyForce(springForce, world1);
			Vec2 v1 = getMainBody().getLinearVelocityFromWorldPoint(world1);
			Vec2 v2 = new Vec2(0, 0);
			Vec2 v = v2.sub(v1);
			Vec2 dampingForce = d.clone().mul((float) (Vec2.dot(v, d) * damping));
			getMainBody().applyForce(dampingForce, world1);
			canvas.drawLine(camera.transformX((int) (getMainBody().getPosition().x * Stage.ratio)), camera.transformY((int) (getMainBody().getPosition().y * Stage.ratio)), camera.transformX((int) mouseX), camera.transformY((int) mouseY), paint);
		} else {
			if (ThrowMe.getInstance().stage.drawThread.isPhysicsRunning()) {
				float speedX = getMainBody().getPosition().x - previousHeadX;
				float speedY = getMainBody().getPosition().y - previousHeadY;
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

				if (balloonPre > 0) {
					balloonPre--;
				}
			}

			int nx = (int) (camera.getX() - (camera.getX() + (-Stage.ratio * getMainBody().getPosition().x + camera.getScreenWidth() / 2)) / 9);
			int ny = (int) (camera.getY() - (camera.getY() - (-Stage.ratio * getMainBody().getPosition().y + camera.getScreenHeight() / 2)) / 10);
			ny = ny < 0 ? camera.getY() : ny;
			nx = nx < camera.getX() ? camera.getX() : nx;
			camera.setCameraXY(nx, ny);
		}
	}

	/**
	 * Sets the time before balloons can be used,
	 * called by the launcher etc.
	 * 
	 * @param balloonPre Time before balloons can be used
	 */
	public void setBalloonPre(int balloonPre) {
		this.balloonPre = balloonPre;
	}

	/**
	 * Sets if the balloon bar is currently being used
	 * 
	 * @param boost True if balloon bar in use
	 */
	public void setBoost(boolean boost) {
		this.boost = boost && balloonPre == 0;
	}

	/**
	 * Gets if boost is being applied, ie balloon bar
	 * in use and no time before balloons can be used
	 * 
	 * @return True if boost is being applied
	 */
	public boolean getBoost() {
		return boost;
	}

	/**
	 * Set the value remaining on the balloon bar
	 * 
	 * @param balloonBar New value of the balloon bar
	 */
	public void setBalloonBar(short balloonBar) {
		this.balloonBar = balloonBar;
	}

	@Override
	public boolean checkPress(int x, int y) {
		return true;
	}

}