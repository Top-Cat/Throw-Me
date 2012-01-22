package com.thorgaming.throwme.animation;

import java.util.List;

import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.callback.Callback;
import com.thorgaming.throwme.displayobjects.DispObj;

public class AlphaAnim extends Anim {

	private int unit;
	private int start;
	private int end;

	public AlphaAnim(DispObj obj, int start, int end, Callback callback, long time) {
		super(callback);
		unit = (int) (Math.abs(start - end) / (time / 10));
		setObject(obj);
		obj.setAlpha(start);
		this.start = start;
		this.end = end;

		ThrowMe.getInstance().stage.animations.add(this);
	}

	@Override
	public void process(List<Anim> over) {
		int j = start > end ? -1 : 1;
		int val = getObject().getAlpha() + unit * j;

		if (val > Math.max(end, start)) {
			val = Math.max(end, start);
		} else if (val < Math.min(end, start)) {
			val = Math.min(end, start);
		}

		getObject().setAlpha(val);

		if (val >= end && j == 1 || val <= end && j == -1) {
			over.add(this);
			callCallback();
		}
	}

}