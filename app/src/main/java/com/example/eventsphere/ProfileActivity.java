package com.example.eventsphere;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get buttons
        Button editButton = findViewById(R.id.editButton);
        Button settingsButton = findViewById(R.id.settingsButton);
        Button logoutButton = findViewById(R.id.logoutButton);

        // Set button clicks
        editButton.setOnClickListener(view -> {
            Toast.makeText(this, "Edit Profile Clicked", Toast.LENGTH_SHORT).show();
        });

        settingsButton.setOnClickListener(view -> {
            Toast.makeText(this, "Settings Clicked", Toast.LENGTH_SHORT).show();
        });

        logoutButton.setOnClickListener(view -> {
            Toast.makeText(this, "Logout Clicked", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_profile) {
            Intent intent1 = new Intent(this, ProfileActivity.class);
            startActivity(intent1);
            // Toast message when the page opens
            Toast.makeText(this, "Edit Profile Page", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.menu_photo) {
            Intent intent2 = new Intent(this, PhotoFeedActivity.class);
            startActivity(intent2);
            // Toast message when the page opens
            Toast.makeText(this, "Photo feed page", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.menu_search) {
            Intent intent3 = new Intent(this, MainActivity.class);
            startActivity(intent3);
            // Toast message when the page opens
            Toast.makeText(this, "Search page", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (itemId == R.id.menu_add_event) {
            Intent intent = new Intent(this, AddEventActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Add Event page", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

}