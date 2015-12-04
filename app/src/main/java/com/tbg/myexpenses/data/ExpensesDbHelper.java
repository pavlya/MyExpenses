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

    public static final String TABLE_CATEGORIES = "categories_table";
    public static final String CATEGORY_NAME = "category_name";


    public static final long DAY_VALUE = 24 * 60 * 60 * 1000;
    public static final long WEEK_VALUE = DAY_VALUE * 7;
    public static final long MONTH_VALUE = DAY_VALUE * 31;


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
        ContentValues contentValues = new ContentValues();
        String createCategoryDB = "CREATE TABLE " + TABLE_CATEGORIES + "(" + ID +
                " INTEGER PRIMARY KEY, " + CATEGORY_NAME + " TEXT);";
        db.execSQL(createCategoryDB);
        String [] categories = new String[] {"General", "Food", "Vacation", "Electronics"};
        for(int i = 0; i <categories.length; i++){
            contentValues.put(ID, i);
            contentValues.put(CATEGORY_NAME, categories[i]);

            db.insert(TABLE_CATEGORIES, null,contentValues);
        }

        String createDbString = "CREATE TABLE " + TABLE_EXPENSES + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                AMOUNT_VALUE + " DOUBLE NOT NULL, " + TITLE_VALUE + " TEXT, " +
                "" + DESCRIPTION_VALUE + " TEXT, " +
                "" + DATE_VALUE + " DOUBLE NOT NULL, " +
                CATEGORY_VALUE + " INTEGER);";
        db.execSQL(createDbString);
    }

    public Cursor getCategoryData(){
        return  getReadableDatabase().query(TABLE_CATEGORIES, null, null, null, null, null, null);
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

    public void updateExpenseItem(ExpensesItem item, long id){
        ContentValues contentValues = new ContentValues();
        contentValues.put(AMOUNT_VALUE, item.getAmount());
        contentValues.put(TITLE_VALUE, item.getTitle());
        contentValues.put(DESCRIPTION_VALUE, item.getExplanation());
        contentValues.put(DATE_VALUE, item.getDate());
        contentValues.put(CATEGORY_VALUE, item.getCategory());
        String whereClause = ID + " = ?";
        String [] whereArgs = {"" + id};
        getWritableDatabase().update(TABLE_EXPENSES, contentValues, whereClause, whereArgs);
    }

    public void deleteExpenseItem(long id){
        String whereClause = ID + " = ?";
        String [] whereArgs = {"" + id};
        getReadableDatabase().delete(TABLE_EXPENSES, whereClause, whereArgs);
    }

    public ExpensesItem getItem(long id){
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

    public List<ExpensesItem> getExpensesItem(){
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

    public Cursor getAllItemsCursor(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_EXPENSES, null);
        return cursor;
    }

    public Cursor getExpensesBeforeSpecifiedDate(long specifiedDate){
        // TODO implement cursor
        long currentTime = System.currentTimeMillis();
        long timeToLook = currentTime - specifiedDate;
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_EXPENSES + " WHERE " + DATE_VALUE + " >= " +timeToLook, null);
        return cursor;
    }

    public Cursor getTodayExpenses (){
        return getExpensesBeforeSpecifiedDate(DAY_VALUE);
    }

    public Cursor getLastWeekExpenses(){
        return getExpensesBeforeSpecifiedDate(WEEK_VALUE);
    }

    public Cursor getLastMontExpenses(){
        return getExpensesBeforeSpecifiedDate(MONTH_VALUE);
    }

    public Cursor getCursorByCategory(int category){
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_EXPENSES + " WHERE " + CATEGORY_VALUE + " = " +category, null);
        return cursor;
    }

    public Cursor getGroupedByCategory(){
//        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_EXPENSES
//                + " GROUP BY " + CATEGORY_VALUE, null);
        // Selecting distinct
        Cursor cursor = getReadableDatabase().query(true, TABLE_EXPENSES,
                null, null, null, CATEGORY_VALUE, null, null, null);

        return cursor;
    }

    public void clearDb(){
        getWritableDatabase().delete(TABLE_EXPENSES, null, null);
    }

    public Cursor getAmountByCategory(int category){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT sum(amount) from " + TABLE_EXPENSES + " WHERE "
                + CATEGORY_VALUE + "=" + category, null);
//        Cursor cursor = getReadableDatabase().rawQuery("SELECT sum(amount) from " + TABLE_EXPENSES, null);
        return cursor;
    }

    public double getTotalAmount(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT sum(amount) from " + TABLE_EXPENSES, null);
        double amount = 0;
        if(cursor.moveToFirst()){
            amount = cursor.getDouble(0);
        }
        if(cursor!=null && !cursor.isClosed()){
            cursor.close();
        }
        return amount;
    }
}
