package thorgaming.throwme.displayobjects;

import java.util.Random;

import thorgaming.throwme.ThrowMe;

public class Hill extends PhysCircle {

	private Crane crane;
	private Random random = new Random();
	
	public Hill(int density) {
		super(density);
	}

	public void updateCrane() {
		if (random.nextInt(100) < 15) {
			if (crane == null) {
				crane = (Crane) new Crane().addToScreen();
			}
			crane.setX(getX() + ThrowMe.stage.camera.getX());
			crane.setY(getY() - 300);
		} else {
			if (crane != null) {
				crane.destroy();
				crane = null;
			}
		}
	}
}