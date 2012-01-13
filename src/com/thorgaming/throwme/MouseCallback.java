package com.thorgaming.throwme;

public abstract class MouseCallback implements Callback {
	
	public void sendCallback() {
		sendCallback(0, 0);
	}
	
	public abstract boolean sendCallback(int x, int y);

}