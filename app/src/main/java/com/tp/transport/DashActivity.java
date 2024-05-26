package com.tp.transport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DashActivity extends AppCompatActivity {

    private static final String TAG = "DashActivity";
    private TextView textdenom, textdemail;
    private ImageView imageView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        textdenom = findViewById(R.id.textdenom);
        textdemail = findViewById(R.id.textdeemail);
        imageView = findViewById(R.id.imageView6);
        imageView.setOutlineProvider(ViewOutlineProvider.BACKGROUND);

        // Retrieve the Intent that started this activity
        Intent intent = getIntent();
        String imageUriString = intent.getStringExtra("image_uri_key");
        String text = intent.getStringExtra("text_key");
        //String email = intent.getStringExtra("email_key");

        // Set the text in the TextViews from Intent if available
        /*if (text != null) {
            textdenom.setText(text);
        }*/

        /*if (email != null) {
            textdemail.setText(email);
        }*/

        // Load the profile image
        if (imageUriString != null) {
            loadProfileImage(imageUriString);
        }

        // Retrieve and display user info from Firestore
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            db.collection("utilisateurs").document(currentUser.getUid()).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                String prenom = document.getString("prenom");
                                String nom = document.getString("nom");
                                String email = document.getString("email");

                                if (prenom != null && nom != null) {
                                    textdenom.setText(prenom + " " + nom);
                                }
                                if (email != null) {
                                    textdemail.setText(email);
                                }
                            } else {
                                Toast.makeText(DashActivity.this, "No user data found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(DashActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                String page = null;
                if (item.getItemId() == R.id.nav_signale) {
                    page = "Signalements";
                } else if (item.getItemId() == R.id.nav_home) {
                    page = "Home";
                } else if (item.getItemId() == R.id.nav_res) {
                    page = "Responsable";
                }

                if (page != null) {
                    navigateToPage(page);
                    return true;
                }
                return false;
            }
        });

        // Set onClickListeners for the layouts
        findViewById(R.id.informationsLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToPage("Profile");
            }
        });

        findViewById(R.id.signalementsLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToPage("Signalements");
            }
        });

        findViewById(R.id.traficsLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToPage("Trafic");
            }
        });

        findViewById(R.id.EspaceResponsable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToPage("Responsable");
            }
        });

        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                navigateToPage("Logout");
            }
        });
    }

    // Method to load the profile image from a URI string
    private void loadProfileImage(String imageUriString) {
        Uri imageUri = Uri.parse(imageUriString);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to navigate to different activities using the service
    private void navigateToPage(String page) {
        Intent intent = new Intent(this, NavigationService.class);
        intent.setAction(NavigationService.ACTION_NAVIGATE);
        intent.putExtra(NavigationService.EXTRA_PAGE, page);
        startService(intent);
    }
}
