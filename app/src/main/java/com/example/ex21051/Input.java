package com.example.ex21051;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Activity for adding and editing expenses.
 *
 * @author Adam
 * @version 1.0
 * @since 2026
 */
public class Input extends AppCompatActivity {

    private EditText etExpenseDesc, etExpenseAmount, etExpenseDate;
    private Spinner spinnerInputCategory;
    private CheckBox cbRecurring;
    private Button btnSaveExpense;
    private FirebaseHelper fbHelper;
    private String updateId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input);

        fbHelper = new FirebaseHelper();
        etExpenseDesc = findViewById(R.id.etExpenseDesc);
        etExpenseAmount = findViewById(R.id.etExpenseAmount);
        etExpenseDate = findViewById(R.id.etExpenseDate);
        spinnerInputCategory = findViewById(R.id.spinnerInputCategory);
        cbRecurring = findViewById(R.id.cbRecurring);
        btnSaveExpense = findViewById(R.id.btnSaveExpense);
        ImageView ivMoreOptions = findViewById(R.id.ivMoreOptions);

        ivMoreOptions.setOnClickListener(v -> showNavigationMenu(v));

        String[] categories = {"Food & Dining", "Transportation", "Shopping", "Entertainment", "Bills & Utilities", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerInputCategory.setAdapter(adapter);

        updateId = getIntent().getStringExtra("EXPENSE_ID");

        if (updateId != null) {
            btnSaveExpense.setText("Update Expense");
            fbHelper.getRef().child(updateId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Expense e = snapshot.getValue(Expense.class);
                    if (e != null) {
                        etExpenseDesc.setText(e.getDescription());
                        etExpenseAmount.setText(String.valueOf(e.getAmount()));
                        etExpenseDate.setText(e.getDate());
                        cbRecurring.setChecked(e.isRecurring());
                        for (int i = 0; i < categories.length; i++) {
                            if (categories[i].equals(e.getCategory())) {
                                spinnerInputCategory.setSelection(i);
                                break;
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        } else {
            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            etExpenseDate.setText(today);
        }

        btnSaveExpense.setOnClickListener(v -> {
            String desc = etExpenseDesc.getText().toString().trim();
            String amountStr = etExpenseAmount.getText().toString().trim();
            String date = etExpenseDate.getText().toString().trim();
            String category = spinnerInputCategory.getSelectedItem().toString();
            boolean recurring = cbRecurring.isChecked();

            if (desc.isEmpty() || amountStr.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double amount = Double.parseDouble(amountStr);

                if (updateId != null) {
                    fbHelper.updateExpense(updateId, desc, amount, category, date, recurring);
                } else {
                    fbHelper.addExpense(desc, amount, category, date, recurring);
                }
                finish();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showNavigationMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenu().add("Home"); popup.getMenu().add("Display");
        popup.getMenu().add("Search"); popup.getMenu().add("Credits");
        popup.setOnMenuItemClickListener(item -> {
            CharSequence titleSeq = item.getTitle();
            if (titleSeq == null) return false;
            String title = titleSeq.toString();

            if (title.equals("Home")) startActivity(new Intent(this, MainActivity.class));
            else if (title.equals("Display")) startActivity(new Intent(this, Display.class));
            else if (title.equals("Search")) startActivity(new Intent(this, Search.class));
            else if (title.equals("Credits")) startActivity(new Intent(this, Credits.class));
            return true;
        });
        popup.show();
    }
}