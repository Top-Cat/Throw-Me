package thorgaming.throwme.DispObjs;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import thorgaming.throwme.Camera;
import thorgaming.throwme.Stage;
import thorgaming.throwme.DispObj;

public class DispRes extends DispObj {

	int width = 0;
	int height = 0;
	int did;
	Drawable drawable;
	int actualX = 0, actualY = 0, hit = 0;
	
	public int getScreenX() {
		return actualX;
	}
	
	public int getScreenY() {
		return actualY;
	}
	
	public DispRes(Stage stage, int did, Resources re, int width, int height, int x, int y, int alpha, int _h) {
		this.height = height;
		this.width = width;
		setX(x);
		setY(y);
		drawable = re.getDrawable(did);
		setAlpha(alpha);
		this.did = did;
		hit = _h;
		
		stage.objects.add(this);
	}
	
	@Override
	public boolean checkPress(int x, int y) {
		if (actualX <= x + hit && actualX + width >= x - hit && actualY <= y + hit && actualY + height >= y - hit) {
			return true;
		}
		return false;
	}

	@Override
	public void draw(Canvas c, Camera ca) {
		actualX = (getX() * ca.getScreenWidth()) / 800;
		actualY = (getY() * ca.getScreenHeight()) / 480;
		
		drawable.setBounds(actualX, actualY, ((getX() + width) * ca.getScreenWidth()) / 800, ((getY() + height) * ca.getScreenHeight()) / 480);
		drawable.setAlpha(getAlpha());
		drawable.draw(c);
	}
	
}