package com.example.eventsphere;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
}