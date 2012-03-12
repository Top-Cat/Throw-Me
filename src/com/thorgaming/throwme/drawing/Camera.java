package com.thorgaming.throwme.drawing;

/**
 * Represents the camera through which the world is viewed
 * 
 * @author Thomas Cheyney
 * @version 1.0
 */
public class Camera {

	/**
	 * X position of the camera
	 */
	private int x = 0;
	/**
	 * Y position of the camera
	 */
	private int y = 0;
	/**
	 * Width of the phone's screen, used to calculate drawing scale
	 */
	private int screenWidth = 800;
	/**
	 * Height of the phone's screen, used to calculate drawing scale
	 */
	private int screenHeight = 480;

	/**
	 * Set the X position of the camera
	 * 
	 * @param x The new x position
	 */
	public void setCameraX(int x) {
		this.x = x;
	}

	/**
	 * Set the Y position of the camera
	 * 
	 * @param y The new y position
	 */
	public void setCameraY(int y) {
		this.y = y;
	}

	/**
	 * Set both the X+Y position of the camera
	 *  
	 * @param x The new x position
	 * @param y The new y position
	 */
	public void setCameraXY(int x, int y) {
		setCameraX(x);
		setCameraY(y);
	}

	/**
	 * Offset the camera from its current
	 *  
	 * @param x Value of x to offset by
	 * @param y Value of y to offset by
	 */
	public void offSetCamera(int x, int y) {
		setCameraXY(this.x + x, this.y + y);
	}

	/**
	 * Set the phone screen size
	 * 
	 * @param width The width (in pixels) of the phone's screen
	 * @param height The height (in pixels) of the phone's screen
	 */
	public void setScreen(int width, int height) {
		screenWidth = width;
		screenHeight = height;
	}

	/**
	 * Get the X position of the camera
	 * 
	 * @return X position of the camera
	 */
	public int getX() {
		return x;
	}

	/**
	 * Get the Y position of the camera
	 * 
	 * @return Y position of the camera
	 */
	public int getY() {
		return y;
	}

	/**
	 * Transform a world location to a screen draw location
	 * This offsets by the camera position and scales to place correctly on the screen
	 * 
	 * @param x World X location
	 * @return Transformed location
	 */
	public int transformRelativeX(int x) {
		return transformX(x - this.x);
	}

	/**
	 * Transform a screen location to a screen draw location
	 * This scales to place correctly on the screen
	 * 
	 * @param x X location
	 * @return Transformed location
	 */
	public int transformX(int x) {
		return x * getScreenWidth() / 800;
	}

	/**
	 * Transform a screen draw location to a screen location
	 * This is used to place the location of mouse events
	 * 
	 * @param x X draw location
	 */
	public int rTransformX(int x) {
		return x * 800 / getScreenWidth();
	}

	/**
	 * Transform a world location to a screen draw location
	 * This offsets by the camera position and scales to place correctly on the screen
	 * 
	 * @param y World Y location
	 * @return Transformed location
	 */
	public int transformRelativeY(int y) {
		return transformY(y + this.y);
	}

	/**
	 * Transform a screen location to a screen draw location
	 * This scales to place correctly on the screen
	 * 
	 * @param y Y location
	 * @return Transformed location
	 */
	public int transformY(int y) {
		return y * getScreenHeight() / 480;
	}

	/**
	 * Transform a screen draw location to a screen location
	 * This is used to place the location of mouse events
	 * 
	 * @param y Y draw location
	 */
	public int rTransformY(int y) {
		return y * 480 / getScreenHeight();
	}

	/**
	 * Gets the width of the phone's screen
	 * 
	 * @return Screen width
	 */
	public int getScreenWidth() {
		return screenWidth;
	}

	/**
	 * Gets the height of the phone's screen
	 * 
	 * @return Screen height
	 */
	public int getScreenHeight() {
		return screenHeight;
	}

}