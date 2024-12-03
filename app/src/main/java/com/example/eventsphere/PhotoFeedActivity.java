package com.example.eventsphere;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PhotoFeedActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_feed);

        // Toast message when the page opens
        Toast.makeText(this, "Photo Feed Page", Toast.LENGTH_SHORT).show();

        // Find and set the images
        ImageView image1 = findViewById(R.id.image1);
        ImageView image2 = findViewById(R.id.image2);
        ImageView image3 = findViewById(R.id.image3);

        // Set images from drawable resources
        image1.setImageResource(R.drawable.image1);
        image2.setImageResource(R.drawable.image2);
        image3.setImageResource(R.drawable.image3);
    }
}