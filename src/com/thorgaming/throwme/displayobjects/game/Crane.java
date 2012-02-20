package com.thorgaming.throwme.displayobjects.game;

import java.util.Random;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.thorgaming.throwme.R;
import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.displayobjects.DispRes_Rel;
import com.thorgaming.throwme.displayobjects.Sensor;
import com.thorgaming.throwme.displayobjects.game.characters.Character;
import com.thorgaming.throwme.drawing.Camera;
import com.thorgaming.throwme.drawing.DrawThread;
import com.thorgaming.throwme.drawing.RenderPriority;
import com.thorgaming.throwme.drawing.Stage;

/**
 * @author Thomas Cheyney
 * @version 1.0
 */
public class Crane extends DispRes_Rel implements Sensor {

	/**
	 * Music notes drawable that hang from the crane
	 */
	private Drawable notes;
	/**
	 * Random generator used to generate initial position and speed
	 */
	private Random random = new Random();
	/**
	 * Position in the motion of the notes from the crane, angle = 40sin(time)
	 * Initial position is randomly generated
	 */
	private float time = random.nextInt(360);
	/**
	 * Speed at which time increases
	 */
	private float speed = random.nextFloat() * 2 + 1;
	/**
	 * Used for hit detection with the character, not drawn
	 */
	protected Body physicsBody;
	/**
	 * Prevents launcher from triggering twice
	 */
	private boolean hit = false;

	public Crane() {
		super(R.drawable.crane);
		setWidth(200);
		setHeight(292);

		CircleDef wreckingBall = new CircleDef();
		wreckingBall.isSensor = true;
		wreckingBall.radius = 40 / Stage.ratio;
		wreckingBall.density = 0;
		wreckingBall.userData = this;

		BodyDef wreckingBallBodyDef = new BodyDef();
		wreckingBallBodyDef.position.set(new Vec2(getX() / Stage.ratio, getY() / Stage.ratio));

		physicsBody = ThrowMe.getInstance().stage.world.createBody(wreckingBallBodyDef);
		physicsBody.createShape(wreckingBall);
		physicsBody.setMassFromShapes();

		notes = ThrowMe.getInstance().stage.getResources().getDrawable(R.drawable.swingingnotes);
	}

	@Override
	public void draw(Canvas canvas, Camera camera) {
		if (camera.transformRelativeX(getX() - 67) < -350) {
			DrawThread.toRemove.add(this);
		}
		super.draw(canvas, camera);
		canvas.save();
		float angle = (float) (40 * Math.sin(Math.toRadians(time)));
		physicsBody.setXForm(new Vec2((float) (getX() + 207 - 97 * Math.sin(Math.toRadians(angle)) - 9 * Math.cos(Math.toRadians(angle))) / Stage.ratio, (float) (getY() + 41 + 97 * Math.cos(Math.toRadians(angle)) + 9 * Math.sin(Math.toRadians(angle))) / Stage.ratio), 0);

		if (ThrowMe.getInstance().stage.drawThread.isPhysicsRunning()) {
			time += speed;
			if (time > 360) {
				time = time % 360;
			}
		}

		canvas.rotate(angle, camera.transformRelativeX(getX() + 198), camera.transformRelativeY(getY() + 54));
		notes.setBounds(camera.transformRelativeX(getX() + 175), camera.transformRelativeY(getY() + 43), camera.transformRelativeX(getX()) + 231, camera.transformRelativeY(getY()) + 170);
		notes.draw(canvas);
		canvas.restore();
	}

	@Override
	public boolean checkPress(int x, int y) {
		return false;
	}

	@Override
	public void hit(Shape otherShape) {
		if (((Character) otherShape.getUserData()).getMainBody().getShapeList() == otherShape && !hit) {
			hit = true;
			new Launcher((Character) otherShape.getUserData()).addToScreen(RenderPriority.Lowest);
		}
	}

	@Override
	public void persistContact(Shape otherShape) {

	}

	@Override
	public void destroy() {
		super.destroy();
		ThrowMe.getInstance().stage.world.destroyBody(physicsBody);
	}

}