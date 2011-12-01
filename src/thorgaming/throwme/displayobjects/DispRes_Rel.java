package thorgaming.throwme.displayobjects;

import thorgaming.throwme.Camera;
import android.graphics.Canvas;

public class DispRes_Rel extends DispRes {

	public DispRes_Rel(int drawableId) {
		super(drawableId);
	}
	
	@Override
	public void draw(Canvas canvas, Camera camera) {
		actualX = camera.transformX(getX() - camera.getX());
		actualY = camera.transformY(getY() + camera.getY());
		
		drawable.setBounds(actualX, actualY, (((getX() - camera.getX()) + getWidth()) * camera.getScreenWidth()) / 800, (((getY() + camera.getY()) + getHeight()) * camera.getScreenHeight()) / 480);
		drawable.setAlpha(getAlpha());
		drawable.draw(canvas);
	}
	
}