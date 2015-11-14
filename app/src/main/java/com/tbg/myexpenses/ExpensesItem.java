package com.tbg.myexpenses;

/**
 * Created by Pavlya on 12/11/2015.
 */
public class ExpensesItem {
    private float amount;
    private long date;
    private String explanation;
    private int type;

    public ExpensesItem(float amount, long date, String explanation, int type) {
        this.amount = amount;
        this.date = date;
        this.explanation = explanation;
        this.type = type;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
