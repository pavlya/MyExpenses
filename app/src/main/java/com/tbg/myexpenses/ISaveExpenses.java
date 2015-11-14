package com.tbg.myexpenses;

import java.util.List;

/**
 * Created by Pavlya on 13/11/2015.
 */
public interface ISaveExpenses {

    public void saveExpenses(ExpensesItem item);

    public void saveExpenses(List<ExpensesItem> items);
}
