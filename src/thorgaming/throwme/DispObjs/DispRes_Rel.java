package thorgaming.throwme.DispObjs;

import thorgaming.throwme.Camera;
import thorgaming.throwme.Stage;
import android.content.res.Resources;
import android.graphics.Canvas;

public class DispRes_Rel extends DispRes {

	public DispRes_Rel(Stage stage, int did, Resources re, int width, int height, int x, int y, int alpha, int _h) {
		super(stage, did, re, width, height, x, y, alpha, _h);
	}
	
	@Override
	public void draw(Canvas c, Camera ca) {
		actualX = ((getX() - ca.getX()) * ca.getScreenWidth()) / 800;
		actualY = ((getY() + ca.getY()) * ca.getScreenHeight()) / 480;
		
		drawable.setBounds(actualX, actualY, (((getX() - ca.getX()) + width) * ca.getScreenWidth()) / 800, (((getY() + ca.getY()) + height) * ca.getScreenHeight()) / 480);
		drawable.setAlpha(getAlpha());
		drawable.draw(c);
	}
	
}