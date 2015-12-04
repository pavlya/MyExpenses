package com.tbg.myexpenses.unused;

import com.tbg.myexpenses.data.ExpensesItem;

import java.util.List;

/**
 * Created by Pavlya on 13/11/2015.
 */
public interface ISaveExpenses {

    public void saveExpenses(ExpensesItem item);

    public void saveExpenses(List<ExpensesItem> items);
}
