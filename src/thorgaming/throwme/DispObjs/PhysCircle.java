package thorgaming.throwme.DispObjs;

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
	
	Body Pbody;
	World w;
	CircleDef circle;
	BodyDef bd;
	Shape s;
	
	public PhysCircle(Stage stage, int r, int _d, int x, int y, int alpha, World world) {
		super(stage, r, x, y, alpha);
		
		w = world;
		
		circle = new CircleDef();  
		circle.radius = (float) r / Stage.ratio;
		circle.density = _d;
		circle.friction = (float) 0.5;
		circle.restitution = (float) 0.6;
		
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(new Vec2((float) getX() / Stage.ratio, (float) getY() / Stage.ratio));
		
		Pbody = w.createBody(groundBodyDef);
		s = Pbody.createShape(circle);
		Pbody.setMassFromShapes();
	}
	
	@Override
	public void setRadius(int r) {
		super.setRadius(r);
		Pbody.destroyShape(s);
		circle.radius = (float) r / Stage.ratio;
		s = Pbody.createShape(circle);
		Pbody.setMassFromShapes();
	}
	
	@Override
	public void move(int _x, int _y) {
		Pbody.setXForm(new Vec2((float) _x / Stage.ratio, (float) _y / Stage.ratio), 0);
	}
	
	public Body getBody() {
		return Pbody;
	}
	
	@Override
	public void destroy(Stage d) {
		w.destroyBody(Pbody);
		super.destroy(d);
	}
	
	@Override
	public void draw(Canvas c, Camera ca) {
		Vec2 p = Pbody.getPosition();
		
		setX((int) (p.x * Stage.ratio) - ca.getX());
		setY((int) (p.y * Stage.ratio) + ca.getY());
		
		super.draw(c, ca);
	}
	
}