package com.example.ex21051;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity that displays credit information about the application's creators.
 *
 * @author Adam
 * @version 1.0
 * @since 2026
 */
public class Credits extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credits);

        ImageView ivMoreOptions = findViewById(R.id.ivMoreOptions);
        ivMoreOptions.setOnClickListener(v -> showNavigationMenu(v));
    }

    private void showNavigationMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenu().add("Home");
        popup.getMenu().add("Display");
        popup.getMenu().add("Search");
        popup.setOnMenuItemClickListener(item -> {
            CharSequence titleSeq = item.getTitle();
            if (titleSeq == null) return false;
            String title = titleSeq.toString();

            if (title.equals("Home")) startActivity(new Intent(this, MainActivity.class));
            else if (title.equals("Display")) startActivity(new Intent(this, Display.class));
            else if (title.equals("Search")) startActivity(new Intent(this, Search.class));
            return true;
        });
        popup.show();
    }
}