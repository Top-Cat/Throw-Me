package com.thorgaming.throwme.callback;

/**
 * @author Thomas Cheyney
 * @version 1.0
 */
public abstract class MouseCallback implements Callback {

	@Override
	public void sendCallback() {
		sendCallback(0, 0);
	}

	public abstract boolean sendCallback(int x, int y);

}