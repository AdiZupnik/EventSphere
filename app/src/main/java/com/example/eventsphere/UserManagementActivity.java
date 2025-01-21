package com.example.eventsphere;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserManagementActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference userRef;

    private TextView userNameTextView;
    private TextView userEmailTextView;
    private Button editProfileButton;
    private Button deleteAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "No user signed in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        userRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());

        initializeViews();
        setupListeners();
        loadUserData();
    }

    private void initializeViews() {
        userNameTextView = findViewById(R.id.userNameTextView);
        userEmailTextView = findViewById(R.id.userEmailTextView);
        editProfileButton = findViewById(R.id.editProfileButton);
        deleteAccountButton = findViewById(R.id.deleteAccountButton);
    }

    private void setupListeners() {
        editProfileButton.setOnClickListener(v -> editProfile());
        deleteAccountButton.setOnClickListener(v -> deleteUser());
    }

    private void loadUserData() {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userDetails = dataSnapshot.getValue(User.class);
                if (userDetails != null) {
                    updateUI(userDetails);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserManagementActivity.this, "Failed to load user data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(User userDetails) {
        userNameTextView.setText(userDetails.getName());
        userEmailTextView.setText(userDetails.getMail());
    }

    private void editProfile() {
        // Implement edit profile logic here
        Toast.makeText(this, "Edit profile functionality to be implemented", Toast.LENGTH_SHORT).show();
    }

    private void deleteUser() {
        userRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                user.delete().addOnCompleteListener(deleteTask -> {
                    if (deleteTask.isSuccessful()) {
                        Toast.makeText(UserManagementActivity.this, "User deleted successfully", Toast.LENGTH_SHORT).show();
                        auth.signOut();
                        finish();
                    } else {
                        Toast.makeText(UserManagementActivity.this, "Failed to delete user: " + deleteTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(UserManagementActivity.this, "Failed to delete user data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
