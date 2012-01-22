package com.thorgaming.throwme.billing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PurchaseDatabase {

	private SQLiteDatabase mDb;
	private DatabaseHelper mDatabaseHelper;

	public PurchaseDatabase(Context context) {
		mDatabaseHelper = new DatabaseHelper(context);
		mDb = mDatabaseHelper.getWritableDatabase();
	}

	public void close() {
		mDatabaseHelper.close();
	}

	private void insertOrder(String orderId, String productId, int state, long purchaseTime, String developerPayload) {
		ContentValues values = new ContentValues();
		values.put("_id", orderId);
		values.put("state", state);
		values.put("productId", productId);
		values.put("purchaseTime", purchaseTime);
		values.put("developerPayload", developerPayload);
		mDb.replace("throwmeHistory", null, values);
	}

	private void updatePurchasedItem(String productId, int quantity) {
		if (quantity == 0) {
			mDb.delete("throwmePurchased", "_id=?", new String[] {productId});
			return;
		}
		ContentValues values = new ContentValues();
		values.put("_id", productId);
		values.put("quantity", quantity);
		mDb.replace("throwmePurchased", null /* nullColumnHack */, values);
	}

	public int updatePurchase(String orderId, String productId, int purchaseState, long purchaseTime, String developerPayload) {
		insertOrder(orderId, productId, purchaseState, purchaseTime, developerPayload);
		return reCalculate(productId);
	}

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

	public Cursor queryAllPurchasedItems() {
		return mDb.query("throwmePurchased", new String[] {"_id", "quantity"}, null, null, null, null, null);
	}

	public boolean isPurchased(String id) {
		Cursor cursor = mDb.query("throwmePurchased", new String[] {"_id", "quantity"}, "_id=?", new String[] {id}, null, null, null);
		return cursor.getCount() > 0;
	}

	private class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(Context context) {
			super(context, "throwme.purchase.db", null, 1);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			createPurchaseTable(db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}

		private void createPurchaseTable(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE throwmeHistory (_id TEXT PRIMARY KEY, state INTEGER, productId TEXT, developerPayload TEXT, purchaseTime INTEGER)");
			db.execSQL("CREATE TABLE throwmePurchased (_id TEXT PRIMARY KEY, quantity INTEGER)");
		}
	}
}