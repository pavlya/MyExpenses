package com.tbg.myexpenses.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

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
    public static final String TABLE_EXPENSES = "expenses";
    public static final String DATE_VALUE = "date";
    public static final String ID = "_id";


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
        String createDbString = "CREATE TABLE " + TABLE_EXPENSES + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                AMOUNT_VALUE + " DOUBLE NOT NULL, " + TITLE_VALUE + " TEXT, " +
                "" + DESCRIPTION_VALUE + " TEXT, " +
                "" + DATE_VALUE + " DOUBLE NOT NULL, " +
                CATEGORY_VALUE + " INTEGER);";
        db.execSQL(createDbString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addExpenseItem(ExpensesItem item){
        ContentValues contentValues = new ContentValues();
        contentValues.put(AMOUNT_VALUE, item.getAmount());
        contentValues.put(TITLE_VALUE, item.getTitle());
        contentValues.put(DESCRIPTION_VALUE, item.getExplanation());
        contentValues.put(DATE_VALUE, item.getDate());
        contentValues.put(CATEGORY_VALUE, item.getCategory());
        getWritableDatabase().insert(TABLE_EXPENSES, null, contentValues);
    }

    public void updateExpenseItem(ExpensesItem item, int id){
        ContentValues contentValues = new ContentValues();
        contentValues.put(AMOUNT_VALUE, item.getAmount());
        contentValues.put(TITLE_VALUE, item.getTitle());
        contentValues.put(DESCRIPTION_VALUE, item.getExplanation());
        contentValues.put(DATE_VALUE, item.getDate());
        contentValues.put(CATEGORY_VALUE, item.getCategory());
        String whereClause = ID + " = ?";
        String [] whereArgs = {"" + id};
        getWritableDatabase().update(TABLE_EXPENSES, contentValues,whereClause, whereArgs);
    }

    public void deleteExpenseItem(int id){
        String whereClause = ID + " = ?";
        String [] whereArgs = {"" + id};
        getReadableDatabase().delete(TABLE_EXPENSES, whereClause, whereArgs);
    }

    public ExpensesItem getItem(int id){
        ExpensesItem expensesItem = new ExpensesItem();
        String[] columns = {ID, AMOUNT_VALUE, TITLE_VALUE, DESCRIPTION_VALUE, DATE_VALUE, CATEGORY_VALUE};
        String selection = ID + " = ?";
        String [] whereArgs = {String.valueOf(id)};
        Cursor cursor = getReadableDatabase().query(TABLE_EXPENSES,
                columns, selection, whereArgs,
                null,null,null,null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        expensesItem.setDate((cursor.getLong(4)));
        expensesItem.setExplanation(cursor.getString(3));
        expensesItem.setAmount(cursor.getDouble(1));
        expensesItem.set_id(cursor.getInt(0));
        expensesItem.setcategory(cursor.getInt(5));
        expensesItem.setTitle(cursor.getString(2));
        return expensesItem;
    }

    public List<ExpensesItem> getExpenseItem(){
        List<ExpensesItem> expensesItemList = new ArrayList<>();
        // Select all query
        String selectQuery = "SELECT * FROM " + TABLE_EXPENSES;
        Cursor cursor = getWritableDatabase().rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do{
                ExpensesItem expensesItem = new ExpensesItem();
                expensesItem.setDate((cursor.getLong(4)));
                expensesItem.setExplanation(cursor.getString(3));
                expensesItem.setAmount(cursor.getDouble(1));
                expensesItem.set_id(cursor.getInt(0));
                expensesItem.setcategory(cursor.getInt(5));
                expensesItem.setTitle(cursor.getString(2));
            } while (cursor.moveToNext());
        }
        return expensesItemList;
    }
}
