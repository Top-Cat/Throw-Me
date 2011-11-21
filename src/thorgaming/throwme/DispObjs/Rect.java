package thorgaming.throwme.DispObjs;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import thorgaming.throwme.Camera;
import thorgaming.throwme.Stage;
import thorgaming.throwme.DispObj;

public class Rect extends DispObj {

	int width, height;
	public Paint paint = new Paint();
	int actualX, actualY;
	
	public int getScreenX() {
		return actualX;
	}
	
	public int getScreenY() {
		return actualY;
	}
	
	public Rect(Stage stage, int width, int height, int x, int y, int alpha) {
		randomiseColor();

		setSize(width, height);
		setX(x);
		setY(y);
		setAlpha(alpha);
		
		stage.objects.add(this);
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
	public void draw(Canvas c, Camera ca) {
		actualX = (getX() * ca.getScreenWidth()) / 800;
		actualY = (getY() * ca.getScreenHeight()) / 480;
		
		paint.setAlpha(getAlpha());
		c.drawRect(getX(), getY(), getX() + width, getY() + height, paint);
	}
	
}