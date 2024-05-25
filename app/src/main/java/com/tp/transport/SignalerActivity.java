package com.tp.transport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignalerActivity extends AppCompatActivity {

    private static final String TAG = "SignalerActivity";
    private EditText editTextProblemType, editTextContactEmail, editTextGravity, editTextDescription;
    private CheckBox acceptTermsCheckbox;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signaler);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        editTextProblemType = findViewById(R.id.edit_text_problem_type1);
        editTextContactEmail = findViewById(R.id.edit_text_contact_email);
        editTextGravity = findViewById(R.id.edit_text_gravity);
        editTextDescription = findViewById(R.id.edit_text_description);
        acceptTermsCheckbox = findViewById(R.id.accept_terms_checkbox);
        Button submitButton = findViewById(R.id.submit_button);

        // Set up BottomNavigationView
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                Intent intent;

                if (itemId == R.id.nav_signale) {
                    intent = new Intent(SignalerActivity.this, SignalementActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_home) {
                    intent = new Intent(SignalerActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_res) {
                    intent = new Intent(SignalerActivity.this, EspResActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        // Set up back button
        ImageView back = findViewById(R.id.back111);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignalerActivity.this, SignalementActivity.class);
                startActivity(intent);
            }
        });

        // Set up import photo button
        Button import_photo_button = findViewById(R.id.import_photo_button);
        import_photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignalerActivity.this, AddPicActivity.class);
                startActivity(intent);
            }
        });

        // Set up submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReport();
            }
        });
    }

    // Method to handle report submission
    private void submitReport() {
        String problemType = editTextProblemType.getText().toString().trim();
        String contactEmail = editTextContactEmail.getText().toString().trim();
        String gravity = editTextGravity.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (problemType.isEmpty() || contactEmail.isEmpty() || gravity.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!acceptTermsCheckbox.isChecked()) {
            Toast.makeText(this, "Veuillez accepter les termes et conditions", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            long timestamp = System.currentTimeMillis();

            Map<String, Object> signalement = new HashMap<>();
            signalement.put("userId", userId);
            signalement.put("problemType", problemType);
            signalement.put("contactEmail", contactEmail);
            signalement.put("gravity", gravity);
            signalement.put("description", description);
            signalement.put("timestamp", timestamp);

            db.collection("signalements")
                    .add(signalement)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(SignalerActivity.this, "Signalement ajouté", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        finish(); // Close the activity after adding the signalement
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(SignalerActivity.this, "Erreur lors de l'ajout du signalement", Toast.LENGTH_SHORT).show();
                    });
        }
        Intent intent = new Intent(SignalerActivity.this, SignalementActivity.class);
        startActivity(intent);
        finish();
    }

    public void onImportPhotoButtonClick(View view) {
        // Logic for importing photo if needed
    }
}
