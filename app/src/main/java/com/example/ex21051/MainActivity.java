package com.example.ex21051;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

/**
 * Main Activity that displays the total monthly expenses and recent activity.
 *
 * @author Adam
 * @version 1.0
 * @since 2026
 */
public class MainActivity extends AppCompatActivity {

    private TextView tvTotalAmount;
    private ListView listViewRecent;
    private FirebaseHelper fbHelper;
    private ArrayList<Expense> recentList;
    private ArrayAdapter<Expense> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fbHelper = new FirebaseHelper();
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        listViewRecent = findViewById(R.id.listViewRecent);
        ImageView ivMoreOptions = findViewById(R.id.ivMoreOptions);
        FloatingActionButton fabAddExpense = findViewById(R.id.fabAddExpense);

        recentList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recentList);
        listViewRecent.setAdapter(adapter);

        ivMoreOptions.setOnClickListener(v -> showNavigationMenu(v));
        fabAddExpense.setOnClickListener(v -> startActivity(new Intent(this, Input.class)));

        fbHelper.getRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double total = 0;
                recentList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Expense expense = data.getValue(Expense.class);
                    if (expense != null) {
                        total += expense.getAmount();
                        recentList.add(0, expense);
                    }
                }
                tvTotalAmount.setText("$" + String.format("%.2f", total));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void showNavigationMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenu().add("Input");
        popup.getMenu().add("Display");
        popup.getMenu().add("Search");
        popup.getMenu().add("Credits");
        popup.setOnMenuItemClickListener(item -> {
            CharSequence titleSeq = item.getTitle();
            if (titleSeq == null) return false;
            String title = titleSeq.toString();
            if (title.equals("Input")) startActivity(new Intent(this, Input.class));
            else if (title.equals("Display")) startActivity(new Intent(this, Display.class));
            else if (title.equals("Search")) startActivity(new Intent(this, Search.class));
            else if (title.equals("Credits")) startActivity(new Intent(this, Credits.class));
            return true;
        });
        popup.show();
    }
}