package thorgaming.throwme.displayobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import thorgaming.throwme.Camera;
import thorgaming.throwme.Stage;

public class Circle extends DispObj {

	private int radius = 0;
	public Paint paint = new Paint();
	private int actualX;
	private int actualY;
	
	public Circle(Stage stage, int radius, int x, int y, int alpha) {
		randomiseColor();

		setRadius(radius);
		setX(x);
		setY(y);
		setAlpha(alpha);
		
		addToScreen(stage);
	}
	
	public int getScreenX() {
		return actualX;
	}
	
	public int getScreenY() {
		return actualY;
	}
	
	public void setRadius(int radius) {
		this.radius = radius;
	}
	
	public void randomiseColor() {
		paint.setColor(Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
	}
	
	@Override
	public boolean checkPress(int x, int y) {
		int distance = (int) Math.sqrt(Math.pow(actualX - x, 2) + Math.pow(actualY - y, 2));
		if (distance < radius) {
			return true;
		}
		return false;
	}

	@Override
	public void draw(Canvas canvas, Camera camera) {
		actualX = (getX() * camera.getScreenWidth()) / 800;
		actualY = (getY() * camera.getScreenHeight()) / 480;
		
		paint.setAlpha(getAlpha());
		canvas.drawCircle(actualX, actualY, radius, paint);
	}
	
}