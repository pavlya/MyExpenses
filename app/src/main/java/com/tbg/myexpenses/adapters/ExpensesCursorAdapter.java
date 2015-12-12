package com.tbg.myexpenses.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tbg.myexpenses.R;
import com.tbg.myexpenses.data.ExpensesDbHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Pavlya on 19/11/2015.
 */
public class ExpensesCursorAdapter extends CursorAdapter {

    SimpleDateFormat simpleDateFormat;
    String dateFormat;
//    private String[] category;


    public ExpensesCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
//        category = context.getResources().getStringArray(R.array.categories);
        dateFormat = "yyyy-MM-dd HH:mm";
        simpleDateFormat = new SimpleDateFormat(dateFormat);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final View itemView = LayoutInflater.from(context).inflate(R.layout.expenses_event, parent, false);
        return itemView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
//        final int id = cursor.getInt(cursor.getColumnIndex(ExpensesDbHelper.ID));
        final String title = cursor.getString(cursor.getColumnIndex(ExpensesDbHelper.TITLE_VALUE));
        final double amount = cursor.getDouble(cursor.getColumnIndex(ExpensesDbHelper.AMOUNT_VALUE));
        final String description = cursor.getString(cursor.getColumnIndex(ExpensesDbHelper.DESCRIPTION_VALUE));
        // TODO remove this sht
        // convert String to long and then convert it back to string :(
        final long dateValue = ExpensesDbHelper.getInstance(context).getDateFromString(
                cursor.getString(cursor.getColumnIndex(ExpensesDbHelper.DATE_VALUE)));
        final int category = cursor.getInt(cursor.getColumnIndex(ExpensesDbHelper.CATEGORY_VALUE));

        final TextView tvCategory  = (TextView)view.findViewById(R.id.tv_category);
        tvCategory.setText(context.getResources().getStringArray(R.array.categories_icons)[category]);
        final TextView tvDescription  = (TextView)view.findViewById(R.id.tv_description);
        tvDescription.setText(description);

        Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf") ;
        tvCategory.setTypeface(font);

        final TextView tvAmount = (TextView)view.findViewById(R.id.tv_amount);
        tvAmount.setText(String.valueOf(amount));

        final TextView tvTitle = (TextView)view.findViewById(R.id.tv_title);
        tvTitle.setText(title);

        final TextView tvDate = (TextView)view.findViewById(R.id.tv_date);
        // set date value
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateValue);
        Date date = calendar.getTime();
        tvDate.setText(String.valueOf(simpleDateFormat.format(date)));
    }
}
