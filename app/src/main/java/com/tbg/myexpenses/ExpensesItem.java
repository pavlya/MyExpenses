package com.tbg.myexpenses;

/**
 * Created by Pavlya on 12/11/2015.
 */
public class ExpensesItem {
    private double amount;
    private long date;
    private String explanation;
    private int type;

    public ExpensesItem(){}

    public ExpensesItem(double amount, long date, String explanation) {
        this.amount = amount;
        this.date = date;
        this.explanation = explanation;
    }

    public ExpensesItem(float amount, long date, String explanation, int type) {
        this.amount = amount;
        this.date = date;
        this.explanation = explanation;
        this.type = type;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ExpensesItem{" +
                "amount=" + amount +
                ", date=" + date +
                ", explanation='" + explanation + '\'' +
                ", type=" + type +
                '}';
    }
}
