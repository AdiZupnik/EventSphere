package com.example.eventsphere;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddEventActivity extends AppCompatActivity {

    private EditText typeEditText, dateEditText, priceEditText, timeEditText;
    private Button addEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        typeEditText = findViewById(R.id.typeEditText);
        dateEditText = findViewById(R.id.dateEditText);
        priceEditText = findViewById(R.id.priceEditText);
        timeEditText = findViewById(R.id.timeEditText);
        addEventButton = findViewById(R.id.addEventButton);

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEventToDatabase();
            }
        });
    }

    private void addEventToDatabase() {
        String type = typeEditText.getText().toString().trim();//trim() removes spaces at the beginning and end of the string for easier match
        String date = dateEditText.getText().toString().trim();
        String priceStr = priceEditText.getText().toString().trim();
        String time = timeEditText.getText().toString().trim();

        if (type.isEmpty() || date.isEmpty() || priceStr.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {//try to parse the price as a double
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {//if the price is not a number
            Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference eventsRef = database.getReference("events");

        String eventId = eventsRef.push().getKey();
        Event event = new Event(type, date, price, time);

        eventsRef.child(eventId).setValue(event)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddEventActivity.this, "Event added successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity after adding the event
                })
                .addOnFailureListener(e -> Toast.makeText(AddEventActivity.this, "Failed to add event: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
