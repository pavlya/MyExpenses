package com.tbg.myexpenses.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavlya on 16/11/2015.
 */
public class ItemsContainer {
    public static List<ExpensesItem> expensesItems = new ArrayList<>();

    public ItemsContainer()
    {
//        expensesItems.add(new ExpensesItem(150.3, System.currentTimeMillis(), "some sht"));
    }

    public static String [] categories = {"Food", "Bills", "Vacation", "Transportation", "Not sorted"};
}
