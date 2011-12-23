package com.thorgaming.throwme.animation;

import java.util.List;

import com.thorgaming.throwme.Callback;
import com.thorgaming.throwme.displayobjects.DispObj;

public abstract class Anim {

	protected DispObj obj;
	protected Callback callback;

	public Anim(Callback callback) {
		setCallback(callback);
	}

	public void setObject(DispObj obj) {
		this.obj = obj;
	}

	public DispObj getObject() {
		return obj;
	}

	public void setCallback(Callback callback) {
		this.callback = callback;
	}

	public void callCallback() {
		if (callback != null) {
			callback.sendCallback();
		}
	}

	public abstract void process(List<Anim> toProcess);

}