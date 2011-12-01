package thorgaming.throwme.displayobjects;

import thorgaming.throwme.Camera;
import thorgaming.throwme.R;
import thorgaming.throwme.Stage;
import android.graphics.Canvas;

public class Crane extends DispRes_Rel {
	
	public Crane(Stage stage) {
		super(stage, R.drawable.crane, 200, 214, 0, 0, 1, 0);
	}
	
	@Override
	public void draw(Canvas canvas, Camera camera) {
		super.draw(canvas, camera);
	}

	@Override
	public boolean checkPress(int x, int y) {
		return false;
	}
	
}