package com.example.restful.sqlite;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.restful.enitities.CartItem;

public class CartDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "cart.db";
    private static final int DATABASE_VERSION = 1;

    public CartDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE cart_items (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "product_name TEXT," +
                "product_price REAL," +
                "quantity INTEGER)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void addCartItem(CartItem cartItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("product_name", cartItem.getProductName());
        values.put("product_price", cartItem.getProductPrice());
        values.put("quantity", cartItem.getQuantity());
        db.insert("cart_items", null, values);
        db.close();
    }
}
