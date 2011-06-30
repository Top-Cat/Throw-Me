package thorgaming.throwme.DispObjs;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import android.graphics.Canvas;

import thorgaming.throwme.Camera;
import thorgaming.throwme.DevCard;

public class PhysCircle extends Circle {
	
	Body Pbody;
	World w;
	CircleDef circle;
	BodyDef bd;
	Shape s;
	
	public PhysCircle(DevCard d, int r, int _d, int _x, int _y, int _alpha, World _w) {
		super(d, r, _x, _y, _alpha);
		
		w = _w;
		
		circle = new CircleDef();  
		circle.radius = (float) r / DevCard.ratio;
		System.out.println(circle.radius);
		circle.density = _d;
		circle.friction = (float) 0.5;
		circle.restitution = (float) 0.6;
        
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(new Vec2((float) x / DevCard.ratio, (float) y / DevCard.ratio));
        
        Pbody = w.createBody(groundBodyDef);
        s = Pbody.createShape(circle);
        Pbody.setMassFromShapes();
	}
	
	@Override
	public void setRadius(int r) {
		super.setRadius(r);
		Pbody.destroyShape(s);
		circle.radius = (float) r / DevCard.ratio;
		s = Pbody.createShape(circle);
		Pbody.setMassFromShapes();
	}
	
	@Override
	public void move(int _x, int _y) {
		Pbody.setXForm(new Vec2((float) _x / DevCard.ratio, (float) _y / DevCard.ratio), 0);
	}
	
	public Body getBody() {
		return Pbody;
	}
	
	@Override
	public void destroy(DevCard d) {
		w.destroyBody(Pbody);
		super.destroy(d);
	}
	
	@Override
	public void draw(Canvas c, Camera ca) {
		
		Vec2 p = Pbody.getPosition();
		
		x = (int) (p.x * DevCard.ratio) - ca.x;
		y = (int) (p.y * DevCard.ratio) + ca.y;
		
		super.draw(c, ca);
	}
	
}