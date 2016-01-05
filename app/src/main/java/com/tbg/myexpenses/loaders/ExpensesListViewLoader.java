package com.tbg.myexpenses.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

/**
 * Created by Pavlya on 05/01/2016.
 */
public class ExpensesListViewLoader extends CursorLoader {
    public ExpensesListViewLoader(Context context) {
        super(context);
    }

    @Override
    protected Cursor onLoadInBackground() {
        return super.onLoadInBackground();
    }
}
