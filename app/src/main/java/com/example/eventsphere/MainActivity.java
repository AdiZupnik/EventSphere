package com.example.eventsphere;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private EditText searchEditText;
    private Button searchButton;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        resultTextView = findViewById(R.id.resultTextView);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = searchEditText.getText().toString();
                searchEvents(searchQuery);
            }
        });
        addEventToDatabase("Rock Concert", "July 15, 2025", 50.00, "8 PM");
        addEventToDatabase("Jazz Festival", "August 20, 2025", 75.00, "6 PM");
        addEventToDatabase("Taylor Swift Concert", "September 5, 2025", 60.00, "7:30 PM");
        addEventToDatabase("StendUp Night", "October 31, 2025", 40.00, "10 PM");
        addEventToDatabase("Country Music Fest", "November 15, 2025", 55.00, "5 PM");

    }
    private void addEventToDatabase(String type, String date, double price, String time) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference eventsRef = database.getReference("events");

        String eventId = eventsRef.push().getKey();
        Event event = new Event(type, date, price, time);

        eventsRef.child(eventId).setValue(event)
                .addOnSuccessListener(aVoid -> Toast.makeText(MainActivity.this, "Event added successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Failed to add event: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void searchEvents(String query) {
        DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("events");
        Query searchQuery = eventsRef.orderByChild("searchField")
                .startAt(query.toLowerCase())
                .endAt(query.toLowerCase() + "\uf8ff");

        searchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StringBuilder result = new StringBuilder();
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    if (event != null) {
                        result.append("Found: ").append(event.type)
                                .append(", ").append(event.date)
                                .append(", ").append(event.time)
                                .append(", $").append(event.price).append("\n");
                    }
                }
                if (result.length() == 0) {
                    result.append("No events found for '").append(query).append("'");
                }
                resultTextView.setText(result.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                resultTextView.setText("Error: " + databaseError.getMessage());
            }
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
        else if (itemId == R.id.menu_user_managment) {
            Intent intent4 = new Intent(this, UserManagementActivity.class);
            startActivity(intent4);
            // Toast message when the page opens
            Toast.makeText(this, "user management page" , Toast.LENGTH_SHORT).show();
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