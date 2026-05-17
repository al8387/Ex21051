package com.example.ex21051;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Helper class for interacting with Firebase Realtime Database.
 * Provides methods for adding, updating, and deleting expenses.
 *
 * @author Adam
 * @version 1.0
 * @since 2026
 */
public class FirebaseHelper {
    private DatabaseReference databaseReference;

    /**
     * Initializes the Firebase database reference for "expenses".
     */
    public FirebaseHelper() {
        databaseReference = FirebaseDatabase.getInstance().getReference("expenses");
    }

    /**
     * Gets the database reference for expenses.
     * @return DatabaseReference object.
     */
    public DatabaseReference getRef() {
        return databaseReference;
    }

    /**
     * Adds a new expense to the database.
     * @param desc Description of the expense.
     * @param amount Amount spent.
     * @param category Expense category.
     * @param date Date of expense.
     * @param recurring Boolean indicating if it's recurring.
     */
    public void addExpense(String desc, double amount, String category, String date, boolean recurring) {
        String id = databaseReference.push().getKey();
        Expense expense = new Expense(id, desc, amount, category, date, recurring);
        if (id != null) {
            databaseReference.child(id).setValue(expense);
        }
    }

    /**
     * Updates an existing expense in the database.
     * @param id ID of the expense to update.
     * @param desc Description of the expense.
     * @param amount Amount spent.
     * @param category Expense category.
     * @param date Date of expense.
     * @param recurring Boolean indicating if it's recurring.
     */
    public void updateExpense(String id, String desc, double amount, String category, String date, boolean recurring) {
        Expense expense = new Expense(id, desc, amount, category, date, recurring);
        databaseReference.child(id).setValue(expense);
    }

    /**
     * Deletes an expense from the database by ID.
     * @param id ID of the expense to delete.
     */
    public void deleteExpense(String id) {
        databaseReference.child(id).removeValue();
    }
}