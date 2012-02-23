package com.thorgaming.throwme.callback;

/**
 * @author Thomas Cheyney
 * @version 1.0
 */
public abstract class MouseCallback implements Callback {

	/**
	 * Ensures that calling the super method calls the finer method this class implements
	 */
	@Override
	public void sendCallback() {
		sendCallback(0, 0);
	}

	/**
	 * Allows simple storage of a method to call when a mouse operation may be important 
	 * 
	 * @param x X coordinate of the mouse
	 * @param y Y coordinate of the mouse
	 * @return True if no further objects should be checked. Useful if switching screen to prevent issues with widgets being removed 
	 */
	public abstract boolean sendCallback(int x, int y);

}