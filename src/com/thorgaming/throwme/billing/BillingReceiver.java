package com.thorgaming.throwme.billing;

import com.thorgaming.throwme.ThrowMe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author Thomas Cheyney
 * @version 1.0
 */
public class BillingReceiver extends BroadcastReceiver {

	/**
	 * Receives calls back from the android system regarding the market
	 * These are then passed to the BillingService class to be saved etc.
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if ("com.android.vending.billing.PURCHASE_STATE_CHANGED".equals(action)) {
			String signedData = intent.getStringExtra("inapp_signed_data");
			String signature = intent.getStringExtra("inapp_signature");
			ThrowMe.getInstance().billingService.purchaseStateChanged(signedData, signature);
		} else if ("com.android.vending.billing.IN_APP_NOTIFY".equals(action)) {
			String notifyId = intent.getStringExtra("notification_id");
			ThrowMe.getInstance().billingService.notify(notifyId);
		} else if ("com.android.vending.billing.RESPONSE_CODE".equals(action)) {
			long requestId = intent.getLongExtra("request_id", -1);
			int responseCodeIndex = intent.getIntExtra("response_code", 6);
			ThrowMe.getInstance().billingService.checkResponseCode(requestId, responseCodeIndex);
		}
	}

}