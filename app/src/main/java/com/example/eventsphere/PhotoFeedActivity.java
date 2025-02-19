package com.example.eventsphere;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.toolbox.Volley;

public class PhotoFeedActivity extends AppCompatActivity {

    // Constant to identify the image picker request
    private static final int PICK_IMAGE_REQUEST = 1;

    // ImageView to display the selected image
    private ImageView imageView;

    // Uri( Resource Identifier) to store the path of the selected image
    private Uri imageUri;
    private RecyclerView photoRecyclerView;
    private PhotoAdapter photoAdapter;
    private List<Photo> photoList;
    private Button uploadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_feed);  // Corrected layout file

        // Initialize RecyclerView
        photoRecyclerView = findViewById(R.id.photoRecyclerView);
        photoRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize ImageView and Upload Button
        imageView = findViewById(R.id.imageView);
        uploadButton = findViewById(R.id.uploadButton);

        // Set click listener for the upload button
        uploadButton.setOnClickListener(v -> openImageChooser());

        // Create a list of image URLs (for testing)
        photoList = new ArrayList<>();
        photoList.add(new Photo("https://via.placeholder.com/600/92c641"));
        photoList.add(new Photo("https://via.placeholder.com/600/771796"));
        photoList.add(new Photo("https://via.placeholder.com/600/24f355"));

        // Set up RecyclerView adapter
        photoAdapter = new PhotoAdapter(photoList);
        photoRecyclerView.setAdapter(photoAdapter);
    }

    // Method to open the image chooser
    private void openImageChooser() {
        Intent intent = new Intent();
        // Set the type of content to be selected (images)
        intent.setType("image/*");
        // Set the action to get content
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Start the image picker activity
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    // Method called when an activity you launched exits
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the request code matches, the result is OK, and data is not null
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the Uri of the selected image
            imageUri = data.getData();
            try {
                // Convert the Uri to a Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri); //bitMap rows of colored pixels arranged in a rectangular array-
                // Set the Bitmap to the ImageView
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        // Check if an image has been selected
        if (imageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL of the server endpoint where the image will be uploaded
        String url = "https://example.com";

        // Create a new VolleyMultipartRequest for uploading the image
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                // Success listener
                response -> {
                    try {
                        JSONObject obj = new JSONObject(new String(response.data));
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                // Error listener
                error -> Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show()) {

            // Override to add custom parameters to the request
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", "YOUR_USER_ID");
                return params;
            }

            // Add binary data (the image file) to the request
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imageName = System.currentTimeMillis();
                params.put("image", new DataPart(imageName + ".png", getFileDataFromUri(imageUri)));
                return params;
            }
        };


        // Add the request to Volley's request queue
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    // Helper method to convert Uri to byte array
    private byte[] getFileDataFromUri(Uri uri) {
        byte[] fileData = null;
        try {
            // Open an input stream from the Uri
            InputStream inputStream = getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int len;
            // Read the file data in chunks and write to the byte buffer
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            // Convert the byte buffer to a byte array
            fileData = byteBuffer.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileData;
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
