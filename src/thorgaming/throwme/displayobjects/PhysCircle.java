package thorgaming.throwme.displayobjects;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import android.graphics.Canvas;

import thorgaming.throwme.Camera;
import thorgaming.throwme.Stage;

public class PhysCircle extends Circle {
	
	private Body physicsBody;
	private World world;
	private CircleDef circle;
	private Shape shape;
	
	public PhysCircle(Stage stage, int radius, int density, int x, int y, int alpha, World world) {
		super(stage, radius, x, y, alpha);
		
		this.world = world;
		
		circle = new CircleDef();  
		circle.radius = (float) radius / Stage.ratio;
		circle.density = density;
		circle.friction = (float) 0.5;
		circle.restitution = (float) 0.6;
		
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(new Vec2((float) getX() / Stage.ratio, (float) getY() / Stage.ratio));
		
		physicsBody = world.createBody(groundBodyDef);
		shape = physicsBody.createShape(circle);
		physicsBody.setMassFromShapes();
	}
	
	@Override
	public void setRadius(int radius) {
		super.setRadius(radius);
		if (physicsBody != null) {
			physicsBody.destroyShape(shape);
			circle.radius = (float) radius / Stage.ratio;
			shape = physicsBody.createShape(circle);
			physicsBody.setMassFromShapes();
		}
	}
	
	@Override
	public void move(int x, int y) {
		physicsBody.setXForm(new Vec2((float) x / Stage.ratio, (float) y / Stage.ratio), 0);
	}
	
	public Body getBody() {
		return physicsBody;
	}
	
	@Override
	public void destroy(Stage stage) {
		world.destroyBody(physicsBody);
		super.destroy(stage);
	}
	
	@Override
	public void draw(Canvas canvas, Camera camera) {
		Vec2 position = physicsBody.getPosition();
		
		setX((int) (position.x * Stage.ratio) - camera.getX());
		setY((int) (position.y * Stage.ratio) + camera.getY());
		
		super.draw(canvas, camera);
	}
	
}