package thorgaming.throwme.Anims;

import java.util.List;

import thorgaming.throwme.Anim;
import thorgaming.throwme.Callback;
import thorgaming.throwme.Stage;
import thorgaming.throwme.DispObj;

public class XAnim extends Anim {

	int p, s, e;
	
	public XAnim(Stage stage, DispObj obj, int start, int end, Callback c, long time) {
		super(c);
		p = (int) ( Math.abs(start - end) / (time / 10) );
		setObject(obj);
		obj.setX(start);
		s = start;
		e = end;
		
		stage.anims.add(this);
	}

	@Override
	public void process(List<Anim> over) {
		int j = s > e ? -1 : 1;
		int val = getObject().getX() + (p * j);
		
		if (val > Math.max(e, s)) {
			val = Math.max(e, s);
		} else if (val < Math.min(e, s)) {
			val = Math.min(e, s);
		}
		
		getObject().setX(val);
		
		if ((val >= e && j == 1) || (val <= e && j == -1)) {
			over.add(this);
			callCallback();
		}
	}

}