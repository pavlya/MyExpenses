package com.tbg.myexpenses.data;

/**
 * Created by Pavlya on 12/11/2015.
 */
public class ExpensesItem {

    private int _id;
    private double amount;
    private long date;
    private String title;
    private String explanation;
    private int category;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public ExpensesItem(){}

    public ExpensesItem(double amount, long date, String explanation) {
        this.amount = amount;
        this.date = date;
        this.explanation = explanation;
    }

    public ExpensesItem(float amount, long date, String explanation, int category) {
        this.amount = amount;
        this.date = date;
        this.explanation = explanation;
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public int getCategory() {
        return category;
    }

    public void setcategory(int category) {
        this.category = category;
    }

}
