package thorgaming.throwme;

import java.util.List;

public abstract class Anim {
	
	protected DispObj d;
	protected Callback c;
	
	public Anim(Callback c) {
		setCallback(c);
	}
	
	public void setObject(DispObj _d) {
		d = _d;
	}
	
	public DispObj getObject() {
		return d;
	}
	
	public void setCallback(Callback _c) {
		c = _c;
	}
	
	public void callCallback() {
		if (c != null) {
			c.sendCallback();
		}
	}
	
	public abstract void process(List<Anim> _d);
	
}