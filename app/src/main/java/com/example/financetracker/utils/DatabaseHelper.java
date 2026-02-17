package com.example.financetracker.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.financetracker.models.Transaction;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database name and version
    private static final String DATABASE_NAME = "FinanceTracker.db";
    // Increment version because we changed the table schema (added a column)
    private static final int DATABASE_VERSION = 1;

    // Table name and columns
    public static final String TABLE_TRANSACTIONS = "transactions";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USER_ID = "user_id"; // column for Firebase UID
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_DATE = "date";

    // Table creation statement including COLUMN_USER_ID
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_TRANSACTIONS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_ID + " TEXT, " +
                    COLUMN_TYPE + " TEXT, " +
                    COLUMN_CATEGORY + " TEXT, " +
                    COLUMN_AMOUNT + " REAL, " +
                    COLUMN_NOTE + " TEXT, " +
                    COLUMN_DATE + " TEXT" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Execute SQL to create the local table
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed and create a fresh one
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        onCreate(db);
    }

    // --- CRUD Operations ---

    // 1. Add a financial transaction linked to a specific user
    public long addTransaction(Transaction t) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, t.getUserId()); // Link to user
        values.put(COLUMN_TYPE, t.getType());
        values.put(COLUMN_CATEGORY, t.getCategory());
        values.put(COLUMN_AMOUNT, t.getAmount());
        values.put(COLUMN_NOTE, t.getNote());
        values.put(COLUMN_DATE, t.getDate());

        long id = db.insert(TABLE_TRANSACTIONS, null, values);
        db.close();
        return id;
    }

    // 2. Fetch transactions ONLY for the current user
    public List<Transaction> getAllTransactions(String currentUserId) {
        List<Transaction> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // 1. Define filter to fetch records only for the logged-in user
        String selection = COLUMN_USER_ID + " = ?";
        String[] selectionArgs = { currentUserId };

        // 2. Execute query sorted by ID descending (newest first)
        Cursor cursor = db.query(TABLE_TRANSACTIONS, null, selection, selectionArgs,
                null, null, COLUMN_ID + " DESC");

        // 3. Iterate through results and populate the list
        if (cursor.moveToFirst()) {
            do {
                Transaction t = new Transaction(
                        cursor.getString(1), // Fetch userId from column index 1
                        cursor.getString(2), // Fetch type from column index 2
                        cursor.getString(3), // Fetch category from column index 3
                        cursor.getDouble(4),
                        cursor.getString(5),
                        cursor.getString(6)
                );
                list.add(t);
            } while (cursor.moveToNext());
        }

        // 4. Close cursor and database to free up resources
        cursor.close();
        db.close();

        return list;
    }

    // 3. Delete a transaction belonging to the current user
    public void deleteTransaction(String userId, String note, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Added userId check to deletion to ensure security
        db.delete(TABLE_TRANSACTIONS,
                COLUMN_USER_ID + "=? AND " + COLUMN_NOTE + "=? AND " + COLUMN_DATE + "=?",
                new String[]{userId, note, date});
        db.close();
    }
}