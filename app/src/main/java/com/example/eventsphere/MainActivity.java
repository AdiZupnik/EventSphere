package com.example.eventsphere;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MainActivity extends AppCompatActivity {

    private EditText searchEditText;
    private Button searchButton;
    private LinearLayout resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // OSMDroid configuration initialization
        Configuration.getInstance().load(this, getPreferences(MODE_PRIVATE));

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        resultTextView = findViewById(R.id.resultTextView);

        searchButton.setOnClickListener(v -> handleSearch());

        // Example events with coordinates
        addEventToDatabase("Rock Concert", "July 15, 2025", 50.00, "8 PM",
                "rock@example.com", "+15551234567", 37.7749, -122.4194);
        addEventToDatabase("Jazz Festival", "August 20, 2025", 75.00, "6 PM",
                "jazz@example.com", "+15552345678", 40.7128, -74.0060);
    }

    private void addEventToDatabase(String type, String date, double price, String time,
                                    String sellerEmail, String sellerPhone, double lat, double lng) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference eventsRef = database.getReference("events");

        Event event = new Event(type, date, price, time, sellerEmail, sellerPhone, lat, lng);
        eventsRef.push().setValue(event)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Event added", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void handleSearch() {
        String query = searchEditText.getText().toString().trim();
        if (!query.isEmpty()) {
            searchEvents(query);
        } else {
            Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show();
        }
    }

    private void searchEvents(String query) {
        Query searchQuery = FirebaseDatabase.getInstance().getReference("events")
                .orderByChild("searchField")
                .startAt(query.toLowerCase())
                .endAt(query.toLowerCase() + "\uf8ff");

        searchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                resultTextView.removeAllViews();

                if (!dataSnapshot.exists()) {
                    showNoResults(query);
                    return;
                }

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Event event = snapshot.getValue(Event.class);
                    if (event != null) {
                        createEventCard(event);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showError(error.getMessage());
            }
        });
    }

    private void createEventCard(Event event) {
        LinearLayout cardLayout = new LinearLayout(this);
        cardLayout.setOrientation(LinearLayout.VERTICAL);
        cardLayout.setPadding(0, 0, 0, 32);

        // Event Details
        TextView details = new TextView(this);
        details.setText(String.format("%s\nDate: %s\nTime: %s\nPrice: $%.2f",
                event.getType(), event.getDate(), event.getTime(), event.getPrice()));
        cardLayout.addView(details);

        // Button Container
        LinearLayout buttonContainer = new LinearLayout(this);
        buttonContainer.setOrientation(LinearLayout.HORIZONTAL);

        Button contactBtn = createButton("Contact Seller", v -> showContactOptions(event));
        Button navigateBtn = createButton("Navigate", v -> showNavigationOptions(event));

        buttonContainer.addView(contactBtn);
        buttonContainer.addView(navigateBtn);
        cardLayout.addView(buttonContainer);

        resultTextView.addView(cardLayout);
    }

    private Button createButton(String text, View.OnClickListener listener) {
        Button btn = new Button(this);
        btn.setText(text);
        btn.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));
        btn.setOnClickListener(listener);
        return btn;
    }

    private void showNavigationOptions(Event event) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Navigation Method")
                .setItems(new String[]{"Open in Maps App", "Show Embedded Map"}, (dialog, which) -> {
                    if (which == 0) {
                        openMapsApp(event);
                    } else {
                        showEmbeddedMap(event);
                    }
                })
                .show();
    }

    private void openMapsApp(Event event) {
        Uri gmmIntentUri = Uri.parse("geo:" + event.getLatitude() + "," + event.getLongitude());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("net.osmand");

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "No maps app installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void showEmbeddedMap(Event event) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(R.layout.osm_map_dialog)
                .setPositiveButton("Close", null)
                .show();

        MapView mapView = dialog.findViewById(R.id.mapView);
        if (mapView == null) {
            Toast.makeText(this, "Error loading map", Toast.LENGTH_SHORT).show();
            return;
        }

        mapView.setTileSource(TileSourceFactory.MAPNIK);
        IMapController controller = mapView.getController();
        GeoPoint point = new GeoPoint(event.getLatitude(), event.getLongitude());
        controller.setCenter(point);
        controller.setZoom(15);

        Marker marker = new Marker(mapView);
        marker.setPosition(point);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(marker);
    }

    private void showContactOptions(Event event) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Contact Seller");
        String[] options = {"Email", "WhatsApp"};
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0: // Email
                    sendEmail(event.getSellerEmail());
                    break;
                case 1: // WhatsApp
                    openWhatsApp(event.getSellerPhone());
                    break;
            }
        });
        builder.show();
    }

    private void sendEmail(String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + email));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Inquiry about event ticket");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show();
        }
    }

    private void openWhatsApp(String phone) {
        String url = "https://api.whatsapp.com/send?phone=" + phone;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "WhatsApp not installed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menu_add_event) {
            Intent intent = new Intent(this, AddEventActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Add Event page", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.menu_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Profile page", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.menu_photo) {
            Intent intent = new Intent(this, PhotoFeedActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Photo Feed", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.menu_search) {
            Toast.makeText(this, "Search event", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.menu_user_managment) {
            Intent intent = new Intent(this, UserManagementActivity.class);
            startActivity(intent);
            Toast.makeText(this, "User managment", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showNoResults(String query) {
        TextView noResults = new TextView(this);
        noResults.setText("No events found for '" + query + "'");
        noResults.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        resultTextView.addView(noResults);
    }

    private void showError(String message) {
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_LONG).show();
    }
}
