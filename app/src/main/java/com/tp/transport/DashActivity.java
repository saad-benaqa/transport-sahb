package com.tp.transport;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView textdenom;
    private TextView textdemail;
    private ImageView imageView6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);

        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        textdenom = findViewById(R.id.textdenom);
        textdemail = findViewById(R.id.textdeemail);
        imageView6 = findViewById(R.id.imageView6);
        setupBottomNavigationView();

        ImageView arrowBack = findViewById(R.id.imageView8);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Optional: Call this if you want to close the current activity
            }
        });

        // Set the outline provider for the ImageView
        imageView6.setOutlineProvider(ViewOutlineProvider.BACKGROUND);

        // Load user information from Firestore
        loadUserInfo();

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

    private void loadUserInfo() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("utilisateurs").document(currentUser.getUid()).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                String prenom = document.getString("prenom");
                                String nom = document.getString("nom");
                                String email = document.getString("email");
                                String profileImageUrl = document.getString("profile_image_url");

                                if (prenom != null && nom != null) {
                                    textdenom.setText(prenom + " " + nom);
                                }
                                if (email != null) {
                                    textdemail.setText(email);
                                }

                                // Load profile image if URL is available
                                if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                                    Glide.with(DashActivity.this).load(profileImageUrl).into(imageView6);
                                }
                            } else {
                                Toast.makeText(DashActivity.this, "No user data found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(DashActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void navigateToPage(String page) {
        Intent intent;
        switch (page) {
            case "Profile":
                intent = new Intent(this, ProfileActivity.class);
                break;
            case "Signalements":
                intent = new Intent(this, SignalementActivity.class);
                break;
            case "Trafic":
                intent = new Intent(this, TraficInfoActivity.class);
                break;
            case "Responsable":
                intent = new Intent(this, EspResActivity.class);
                break;
            case "Logout":
                intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return;
            default:
                throw new IllegalArgumentException("Unknown page: " + page);
        }
        startActivity(intent);
    }
    private void setupBottomNavigationView() {
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_signale) {
                startActivity(new Intent(DashActivity.this, SignalementActivity.class));
                return true;
            } else if (itemId == R.id.nav_home) {
                startActivity(new Intent(DashActivity.this, MainActivity.class));
                return true;
            } else if (itemId == R.id.nav_res) {

                return true;
            }
            return false;
        });
    }
}
