package com.tbg.myexpenses.fragments;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ResourceCursorTreeAdapter;
import android.widget.TextView;

import com.tbg.myexpenses.R;
import com.tbg.myexpenses.data.ExpensesDbHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Pavlya on 30/11/2015.
 */
public class ExpensesCursorTreeAdapter extends ResourceCursorTreeAdapter {
    Context ctxt;
    SimpleDateFormat simpleDateFormat;
    String dateFormat;
    private Typeface font;

    public ExpensesCursorTreeAdapter(Context context, Cursor cursor, int groupLayout, int childLayout) {
        super(context, cursor, groupLayout, childLayout);
        this.ctxt = context;
        dateFormat = "yyyy-MM-dd HH:mm";
        simpleDateFormat = new SimpleDateFormat(dateFormat);
        font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
    }

    @Override
    protected Cursor getChildrenCursor(Cursor groupCursor) {
        int categoryId = groupCursor.getColumnIndex(ExpensesDbHelper.CATEGORY_VALUE);
        return ExpensesDbHelper.getInstance(ctxt).getCursorByCategory(groupCursor.getInt(categoryId));
    }

    @Override
    protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
        TextView tvCategoryNumber = (TextView)view.findViewById(R.id.tv_header_category);
        int category = cursor.getInt(cursor.getColumnIndex(ExpensesDbHelper.CATEGORY_VALUE));
        TextView tvHeaderAmount = (TextView)view.findViewById(R.id.tv_header_amount);

        Cursor curs = ExpensesDbHelper.getInstance(ctxt).getAmountByCategory(category);

        double amount = 0;
        if(curs.moveToFirst()){
            amount = curs.getDouble(0);
        }

        tvHeaderAmount.setText(String.valueOf(amount));
        // get the value from array and set the icon
        tvCategoryNumber.setText(context.getResources().getStringArray(R.array.categories_icons)[category]);
        tvCategoryNumber.setTypeface(font);
    }

    @Override
    protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {
//        TextView tvAmount = (TextView)view.findViewById(R.id.tv_child_amount);
//        TextView tvTitle = (TextView)view.findViewById(R.id.tv_child_title);
//
//        String title = cursor.getString(cursor.getColumnIndex(ExpensesDbHelper.TITLE_VALUE));
//        int amount = cursor.getInt(cursor.getColumnIndex(ExpensesDbHelper.AMOUNT_VALUE));
//
//        tvAmount.setText(String.valueOf(amount));
//        tvTitle.setText(title);
        final String title = cursor.getString(cursor.getColumnIndex(ExpensesDbHelper.TITLE_VALUE));
        final double amount = cursor.getDouble(cursor.getColumnIndex(ExpensesDbHelper.AMOUNT_VALUE));
        final String description = cursor.getString(cursor.getColumnIndex(ExpensesDbHelper.DESCRIPTION_VALUE));
        final long dateValue = cursor.getLong(cursor.getColumnIndex(ExpensesDbHelper.DATE_VALUE));
        final int category = cursor.getInt(cursor.getColumnIndex(ExpensesDbHelper.CATEGORY_VALUE));

        final TextView tvCategory  = (TextView)view.findViewById(R.id.tv_category);
        tvCategory.setText(context.getResources().getStringArray(R.array.categories_icons)[category]);
        final TextView tvDescription  = (TextView)view.findViewById(R.id.tv_description);
        tvDescription.setText(description);

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
