package com.example.ex21051;

public class Expense {
    private String id;
    private String description;
    private double amount;
    private String category;
    private String date;
    private boolean recurring;

    public Expense() {}

    public Expense(String id, String description, double amount, String category, String date, boolean recurring) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.recurring = recurring;
    }

    public String getId() { return id; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public String getDate() { return date; }
    public boolean isRecurring() { return recurring; }

    @Override
    public String toString() {
        return date + " | " + description + " - $" + String.format("%.2f", amount) + "\n(" + category + ")";
    }
}