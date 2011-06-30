package thorgaming.throwme;

public class Camera {
	
	public int x = 0, y = 0, w = 800, h = 480;
	
	public void SetCameraX(int _x) {
		SetCameraXY(_x, y);
	}
	
	public void SetCameraY(int _y) {
		SetCameraXY(x, _y);
	}
	
	public void SetCameraXY(int _x, int _y) {
		x = _x;
		y = _y;
	}
	
	public void OffSetCamera(int _x, int _y) {
		SetCameraXY(_x + x, _y + y);
	}
	
	public void setScreen(int _w, int _h) {
		w = _w;
		h = _h;
	}
	
}