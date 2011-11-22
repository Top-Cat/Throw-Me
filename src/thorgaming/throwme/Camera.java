package thorgaming.throwme;

public class Camera {
	
	private int x = 0;
	private int y = 0;
	private int screenWidth = 800;
	private int screenHeight = 480;
	
	public void setCameraX(int x) {
		this.x = x;
	}
	
	public void setCameraY(int y) {
		this.y = y;
	}
	
	public void setCameraXY(int x, int y) {
		setCameraX(x);
		setCameraY(y);
	}
	
	public void offSetCamera(int x, int y) {
		setCameraXY(this.x + x, this.y + y);
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