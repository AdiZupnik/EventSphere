package com.example.eventsphere;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PhotoFeedActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_feed);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find and set the images
        ImageView image1 = findViewById(R.id.image1);
        ImageView image2 = findViewById(R.id.image2);
        ImageView image3 = findViewById(R.id.image3);

        // Set images from drawable resources
        image1.setImageResource(R.drawable.image1);
        image2.setImageResource(R.drawable.image2);
        image3.setImageResource(R.drawable.image3);
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