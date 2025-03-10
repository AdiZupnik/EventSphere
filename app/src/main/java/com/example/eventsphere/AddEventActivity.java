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
    private EditText typeEditText, dateEditText, priceEditText, timeEditText, sellerEmailEditText, sellerPhoneEditText;
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
        sellerEmailEditText = findViewById(R.id.sellerEmailEditText);
        sellerPhoneEditText = findViewById(R.id.sellerPhoneEditText);
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
        String sellerEmail = sellerEmailEditText.getText().toString().trim();
        String sellerPhone = sellerPhoneEditText.getText().toString().trim();

        // Check if any field is empty
        if (type.isEmpty() || date.isEmpty() || priceStr.isEmpty() || time.isEmpty() || sellerEmail.isEmpty() || sellerPhone.isEmpty()) {
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

        // Example latitude and longitude (replace with actual values if needed)
        double latitude = 37.7749;  // Example: San Francisco latitude
        double longitude = -122.4194; // Example: San Francisco longitude

        // Get instance of Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference eventsRef = database.getReference("events");

        // Generate a unique key for the new event
        String eventId = eventsRef.push().getKey();

        // Create a new Event object with all required parameters
        Event event = new Event(type, date, price, time, sellerEmail, sellerPhone, latitude, longitude);

        // Add the event to the database
        eventsRef.child(eventId).setValue(event)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddEventActivity.this, "Event added successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddEventActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddEventActivity.this, "Failed to add event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}
