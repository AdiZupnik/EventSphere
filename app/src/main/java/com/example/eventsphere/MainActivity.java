package com.example.eventsphere;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private EditText searchEditText;
    private Button searchButton;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        resultTextView = findViewById(R.id.resultTextView);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        // Get the Bottom Navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Connect the Navigation Graph and Bottom Navigation View
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = searchEditText.getText().toString();
                String searchResult = searchEvents(searchQuery);
                resultTextView.setText(searchResult);
            }
        });
    }

    private String searchEvents(String query) {
        // This is a mock implementation. In a real app, you would query a database or API.
        switch (query.toLowerCase()) {
            case "concert":
                return "Found: Rock Concert, July 15, 8 PM, $50";
            case "theater":
                return "Found: Shakespeare Play, August 5, 7 PM, $40";
            case "sports":
                return "Found: Football Match, September 10, 3 PM, $60";
            default:
                return "No events found for '" + query + "'";
        }
    }
}