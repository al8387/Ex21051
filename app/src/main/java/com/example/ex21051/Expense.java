package com.example.ex21051;

/**
 * Model class representing an expense item.
 *
 * @author Adam
 * @version 1.0
 * @since 2026
 */
public class Expense {
    private String id;
    private String description;
    private double amount;
    private String category;
    private String date;
    private boolean recurring;

    /**
     * Default constructor required for Firebase calls.
     */
    public Expense() {}

    /**
     * Constructs a new Expense.
     *
     * @param id          Unique ID of the expense.
     * @param description Description of the expense.
     * @param amount      Amount of the expense.
     * @param category     Category of the expense.
     * @param date        Date when the expense occurred.
     * @param recurring   Whether the expense is recurring.
     */
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