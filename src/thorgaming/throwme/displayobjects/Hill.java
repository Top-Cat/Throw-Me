package thorgaming.throwme.displayobjects;

import java.util.Random;

import org.jbox2d.dynamics.World;

import thorgaming.throwme.Stage;

public class Hill extends PhysCircle {

	private Crane crane;
	private Random random = new Random();
	
	public Hill(Stage stage, int density, World world) {
		super(stage, density, world);
	}

	public void updateCrane() {
		if (random.nextInt(100) < 10) {
			if (crane == null) {
				crane = new Crane(stage);
			}
			crane.setX(getX());
			System.out.println(getX());
			crane.setY(getY() - 200);
		} else {
			if (crane != null) {
				crane.destroy(stage);
				crane = null;
			}
		}
	}
}