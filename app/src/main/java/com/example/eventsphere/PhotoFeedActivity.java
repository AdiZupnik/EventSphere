package com.example.eventsphere;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class PhotoFeedActivity extends AppCompatActivity {

    private RecyclerView photoRecyclerView;
    private PhotoAdapter photoAdapter;
    private List<Photo> photoList;
    private FloatingActionButton addPhotoFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_feed);

        // Set up Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Photo Feed");
        }

        photoRecyclerView = findViewById(R.id.photoRecyclerView);
        addPhotoFab = findViewById(R.id.addPhotoFab);

        photoList = new ArrayList<>();
        photoAdapter = new PhotoAdapter(photoList);
        photoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        photoRecyclerView.setAdapter(photoAdapter);

        addPhotoFab.setOnClickListener(v -> {
            Intent intent = new Intent(PhotoFeedActivity.this, PhotoUploadActivity.class);
            startActivity(intent);
        });

        loadPhotos();
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
            Toast.makeText(this, "Edit Profile Page", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.menu_photo) {
            Intent intent2 = new Intent(this, PhotoFeedActivity.class);
            startActivity(intent2);
            Toast.makeText(this, "Photo feed page", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.menu_search) {
            Intent intent3 = new Intent(this, MainActivity.class);
            startActivity(intent3);
            Toast.makeText(this, "Search page", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (itemId == R.id.menu_user_managment) {
            Intent intent4 = new Intent(this, UserManagementActivity.class);
            startActivity(intent4);
            Toast.makeText(this, "user management page" , Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (itemId == R.id.menu_add_event) {
            Intent intent = new Intent(this, AddEventActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Add Event page", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item); // Changed this line
    }


    private void loadPhotos() {
        FirebaseDatabase.getInstance().getReference("images")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        photoList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String imageUrl = snapshot.getValue(String.class);
                            photoList.add(0, new Photo(imageUrl));
                        }
                        photoAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(PhotoFeedActivity.this, "Error loading photos: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
