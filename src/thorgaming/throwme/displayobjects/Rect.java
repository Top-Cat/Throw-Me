package thorgaming.throwme.displayobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import thorgaming.throwme.Camera;
import thorgaming.throwme.Stage;

public class Rect extends DispObj {

	protected int width;
	protected int height;
	public Paint paint = new Paint();
	protected int actualX;
	protected int actualY;
	
	public Rect(Stage stage, int width, int height, int x, int y, int alpha) {
		super(stage);
		randomiseColor();

		setSize(width, height);
		setX(x);
		setY(y);
		setAlpha(alpha);
	}
	
	public int getScreenX() {
		return actualX;
	}
	
	public int getScreenY() {
		return actualY;
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void randomiseColor() {
		paint.setColor(Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
	}
	
	@Override
	public boolean checkPress(int x, int y) {
		if (x > getX() && x < getX() + width && y > getY() && y < getY() + height) {
			return true;
		}
		return false;
	}

	@Override
	public void draw(Canvas canvas, Camera camera) {
		actualX = (getX() * camera.getScreenWidth()) / 800;
		actualY = (getY() * camera.getScreenHeight()) / 480;
		
		paint.setAlpha(getAlpha());
		canvas.drawRect(getX(), getY(), getX() + width, getY() + height, paint);
	}
	
}