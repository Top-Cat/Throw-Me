package com.thorgaming.throwme.displayobjects.power;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.thorgaming.throwme.R;
import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.billing.BillingService;
import com.thorgaming.throwme.callback.MouseCallback;
import com.thorgaming.throwme.displayobjects.DispObj;
import com.thorgaming.throwme.displayobjects.game.characters.Characters;
import com.thorgaming.throwme.drawing.Camera;

/**
 * @author Thomas Cheyney
 * @version 1.0
 */
public class PowerInfo extends DispObj {

	private Characters thisInfo;
	private Paint paint = new Paint();

	private Drawable preview;
	private Drawable purchaseD;
	private Drawable selectedD;
	private Drawable unselectedD;

	private boolean purchased = false;
	private Rect hitBox = new Rect();
	private boolean selected = false;

	public PowerInfo(Characters thisInfo) {
		setInfo(thisInfo);

		purchaseD = ThrowMe.getInstance().stage.getResources().getDrawable(R.drawable.purchase);
		selectedD = ThrowMe.getInstance().stage.getResources().getDrawable(R.drawable.sel);
		unselectedD = ThrowMe.getInstance().stage.getResources().getDrawable(R.drawable.unsel);

		paint.setColor(Color.rgb(255, 255, 255));
		paint.setTextSize(30);
		paint.setSubpixelText(true);
		paint.setTextAlign(Align.CENTER);

		setMouseDownEvent(new MouseCallback() {
			@Override
			public boolean sendCallback(int x, int y) {
				if (!purchased) {
					ThrowMe.getInstance().billingService.startPurchase(PowerInfo.this.thisInfo.getMarketId());
				} else if (!selected) {
					ThrowMe.getInstance().customisationSettings.edit().putInt("char", PowerInfo.this.thisInfo.getId()).commit();
					selected = true;
				}
				return false;
			}
		});
	}

	public void setInfo(Characters thisInfo) {
		this.thisInfo = thisInfo;
		preview = ThrowMe.getInstance().stage.getResources().getDrawable(thisInfo.getDrawableId());
		update();
	}

	public void update() {
		purchased = thisInfo.getMarketId().length() == 0 || BillingService.purchases.isPurchased(thisInfo.getMarketId());
		selected = Characters.getFromId(ThrowMe.getInstance().customisationSettings.getInt("char", 0)) == thisInfo;
		if (!purchased && selected) {
			selected = false;
			ThrowMe.getInstance().customisationSettings.edit().putInt("char", 0).commit();
		}
	}

	@Override
	public void draw(Canvas canvas, Camera camera) {
		canvas.drawText(thisInfo.getName(), camera.transformX(getX() + 295), camera.transformY(getY()) + 30, paint);
		preview.setBounds(camera.transformX(getX() + 70), camera.transformY(getY() + 70), camera.transformX(getX() + 520), camera.transformY(getY() + 420));
		preview.draw(canvas);

		if (ThrowMe.getInstance().billingDirty) {
			ThrowMe.getInstance().billingDirty = false;
			update();
		}

		if (!purchased) {
			hitBox.set(camera.transformX(getX() + 40), camera.transformY(getY() + 380), camera.transformX(getX() + 240), camera.transformY(getY() + 435));
			purchaseD.setBounds(hitBox);
			purchaseD.draw(canvas);
		} else if (selected) {
			hitBox.set(camera.transformX(getX() + 40), camera.transformY(getY() + 375), camera.transformX(getX() + 213), camera.transformY(getY() + 435));
			selectedD.setBounds(hitBox);
			selectedD.draw(canvas);
		} else {
			hitBox.set(camera.transformX(getX() + 40), camera.transformY(getY() + 386), camera.transformX(getX() + 213), camera.transformY(getY() + 436));
			unselectedD.setBounds(hitBox);
			unselectedD.draw(canvas);
		}
	}

	@Override
	public boolean checkPress(int x, int y) {
		return hitBox.intersects(x - 3, y - 3, x + 3, y + 3);
	}
}