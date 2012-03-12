package com.thorgaming.throwme.displayobjects.power;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.drawable.Drawable;

import com.thorgaming.throwme.R;
import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.billing.BillingService;
import com.thorgaming.throwme.callback.MouseCallback;
import com.thorgaming.throwme.displayobjects.DispObj;
import com.thorgaming.throwme.displayobjects.game.characters.Characters;
import com.thorgaming.throwme.drawing.Camera;

/**
 * Display object that shows information
 * about power-ups and allows selection
 * 
 * @author Thomas Cheyney
 * @version 1.0
 */
public class PowerInfo extends DispObj {

	/**
	 * Character currently being displayed
	 */
	private Characters thisInfo;
	/**
	 * Paint to draw the title
	 */
	private Paint paint = new Paint();
	/**
	 * Drawable to show the selected power-up
	 */
	private Drawable preview;
	/**
	 * Drawable to show on unpurchased power-ups
	 */
	private Drawable purchaseD;
	/**
	 * Drawable to show on the selected power-up
	 */
	private Drawable selectedD;
	/**
	 * Drawable to show on purchased but unselected power-ups
	 */
	private Drawable unselectedD;
	/**
	 * If the current power-up has been purchased
	 */
	private boolean purchased = false;
	/**
	 * If the current power-up is selected
	 */
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
		
		setHitPadding(6);
	}

	/**
	 * Sets the currently displayed power-up
	 * 
	 * @param thisInfo New power-up to display
	 */
	public void setInfo(Characters thisInfo) {
		this.thisInfo = thisInfo;
		preview = ThrowMe.getInstance().stage.getResources().getDrawable(thisInfo.getDrawableId());
		update();
	}

	/**
	 * Update purchased and selected booleans by querying the market
	 */
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
}