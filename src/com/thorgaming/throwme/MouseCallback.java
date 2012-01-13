package com.thorgaming.throwme;

public abstract class MouseCallback implements Callback {
	
	public void sendCallback() {
		sendCallback(0, 0);
	}
	
	public abstract void sendCallback(int x, int y);

}