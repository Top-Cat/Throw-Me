package thorgaming.throwme.DispObjs;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import thorgaming.throwme.Camera;
import thorgaming.throwme.Stage;
import thorgaming.throwme.DispObj;

public class Circle extends DispObj {

	int radius = 0;
	Paint paint = new Paint();
	int actualX, actualY;
	
	public int getScreenX() {
		return actualX;
	}
	
	public int getScreenY() {
		return actualY;
	}
	
	public Circle(Stage stage, int r, int x, int y, int alpha) {
		randomiseColor();

		radius = r;
		setX(x);
		setY(y);
		setAlpha(alpha);
		
		stage.objects.add(this);
	}
	
	public void setRadius(int r) {
		radius = r;
	}
	
	public void randomiseColor() {
		paint.setColor(Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
	}
	
	@Override
	public boolean checkPress(int x, int y) {
		int d = (int) Math.sqrt(Math.pow(actualX - x, 2) + Math.pow(actualY - y, 2));
		if (d < radius) {
			return true;
		}
		return false;
	}

	@Override
	public void draw(Canvas c, Camera ca) {
		actualX = (getX() * ca.getScreenWidth()) / 800;
		actualY = (getY() * ca.getScreenHeight()) / 480;
		
		paint.setAlpha(getAlpha());
		c.drawCircle(actualX, actualY, radius, paint);
	}
	
}