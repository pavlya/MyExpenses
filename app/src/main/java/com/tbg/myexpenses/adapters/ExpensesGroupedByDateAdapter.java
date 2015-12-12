package com.tbg.myexpenses.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.tbg.myexpenses.R;
import com.tbg.myexpenses.Utility;

import java.text.SimpleDateFormat;

/**
 * Created by Pavlya on 08/12/2015.
 */
public class ExpensesGroupedByDateAdapter extends CursorAdapter {
    LayoutInflater inflater;
    SimpleDateFormat simpleDateFormat;
    String dateFormat;

    public ExpensesGroupedByDateAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dateFormat = "yyyy-MM-dd";
        simpleDateFormat = new SimpleDateFormat(dateFormat);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.expense_item_header, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvAmount = (TextView) view.findViewById(R.id.tv_header_amount);
        TextView tvCategory = (TextView) view.findViewById(R.id.tv_header_category);
        TextView tvDateNum = (TextView) view.findViewById(R.id.tv_date_num);

        // TODO: 08/12/2015 change number values to contstants with text representation
        tvAmount.setText(cursor.getString(3));
        tvDateNum.setText(cursor.getString(1));
//        tvCategory.setText(Utility.getDateText(2));
        tvCategory.setText(Utility.getDateText(cursor.getLong(2)));
    }
}
