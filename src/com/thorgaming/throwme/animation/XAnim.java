package com.thorgaming.throwme.animation;

import java.util.List;

import com.thorgaming.throwme.callback.Callback;
import com.thorgaming.throwme.displayobjects.DispObj;

/**
 * @author Thomas Cheyney
 * @version 1.0
 */
public class XAnim extends Anim {

	public XAnim(DispObj obj, int start, int end, Callback callback, long time) {
		super(obj, start, end, callback, time);
	}
	
	@Override
	public void process(List<Anim> over) {
		int j = start > end ? -1 : 1;
		int val = getObject().getX() + unit * j;

		if (val > Math.max(end, start)) {
			val = Math.max(end, start);
		} else if (val < Math.min(end, start)) {
			val = Math.min(end, start);
		}

		getObject().setX(val);

		if (val >= end && j == 1 || val <= end && j == -1) {
			over.add(this);
			callCallback();
		}
	}

}