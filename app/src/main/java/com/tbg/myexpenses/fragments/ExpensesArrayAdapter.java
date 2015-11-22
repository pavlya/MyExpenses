package com.tbg.myexpenses.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tbg.myexpenses.MyFontManager;
import com.tbg.myexpenses.R;
import com.tbg.myexpenses.data.ExpensesItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Pavlya on 15/11/2015.
 */
public class ExpensesArrayAdapter extends ArrayAdapter<ExpensesItem> {
    SimpleDateFormat simpleDateFormat;
    private String[] category;
    String dateFormat;
    public ExpensesArrayAdapter(Context context, List<ExpensesItem> items){
        super(context, 0, items);
        category = context.getResources().getStringArray(R.array.categories);
        dateFormat = "yyyy-MM-dd HH:mm";
        simpleDateFormat = new SimpleDateFormat(dateFormat);
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
        TextView tvTitle = (TextView)convertView.findViewById(R.id.tv_title);
        TextView tvDate  = (TextView)convertView.findViewById(R.id.tv_date);
        TextView tvCategory  = (TextView)convertView.findViewById(R.id.tv_category);
        TextView tvDescription  = (TextView)convertView.findViewById(R.id.tv_description);
        // setting icon for an item
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fontawesome-webfont.ttf") ;
//        tvCategory.setTypeface(MyFontManager.getTypeFace(getContext(), MyFontManager.FONTAWESOME));
        tvCategory.setText(R.string.fa_icon_shopping_cart);
        tvCategory.setTypeface(font);

        tvAmount.setText(String.valueOf(item.getAmount()));

        tvTitle.setText(item.getExplanation());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(item.getDate());
        Date date = calendar.getTime();
        tvDate.setText(String.valueOf(simpleDateFormat.format(date)));

        return convertView;
    }
}
