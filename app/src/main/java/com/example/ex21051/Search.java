package com.example.ex21051;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Activity for searching and filtering expenses.
 *
 * @author Adam
 * @version 1.0
 * @since 2026
 */
public class Search extends AppCompatActivity {

    private Spinner spinnerFilterCategory, spinnerSortBy;
    private EditText etMinPrice, etMaxPrice;
    private ListView listViewSearchResults;
    private FirebaseHelper fbHelper;
    private ArrayList<Expense> allExpenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        fbHelper = new FirebaseHelper();
        allExpenses = new ArrayList<>();
        spinnerFilterCategory = findViewById(R.id.spinnerFilterCategory);
        spinnerSortBy = findViewById(R.id.spinnerSortBy);
        etMinPrice = findViewById(R.id.etMinPrice);
        etMaxPrice = findViewById(R.id.etMaxPrice);
        listViewSearchResults = findViewById(R.id.listViewSearchResults);
        Button btnApplyFilters = findViewById(R.id.btnApplyFilters);
        ImageView ivMoreOptions = findViewById(R.id.ivMoreOptions);

        ivMoreOptions.setOnClickListener(v -> showNavigationMenu(v));

        String[] categories = {"All", "Food & Dining", "Transportation", "Shopping", "Entertainment", "Bills & Utilities", "Other"};
        spinnerFilterCategory.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories));

        String[] sortOptions = {"Date (Newest First)", "Date (Oldest First)", "Amount (Highest)", "Amount (Lowest)"};
        spinnerSortBy.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sortOptions));

        fbHelper.getRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allExpenses.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Expense e = data.getValue(Expense.class);
                    if (e != null) allExpenses.add(e);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        btnApplyFilters.setOnClickListener(v -> applyFiltersAndSort());
    }

    private void applyFiltersAndSort() {
        String cat = spinnerFilterCategory.getSelectedItem().toString();
        String sort = spinnerSortBy.getSelectedItem().toString();
        double min = etMinPrice.getText().toString().isEmpty() ? 0 : Double.parseDouble(etMinPrice.getText().toString());
        double max = etMaxPrice.getText().toString().isEmpty() ? Double.MAX_VALUE : Double.parseDouble(etMaxPrice.getText().toString());

        ArrayList<Expense> filteredList = new ArrayList<>();
        for (Expense e : allExpenses) {
            boolean matchCat = cat.equals("All") || e.getCategory().equals(cat);
            boolean matchPrice = e.getAmount() >= min && e.getAmount() <= max;
            if (matchCat && matchPrice) filteredList.add(e);
        }

        if (sort.contains("Highest")) Collections.sort(filteredList, (a, b) -> Double.compare(b.getAmount(), a.getAmount()));
        else if (sort.contains("Lowest")) Collections.sort(filteredList, (a, b) -> Double.compare(a.getAmount(), b.getAmount()));
        else if (sort.contains("Newest")) Collections.sort(filteredList, (a, b) -> b.getDate().compareTo(a.getDate()));
        else Collections.sort(filteredList, (a, b) -> a.getDate().compareTo(b.getDate()));

        listViewSearchResults.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filteredList));
        if (filteredList.isEmpty()) Toast.makeText(this, "No results", Toast.LENGTH_SHORT).show();
    }

    private void showNavigationMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenu().add("Home"); popup.getMenu().add("Display");
        popup.getMenu().add("Input"); popup.getMenu().add("Credits");
        popup.setOnMenuItemClickListener(item -> {
            CharSequence titleSeq = item.getTitle();
            if (titleSeq == null) return false;
            String title = titleSeq.toString();

            if (title.equals("Home")) startActivity(new Intent(this, MainActivity.class));
            else if (title.equals("Display")) startActivity(new Intent(this, Display.class));
            else if (title.equals("Input")) startActivity(new Intent(this, Input.class));
            else if (title.equals("Credits")) startActivity(new Intent(this, Credits.class));
            return true;
        });
        popup.show();
    }
}