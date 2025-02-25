package com.example.eventsphere;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddEventActivity extends AppCompatActivity {

    // Declare UI elements
    private EditText typeEditText, dateEditText, priceEditText, timeEditText;
    private Button addEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // Initialize UI elements by finding their IDs in the layout
        typeEditText = findViewById(R.id.typeEditText);
        dateEditText = findViewById(R.id.dateEditText);
        priceEditText = findViewById(R.id.priceEditText);
        timeEditText = findViewById(R.id.timeEditText);
        addEventButton = findViewById(R.id.addEventButton);

        // Set click listener for the add event button
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEventToDatabase();
            }
        });
    }

    private void addEventToDatabase() {
        // Get input values and trim whitespace
        String type = typeEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();
        String priceStr = priceEditText.getText().toString().trim();
        String time = timeEditText.getText().toString().trim();

        // Check if any field is empty
        if (type.isEmpty() || date.isEmpty() || priceStr.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            // Try to parse the price as a double
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            // If the price is not a valid number, show an error message
            Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get instance of Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Get reference to "events" node in the database
        DatabaseReference eventsRef = database.getReference("events");

        // Generate a unique key for the new event
        String eventId = eventsRef.push().getKey();
        // Create a new Event object
        Event event = new Event(type, date, price, time);

        // Add the event to the database
        eventsRef.child(eventId).setValue(event)
                .addOnSuccessListener(aVoid -> {
                    // If successful, show success message
                    Toast.makeText(AddEventActivity.this, "Event added successfully", Toast.LENGTH_SHORT).show();
                    // Create intent to return to MainActivity
                    Intent intent = new Intent(AddEventActivity.this, MainActivity.class);
                    // Clear activity stack and reuse existing MainActivity if it's running
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    // Start MainActivity
                    startActivity(intent);
                    // Close current activity
                    finish();
                })
                .addOnFailureListener(e -> {
                    // If failed, show error message
                    Toast.makeText(AddEventActivity.this, "Failed to add event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
