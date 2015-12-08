package com.tbg.myexpenses.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.tbg.myexpenses.R;

import java.util.Date;

/**
 * Created by Pavlya on 08/12/2015.
 */
public class ExpensesGroupedByDateAdapter extends CursorAdapter {
    LayoutInflater inflater;

    public ExpensesGroupedByDateAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.expense_item_header, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvAmount = (TextView) view.findViewById(R.id.tv_header_amount);
        TextView tvCategory = (TextView) view.findViewById(R.id.tv_header_category);

        // TODO: 08/12/2015 change number values to contstants with text representation
        tvAmount.setText(cursor.getString(3));
        tvCategory.setText(new Date(cursor.getLong(2)).toString());
    }
}
