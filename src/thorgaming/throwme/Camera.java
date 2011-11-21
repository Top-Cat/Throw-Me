package thorgaming.throwme;

public class Camera {
	
	private int x = 0, y = 0, screenWidth = 800, screenHeight = 480;
	
	public void setCameraX(int _x) {
		setCameraXY(_x, y);
	}
	
	public void setCameraY(int _y) {
		setCameraXY(x, _y);
	}
	
	public void setCameraXY(int _x, int _y) {
		x = _x;
		y = _y;
	}
	
	public void offSetCamera(int _x, int _y) {
		setCameraXY(_x + x, _y + y);
	}
	
	public void setScreen(int width, int height) {
		screenWidth = width;
		screenHeight = height;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getScreenWidth() {
		return screenWidth;
	}
	
	public int getScreenHeight() {
		return screenHeight;
	}
	
}