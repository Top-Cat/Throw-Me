package com.thorgaming.throwme.billing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Maintains the sqlite database on the phone that records purchases
 * so that multiple queries to the market are not needed
 * 
 * @author Thomas Cheyney
 * @version 1.0
 */
public class PurchaseDatabase {

	/**
	 * Object that allows access to the database
	 */
	private SQLiteDatabase mDb;
	/**
	 * Database helper contains methods for creating and maintaining the tables
	 */
	private DatabaseHelper mDatabaseHelper;

	public PurchaseDatabase(Context context) {
		mDatabaseHelper = new DatabaseHelper(context);
		mDb = mDatabaseHelper.getWritableDatabase();
	}

	/**
	 * Adds a transaction to the history which can then be used to calculate purchased quantities
	 * 
	 * @param orderId Id of the order from the android market
	 * @param productId Id of the product in the android market
	 * @param state State of the transaction, purchase, return etc.
	 * @param purchaseTime When did the transaction occur
	 * @param developerPayload Return value specified by us
	 */
	private void insertOrder(String orderId, String productId, int state, long purchaseTime, String developerPayload) {
		ContentValues values = new ContentValues();
		values.put("_id", orderId);
		values.put("state", state);
		values.put("productId", productId);
		values.put("purchaseTime", purchaseTime);
		values.put("developerPayload", developerPayload);
		mDb.replace("throwmeHistory", null, values);
	}

	/**
	 * Update the quantity of an item in the purchased table
	 * 
	 * @param productId Id of the product in the android market
	 * @param quantity New quantity
	 */
	private void updatePurchasedItem(String productId, int quantity) {
		if (quantity == 0) {
			mDb.delete("throwmePurchased", "_id=?", new String[] {productId});
			return;
		}
		ContentValues values = new ContentValues();
		values.put("_id", productId);
		values.put("quantity", quantity);
		mDb.replace("throwmePurchased", null, values);
	}

	/**
	 * Called when the BillingService receives a call from the market
	 * and updates the tables accordingly
	 * 
	 * @param orderId Id of the order from the android market
	 * @param productId Id of the product in the android market
	 * @param purchaseState State of the transaction, purchase, return etc.
	 * @param purchaseTime When did the transaction occur
	 * @param developerPayload Return value specified by us
	 * @return New total purchased quantity of item
	 */
	public int updatePurchase(String orderId, String productId, int purchaseState, long purchaseTime, String developerPayload) {
		insertOrder(orderId, productId, purchaseState, purchaseTime, developerPayload);
		return reCalculate(productId);
	}

	/**
	 * Recalculate the purchased quantity of an item
	 * 
	 * @param productId Id of the product in the android market
	 * @return Total purchased quantity of item
	 */
	public int reCalculate(String productId) {
		Cursor cursor = mDb.query("throwmeHistory", new String[] {"_id", "state", "productId", "purchaseTime", "developerPayload"}, "productId=?", new String[] {productId}, null, null, null, null);
		if (cursor == null) {
			return 0;
		}
		int quantity = 0;
		try {
			while (cursor.moveToNext()) {
				if (cursor.getInt(1) == 0) {
					quantity += 1;
				}
			}

			updatePurchasedItem(productId, quantity);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return quantity;
	}

	/**
	 * Get a list of all purchased items and corresponding quantities
	 * 
	 * @return Cursor in a list of items
	 */
	public Cursor queryAllPurchasedItems() {
		return mDb.query("throwmePurchased", new String[] {"_id", "quantity"}, null, null, null, null, null);
	}

	/**
	 * Helper to check if any of an item have been purchased
	 * 
	 * @param productId Id of the product in the android market
	 * @return True if any of the item have been purchased
	 */
	public boolean isPurchased(String productId) {
		Cursor cursor = mDb.query("throwmePurchased", new String[] {"_id", "quantity"}, "_id=?", new String[] {productId}, null, null, null);
		return cursor.getCount() > 0;
	}
}