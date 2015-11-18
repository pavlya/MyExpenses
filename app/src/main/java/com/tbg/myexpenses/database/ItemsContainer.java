package com.tbg.myexpenses.database;

import android.content.ClipData;

import com.tbg.myexpenses.ExpensesItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavlya on 16/11/2015.
 */
public class ItemsContainer {
    public static List<ExpensesItem> expensesItems = new ArrayList<>();

    public ItemsContainer()
    {
        expensesItems.add(new ExpensesItem(150.3, System.currentTimeMillis(), "some sht"));
    }

    public static String [] categories = { "Not sorted", "Food", "Bills", "Vacation", "Transportation"};
}
