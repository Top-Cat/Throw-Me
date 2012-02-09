package com.thorgaming.throwme.billing;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashSet;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Base64;

import com.android.vending.billing.IMarketBillingService;
import com.thorgaming.throwme.ThrowMe;

public class BillingService extends Service implements ServiceConnection {

	private static IMarketBillingService mService;
	private boolean supported = false;
	private static Activity activity;
	private Random random = new Random();
	public static HashSet<Long> knownOnce = new HashSet<Long>();
	public static PurchaseDatabase purchases;
	public static long restoreId = 0;

	public BillingService() {

	}

	public BillingService(Activity activity) {
		super();
		BillingService.activity = activity;
		attachBaseContext(activity);
		purchases = new PurchaseDatabase(this);
		attachService();
	}

	private void attachService() {
		try {
			activity.startService(new Intent("com.android.vending.billing.MarketBillingService.BIND"));
			boolean bindResult = activity.bindService(new Intent("com.android.vending.billing.MarketBillingService.BIND"), this, Context.BIND_AUTO_CREATE);
			if (!bindResult) {
				System.out.println("Could not bind to the MarketBillingService.");
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onServiceConnected(ComponentName arg0, IBinder service) {
		mService = IMarketBillingService.Stub.asInterface(service);
		if (checkBillingSupported()) {
			supported = true;
			SharedPreferences prefs = activity.getPreferences(Context.MODE_PRIVATE);
			boolean initialized = prefs.getBoolean("dbINIT", false);
			if (!initialized) {
				long rand = random.nextLong();
				knownOnce.add(rand);

				Bundle request = makeRequestBundle("RESTORE_TRANSACTIONS");
				request.putLong("NONCE", rand);
				try {
					restoreId = mService.sendBillingRequest(request).getLong("REQUEST_ID", -1);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private Bundle makeRequestBundle(String method) {
		Bundle request = new Bundle();
		request.putString("BILLING_REQUEST", method);
		request.putInt("API_VERSION", 1);
		request.putString("PACKAGE_NAME", getPackageName());
		return request;
	}

	private boolean checkBillingSupported() {
		Bundle request = makeRequestBundle("CHECK_BILLING_SUPPORTED");
		try {
			Bundle response = mService.sendBillingRequest(request);
			int responseCode = response.getInt("RESPONSE_CODE");
			boolean billingSupported = responseCode == 0;
			return billingSupported;
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		if (intent != null) {
			String action = intent.getAction();
			System.out.println("RESPONSE! " + action);
			if ("com.thorgaming.throwme.GET_PURCHASE_INFORMATION".equals(action)) {
				String notifyId = intent.getStringExtra("notification_id");

				long rand = random.nextLong();
				knownOnce.add(rand);

				Bundle request = makeRequestBundle("GET_PURCHASE_INFORMATION");
				request.putLong("NONCE", rand);

				request.putStringArray("NOTIFY_IDS", new String[] {notifyId});
				try {
					mService.sendBillingRequest(request);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			} else if ("com.thorgaming.throwme.ACTION_PURCHASE_STATE_CHANGED".equals(action)) {
				String signedData = intent.getStringExtra("inapp_signed_data");
				String signature = intent.getStringExtra("inapp_signature");

				boolean verified = false;
				if (signature.length() > 0) {
					String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4GbR3FqjQIqFkxFBWoKqCmIXAEMwdK8E13+AQuMU4i0fVw8kLMFZbk7T1YPezQnBm6ozwJSBrQA+M4HOdKguqnGE+hDtFzWCq5/mZh7VM8/9Sow7EFvlbQll2DR/8OQE1aXGcRKEf51H9a7i5VswOsqwiTAP7BqtbGo/aujo1NxtwX/OYDGIIEx/V7r1lBQCfgNEM9+dn6Ahr4ETPVU9QLhyP2F99vKBhgJ4euQj0/zpaA0jjItMhrfTRAwPXVvWnh65+ECOlpQ6WNZZF2kHBjr5ocHH+zEJDGKrs0DOQ3WDiraoaqmBXRB85vHtQQRV/8KxJHpjtWC2k0eLrfoH4wIDAQAB";

					PublicKey key = generatePublicKey(base64EncodedPublicKey);
					verified = verify(key, signedData, signature);
					if (!verified) {
						System.out.println("Could not verify!");
					}
				}

				JSONObject jObject;
				JSONArray jTransactionsArray = null;
				int numTransactions = 0;
				long nonce = 0L;
				try {
					jObject = new JSONObject(signedData);

					nonce = jObject.optLong("nonce");
					if (knownOnce.contains(nonce)) {
						jTransactionsArray = jObject.optJSONArray("orders");
						if (jTransactionsArray != null) {
							numTransactions = jTransactionsArray.length();
						}

						for (int i = 0; i < numTransactions; i++) {
							JSONObject jElement = jTransactionsArray.getJSONObject(i);
							int response = jElement.getInt("purchaseState");

							String productId = jElement.getString("productId");
							long purchaseTime = jElement.getLong("purchaseTime");
							String orderId = jElement.optString("orderId", "");
							String notifyId = null;
							if (jElement.has("notificationId")) {
								notifyId = jElement.getString("notificationId");

								Bundle request = makeRequestBundle("CONFIRM_NOTIFICATIONS");
								request.putStringArray("NOTIFY_IDS", new String[] {notifyId});
								try {
									mService.sendBillingRequest(request);
								} catch (RemoteException e) {
									e.printStackTrace();
								}
							}
							String developerPayload = jElement.optString("developerPayload", null);

							if (response == 0 && !verified) {
								continue;
							}
							purchases.updatePurchase(orderId, productId, response, purchaseTime, developerPayload);
							ThrowMe.getInstance().billingDirty = true;
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				knownOnce.remove(nonce);
			} else if ("com.android.vending.billing.RESPONSE_CODE".equals(action)) {
				long requestId = intent.getLongExtra("request_id", -1);
				if (requestId == restoreId && restoreId > 0) {
					SharedPreferences prefs = activity.getPreferences(Context.MODE_PRIVATE);
					Editor edit = prefs.edit();
					edit.putBoolean("dbINIT", true);
					edit.commit();
				}
			}
		}
	}

	public void startPurchase(String marketId) {
		if (mService != null) {
			if (supported) {
				Bundle request = makeRequestBundle("REQUEST_PURCHASE");
				request.putString("ITEM_ID", marketId);
				try {
					Bundle response = mService.sendBillingRequest(request);
					PendingIntent pendingIntent = response.getParcelable("PURCHASE_INTENT");
					Intent intent = new Intent();
					if (pendingIntent == null) {
						System.out.println("Error in response!");
					} else {
						try {
							activity.startIntentSender(pendingIntent.getIntentSender(), intent, 0, 0, 0);
						} catch (SendIntentException e) {
							e.printStackTrace();
						}
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("Unsupported!");
			}
		} else {
			System.out.println("Service gone! :O");
			attachService();
		}
	}

	@Override
	public void onServiceDisconnected(ComponentName arg0) {
		mService = null;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public static PublicKey generatePublicKey(String encodedPublicKey) {
		try {
			byte[] decodedKey = Base64.decode(encodedPublicKey, Base64.DEFAULT);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeySpecException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static boolean verify(PublicKey publicKey, String signedData, String signature) {
		try {
			Signature sig = Signature.getInstance("SHA1withRSA");
			sig.initVerify(publicKey);
			sig.update(signedData.getBytes());
			if (sig.verify(Base64.decode(signature, Base64.DEFAULT))) {
				return true;
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		}
		return false;
	}

}