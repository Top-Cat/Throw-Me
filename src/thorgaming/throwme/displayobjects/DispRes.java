package thorgaming.throwme.displayobjects;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import thorgaming.throwme.Camera;
import thorgaming.throwme.Stage;

public class DispRes extends DispObj {

	protected int width = 0;
	protected int height = 0;
	protected Drawable drawable;
	protected int actualX = 0;
	protected int actualY = 0;
	
	private int hitPadding = 0;
	
	public DispRes(Stage stage, int drawableId, Resources resources, int width, int height, int x, int y, int alpha, int hitPadding) {
		super(stage);
		this.height = height;
		this.width = width;
		setX(x);
		setY(y);
		drawable = resources.getDrawable(drawableId);
		setAlpha(alpha);
		this.hitPadding = hitPadding;
	}
	
	public int getScreenX() {
		return actualX;
	}
	
	public int getScreenY() {
		return actualY;
	}
	
	@Override
	public boolean checkPress(int x, int y) {
		if (actualX <= x + hitPadding && actualX + width >= x - hitPadding && actualY <= y + hitPadding && actualY + height >= y - hitPadding) {
			return true;
		}
		return false;
	}

	@Override
	public void draw(Canvas canvas, Camera camera) {
		actualX = (getX() * camera.getScreenWidth()) / 800;
		actualY = (getY() * camera.getScreenHeight()) / 480;
		
		drawable.setBounds(actualX, actualY, ((getX() + width) * camera.getScreenWidth()) / 800, ((getY() + height) * camera.getScreenHeight()) / 480);
		drawable.setAlpha(getAlpha());
		drawable.draw(canvas);
	}
	
}