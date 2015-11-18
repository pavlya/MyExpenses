package com.tbg.myexpenses.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pavlya on 16/11/2015.
 */
public class ExpensesDbHelper extends SQLiteOpenHelper{

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Expenses.db";
    public static final String AMOUNT_VALUE = "amount";
    public static final String TITLE_VALUE = "title";
    public static final String DESCRIPTION_VALUE = "description";
    public static final String CATEGORY_VALUE = "category";

    private static ExpensesDbHelper instance = null;

    public synchronized static ExpensesDbHelper getInstance(Context ctxt){
        if(instance == null){
            instance = new ExpensesDbHelper(ctxt.getApplicationContext());
        }
        return instance;
    }

    private ExpensesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createDbString = "CREATE TABLE expenses(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                AMOUNT_VALUE + " DOUBLE NOT NULL, " + TITLE_VALUE + " TEXT, " +
                "" + DESCRIPTION_VALUE + " TEXT, " +
                CATEGORY_VALUE + " INTEGER);";
        db.execSQL(createDbString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
