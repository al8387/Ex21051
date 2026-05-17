package com.example.ex21051;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {
    private DatabaseReference databaseReference;

    public FirebaseHelper() {
        databaseReference = FirebaseDatabase.getInstance().getReference("expenses");
    }

    public DatabaseReference getRef() {
        return databaseReference;
    }

    public void addExpense(String desc, double amount, String category, String date, boolean recurring) {
        String id = databaseReference.push().getKey();
        Expense expense = new Expense(id, desc, amount, category, date, recurring);
        if (id != null) {
            databaseReference.child(id).setValue(expense);
        }
    }

    public void updateExpense(String id, String desc, double amount, String category, String date, boolean recurring) {
        Expense expense = new Expense(id, desc, amount, category, date, recurring);
        databaseReference.child(id).setValue(expense);
    }

    public void deleteExpense(String id) {
        databaseReference.child(id).removeValue();
    }
}