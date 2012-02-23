package com.thorgaming.throwme.animation;

import java.util.List;

import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.callback.Callback;
import com.thorgaming.throwme.displayobjects.DispObj;

/**
 * @author Thomas Cheyney
 * @version 1.0
 */
public abstract class Anim {

	/**
	 * How much is added to the current position each frame
	 */
	protected int unit;
	/**
	 * The initial alpha value
	 */
	protected int start;
	/**
	 * The desired alpha value
	 */
	protected int end;
	/**
	 * DispObj being animated
	 */
	private DispObj obj;
	/**
	 * Callback to call on completion of the animation
	 */
	private Callback callback;
	
	public Anim(DispObj obj, int start, int end, Callback callback, long time) {
		setCallback(callback);
		unit = (int) (Math.abs(start - end) / (time / 10));
		setObject(obj);
		obj.setAlpha(start);
		this.start = start;
		this.end = end;

		ThrowMe.getInstance().stage.animations.add(this);
	}

	/**
	 * Sets the object to be animated
	 * 
	 * @param obj The object
	 */
	public void setObject(DispObj obj) {
		this.obj = obj;
	}

	/**
	 * Gets the object being animated
	 * 
	 * @return The object
	 */
	public DispObj getObject() {
		return obj;
	}

	/**
	 * Set the callback to be called on completion
	 * 
	 * @param callback The callback
	 */
	public void setCallback(Callback callback) {
		this.callback = callback;
	}

	protected void callCallback() {
		if (callback != null) {
			callback.sendCallback();
		}
	}

	/**
	 * Makes the animation set the values to the next step
	 * 
	 * @param toProcess List of animations to remove once loop through is complete (Prevents concurrency issues)
	 */
	public abstract void process(List<Anim> toProcess);

}