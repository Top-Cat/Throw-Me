package thorgaming.throwme.Anims;

import java.util.List;

import thorgaming.throwme.Anim;
import thorgaming.throwme.Callback;
import thorgaming.throwme.Stage;
import thorgaming.throwme.DispObj;

public class XAnim extends Anim {

	int unit, start, end;
	
	public XAnim(Stage stage, DispObj obj, int start, int end, Callback callback, long time) {
		super(callback);
		unit = (int) ( Math.abs(start - end) / (time / 10) );
		setObject(obj);
		obj.setX(start);
		this.start = start;
		this.end = end;
		
		stage.animations.add(this);
	}

	@Override
	public void process(List<Anim> over) {
		int j = start > end ? -1 : 1;
		int val = getObject().getX() + (unit * j);
		
		if (val > Math.max(end, start)) {
			val = Math.max(end, start);
		} else if (val < Math.min(end, start)) {
			val = Math.min(end, start);
		}
		
		getObject().setX(val);
		
		if ((val >= end && j == 1) || (val <= end && j == -1)) {
			over.add(this);
			callCallback();
		}
	}

}