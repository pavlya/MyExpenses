package com.tbg.myexpenses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tbg.myexpenses.data.ExpensesItem;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Pavlya on 15/11/2015.
 */
public class ExpensesArrayAdapter extends ArrayAdapter<ExpensesItem> {

    public ExpensesArrayAdapter(Context context, List<ExpensesItem> items){
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ExpensesItem item = getItem(position);
        // check if an existing view is being reused, otherwise inflate the view
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.expenses_event, parent, false);
        }
        // Lookup view for data population
        TextView tvAmount = (TextView)convertView.findViewById(R.id.tv_amount);
        TextView tvDescription = (TextView)convertView.findViewById(R.id.tv_description);
        TextView tvDate  = (TextView)convertView.findViewById(R.id.tv_date);
        tvAmount.setText(String.valueOf(item.getAmount()));
        tvDescription.setText("" + item.getExplanation());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(item.getDate());
        tvDate.setText("" + calendar.getTime());

        return convertView;
    }
}
