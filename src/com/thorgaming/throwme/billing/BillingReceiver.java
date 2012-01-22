package com.thorgaming.throwme.billing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BillingReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if ("com.android.vending.billing.PURCHASE_STATE_CHANGED".equals(action)) {
			String signedData = intent.getStringExtra("inapp_signed_data");
			String signature = intent.getStringExtra("inapp_signature");
			purchaseStateChanged(context, signedData, signature);
		} else if ("com.android.vending.billing.IN_APP_NOTIFY".equals(action)) {
			String notifyId = intent.getStringExtra("notification_id");
			notify(context, notifyId);
		} else if ("com.android.vending.billing.RESPONSE_CODE".equals(action)) {
			long requestId = intent.getLongExtra("request_id", -1);
			int responseCodeIndex = intent.getIntExtra("response_code", 6);
			checkResponseCode(context, requestId, responseCodeIndex);
		}
	}

	private void purchaseStateChanged(Context context, String signedData, String signature) {
		Intent intent = new Intent("com.thorgaming.throwme.ACTION_PURCHASE_STATE_CHANGED");
		intent.setClass(context, BillingService.class);
		intent.putExtra("inapp_signed_data", signedData);
		intent.putExtra("inapp_signature", signature);
		context.startService(intent);
	}

	private void notify(Context context, String notifyId) {
		Intent intent = new Intent("com.thorgaming.throwme.GET_PURCHASE_INFORMATION");
		intent.setClass(context, BillingService.class);
		intent.putExtra("notification_id", notifyId);
		context.startService(intent);
	}

	private void checkResponseCode(Context context, long requestId, int responseCodeIndex) {
		Intent intent = new Intent("com.android.vending.billing.RESPONSE_CODE");
		intent.setClass(context, BillingService.class);
		intent.putExtra("request_id", requestId);
		intent.putExtra("response_code", responseCodeIndex);
		context.startService(intent);
	}

}