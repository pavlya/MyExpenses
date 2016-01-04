package com.tbg.myexpenses.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.tbg.myexpenses.MyExpensesApplication;
import com.tbg.myexpenses.data.ExpensesDbHelper;

/**
 * Created by Pavlya on 04/01/2016.
 */
public class GroupedByDateLoader extends CursorLoader {
    private String groupedBy;

    public GroupedByDateLoader(Context context, String groupedBy) {
        super(context);
        this.groupedBy = groupedBy;
    }

    @Override
    protected Cursor onLoadInBackground() {
        if (groupedBy.equals(MyExpensesApplication.GROUPED_BY_DAY)) {
            return ExpensesDbHelper.getInstance(getContext()).getGroupedByDay();
        } else if (groupedBy.equals(MyExpensesApplication.GROUPED_BY_WEEK)) {
            return ExpensesDbHelper.getInstance(getContext()).getGroupedByWeek();
        } else if (groupedBy.equals(MyExpensesApplication.GROUPED_BY_MONTH)) {
            return ExpensesDbHelper.getInstance(getContext()).getGroupedByMonth();
        }
        return null;
    }
}
