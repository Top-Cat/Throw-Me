package com.thorgaming.throwme.callback;

public abstract class MouseCallback implements Callback {

	@Override
	public void sendCallback() {
		sendCallback(0, 0);
	}

	public abstract boolean sendCallback(int x, int y);

}