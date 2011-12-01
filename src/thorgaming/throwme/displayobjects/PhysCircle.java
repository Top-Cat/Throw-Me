package thorgaming.throwme.displayobjects;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import thorgaming.throwme.Stage;

public class PhysCircle extends Circle {
	
	private Body physicsBody;
	private World world;
	private CircleDef circle;
	private Shape shape;
	
	public PhysCircle(Stage stage, int density, World world) {
		super(stage);
		
		this.world = world;
		
		circle = new CircleDef();
		circle.radius = 1F;
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
	public Circle setRadius(int radius) {
		super.setRadius(radius);
		if (physicsBody != null) {
			physicsBody.destroyShape(shape);
			circle.radius = (float) radius / Stage.ratio;
			shape = physicsBody.createShape(circle);
			physicsBody.setMassFromShapes();
		}
		return this;
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
		super.destroy(stage);
		world.destroyBody(physicsBody);
	}
	
	@Override
	public int getX() {
		if (physicsBody == null) {
			return 0;
		}
		Vec2 position = physicsBody.getPosition();
		return (int) (position.x * Stage.ratio) - stage.camera.getX();
	}
	
	@Override
	public int getY() {
		if (physicsBody == null) {
			return 0;
		}
		Vec2 position = physicsBody.getPosition();
		return (int) (position.y * Stage.ratio) + stage.camera.getY();
	}
	
}