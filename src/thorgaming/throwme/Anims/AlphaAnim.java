package thorgaming.throwme.Anims;

import java.util.List;

import thorgaming.throwme.Anim;
import thorgaming.throwme.Callback;
import thorgaming.throwme.DevCard;
import thorgaming.throwme.DispObj;

public class AlphaAnim extends Anim {

	int p, s, e;
	
	public AlphaAnim(DevCard _d, DispObj obj, int start, int end, Callback c, long time) {
		super(c);
		p = (int) ( Math.abs(start - end) / (time / 10) );
		setObject(obj);
		obj.setAlpha(start);
		s = start;
		e = end;
		
		_d.anims.add(this);
	}

	@Override
	public void process(List<Anim> over) {
		int j = s > e ? -1 : 1;
		int val = getObject().getAlpha() + (p * j);
		
		if (val > Math.max(e, s)) {
			val = Math.max(e, s);
		} else if (val < Math.min(e, s)) {
			val = Math.min(e, s);
		}
		
		getObject().setAlpha(val);
		
		if ((val >= e && j == 1) || (val <= e && j == -1)) {
			over.add(this);
			callCallback();
		}
	}

}