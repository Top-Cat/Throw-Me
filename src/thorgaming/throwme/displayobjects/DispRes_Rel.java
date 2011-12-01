package thorgaming.throwme.displayobjects;

import thorgaming.throwme.Camera;
import thorgaming.throwme.Stage;
import android.graphics.Canvas;

public class DispRes_Rel extends DispRes {

	public DispRes_Rel(Stage stage, int drawableId, int width, int height, int x, int y, int alpha, int hitPadding) {
		super(stage, drawableId, width, height, x, y, alpha, hitPadding);
	}
	
	@Override
	public void draw(Canvas canvas, Camera camera) {
		actualX = ((getX() - camera.getX()) * camera.getScreenWidth()) / 800;
		actualY = ((getY() + camera.getY()) * camera.getScreenHeight()) / 480;
		
		drawable.setBounds(actualX, actualY, (((getX() - camera.getX()) + width) * camera.getScreenWidth()) / 800, (((getY() + camera.getY()) + height) * camera.getScreenHeight()) / 480);
		drawable.setAlpha(getAlpha());
		drawable.draw(canvas);
	}
	
}