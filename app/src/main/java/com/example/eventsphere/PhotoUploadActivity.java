package com.example.eventsphere;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class PhotoUploadActivity extends AppCompatActivity {

    // Constants
    private static final int PICK_IMAGE_REQUEST = 1;

    // UI Components
    private ImageView imageView;
    private Button chooseImageButton, uploadImageButton;

    // Uri to store the selected image path
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_upload);

        // Initialize UI components
        imageView = findViewById(R.id.imageView);
        chooseImageButton = findViewById(R.id.chooseImageButton);
        uploadImageButton = findViewById(R.id.uploadImageButton);

        // Set click listeners for buttons
        chooseImageButton.setOnClickListener(v -> openFileChooser());
        uploadImageButton.setOnClickListener(v -> uploadImage());
    }

    // Method to open the file chooser
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");  // Set type to image
        intent.setAction(Intent.ACTION_GET_CONTENT);  // Set action to get content
        startActivityForResult(intent, PICK_IMAGE_REQUEST);  // Start the activity for result
    }

    // Handle the result of the file chooser
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();  // Get the Uri of the selected image
            imageView.setImageURI(imageUri);  // Set the selected image to the ImageView
        }
    }

    // Method to upload the selected image
    private void uploadImage() {
        if (imageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get instance of Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference("images");

        // Generate a unique name for the image
        final String imageName = UUID.randomUUID().toString();
        StorageReference imageRef = storageRef.child(imageName + ".jpg");

        // Upload the file to Firebase Storage
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // If upload is successful, get the download URL
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        saveImageUrlToDatabase(imageUrl);  // Save the URL to the database
                    });
                })
                .addOnFailureListener(e -> {
                    // If upload fails, show an error message
                    Toast.makeText(PhotoUploadActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Method to save the image URL to the Firebase Realtime Database
    private void saveImageUrlToDatabase(String imageUrl) {
        // Get instance of Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference imagesRef = database.getReference("images");

        // Generate a unique key for the new image entry
        String imageId = imagesRef.push().getKey();

        // Save the image URL under the generated key
        imagesRef.child(imageId).setValue(imageUrl)
                .addOnSuccessListener(aVoid -> {
                    // If save is successful, show a success message and close the activity
                    Toast.makeText(PhotoUploadActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        // If save fails, show an error message
                        Toast.makeText(PhotoUploadActivity.this, "Failed to save image URL: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
