package com.example.ex21051;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class Display extends AppCompatActivity {

    private ListView listViewExpenses;
    private ArrayAdapter<Expense> adapter;
    private ArrayList<Expense> expensesList;
    private FirebaseHelper fbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);

        fbHelper = new FirebaseHelper();
        listViewExpenses = findViewById(R.id.listViewExpenses);
        expensesList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expensesList);
        listViewExpenses.setAdapter(adapter);

        ImageView ivMoreOptions = findViewById(R.id.ivMoreOptions);
        ivMoreOptions.setOnClickListener(v -> showNavigationMenu(v));

        fbHelper.getRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                expensesList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Expense e = data.getValue(Expense.class);
                    if (e != null) expensesList.add(e);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        listViewExpenses.setOnItemLongClickListener((parent, view, position, id) -> {
            showEditDeleteDialog(position);
            return true;
        });
    }

    private void showEditDeleteDialog(int position) {
        String[] options = {"Update Expense", "Delete Expense"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Action");
        builder.setItems(options, (dialog, which) -> {
            Expense expense = expensesList.get(position);
            if (which == 0) {
                Intent intent = new Intent(this, Input.class);
                intent.putExtra("EXPENSE_ID", expense.getId());
                startActivity(intent);
            } else if (which == 1) {
                fbHelper.deleteExpense(expense.getId());
                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    private void showNavigationMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenu().add("Home"); popup.getMenu().add("Input");
        popup.getMenu().add("Search"); popup.getMenu().add("Credits");
        popup.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();
            if (title.equals("Home")) startActivity(new Intent(this, MainActivity.class));
            else if (title.equals("Input")) startActivity(new Intent(this, Input.class));
            else if (title.equals("Search")) startActivity(new Intent(this, Search.class));
            else if (title.equals("Credits")) startActivity(new Intent(this, Credits.class));
            return true;
        });
        popup.show();
    }
}