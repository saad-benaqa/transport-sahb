package com.tp.transport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SignalerActivity extends AppCompatActivity {

    private static final String TAG = "SignalerActivity";
    private EditText editTextDate, editTextDescription, editTextCity, editTextZipcode, editTextStreet, editTextCustomProblemType;
    private Spinner spinnerProblemType, spinnerGravity;
    private CheckBox acceptTermsCheckbox;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signaler);

        Log.d(TAG, "onCreate: Initializing FirebaseAuth and FirebaseFirestore");
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        Log.d(TAG, "onCreate: Initializing UI components");
        spinnerProblemType = findViewById(R.id.spinner_problem_type);
        editTextDate = findViewById(R.id.edit_text_date);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextCity = findViewById(R.id.edit_text_city);
        editTextZipcode = findViewById(R.id.edit_text_zipcode);
        editTextStreet = findViewById(R.id.edit_text_street);
        acceptTermsCheckbox = findViewById(R.id.accept_terms_checkbox);
        editTextCustomProblemType = findViewById(R.id.edit_text_custom_problem_type);
        Button submitButton = findViewById(R.id.submit_button);
        spinnerGravity = findViewById(R.id.spinner_gravity);

        // Initialize suggested problem types
        Log.d(TAG, "onCreate: Initializing suggested problem types");
        String[] suggestedProblemTypes = {
                "Problème sur piste cyclable",
                "Problème arrêt de bus",
                "Problème sur autoroute",
                "Problème feux rouge",
                "Problème pour personnes à mobilité réduite",
                "Autre" // Add an "Other" option
        };

        // Populate the spinner with suggested problem types
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, suggestedProblemTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProblemType.setAdapter(adapter);

        // Initialize gravity levels
        Log.d(TAG, "onCreate: Initializing gravity levels");
        String[] gravityLevels = {"Faible", "Moyen", "Élevé"};

        // Populate the spinner with gravity levels
        ArrayAdapter<String> gravityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gravityLevels);
        gravityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGravity.setAdapter(gravityAdapter);

        // Set up BottomNavigationView
        /*Log.d(TAG, "onCreate: Setting up BottomNavigationView");
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        if (nav != null) {
            nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();
                    Intent intent;

                    if (itemId == R.id.nav_signale) {
                        Log.d(TAG, "onNavigationItemSelected: Navigating to SignalementActivity");
                        intent = new Intent(SignalerActivity.this, SignalementActivity.class);
                        startActivity(intent);
                        return true;
                    } else if (itemId == R.id.nav_home) {
                        Log.d(TAG, "onNavigationItemSelected: Navigating to MainActivity");
                        intent = new Intent(SignalerActivity.this, MainActivity.class);
                        startActivity(intent);
                        return true;
                    } else if (itemId == R.id.nav_res) {
                        Log.d(TAG, "onNavigationItemSelected: Navigating to EspResActivity");
                        intent = new Intent(SignalerActivity.this, EspResActivity.class);
                        startActivity(intent);
                        return true;
                    }
                    return false;
                }
            });
        } else {
            Log.d(TAG, "onNavigationItemSeazssqSqsQSlected: Navigating to MainActivity");
        }
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                Intent intent;

                if (itemId == R.id.nav_signale) {
                    Log.d(TAG, "onNavigationItemSelected: Navigating to SignalementActivity");
                    intent = new Intent(SignalerActivity.this, SignalementActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_home) {
                    Log.d(TAG, "onNavigationItemSelected: Navigating to MainActivity");
                    intent = new Intent(SignalerActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_res) {
                    Log.d(TAG, "onNavigationItemSelected: Navigating to EspResActivity");
                    intent = new Intent(SignalerActivity.this, EspResActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });*/

        // Set up back button
        Log.d(TAG, "onCreate: Setting up back button");
        ImageView back = findViewById(R.id.back111);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Back button clicked, navigating to SignalementActivity");
                Intent intent = new Intent(SignalerActivity.this, SignalementActivity.class);
                startActivity(intent);
            }
        });

        // Set up import photo button
        Log.d(TAG, "onCreate: Setting up import photo button");
        Button import_photo_button = findViewById(R.id.import_photo_button);
        import_photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Import photo button clicked, navigating to AddPicActivity");
                Intent intent = new Intent(SignalerActivity.this, AddPicActivity.class);
                startActivity(intent);
            }
        });

        // Handle visibility of custom problem type EditText
        Log.d(TAG, "onCreate: Setting up spinnerProblemType item selection listener");
        spinnerProblemType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = (String) parent.getItemAtPosition(position);
                Log.d(TAG, "onItemSelected: Selected problem type: " + selectedType);
                if ("Autre".equals(selectedType)) {
                    editTextCustomProblemType.setVisibility(View.VISIBLE);
                } else {
                    editTextCustomProblemType.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                editTextCustomProblemType.setVisibility(View.GONE);
            }
        });

        // Set up date picker for date EditText
        Log.d(TAG, "onCreate: Setting up date picker for date EditText");
        calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Date EditText clicked, showing DatePickerDialog");
                new DatePickerDialog(SignalerActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Set up submit button
        Log.d(TAG, "onCreate: Setting up submit button");
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Submit button clicked, calling submitReport");
                submitReport();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; // In which you need to display the date
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editTextDate.setText(sdf.format(calendar.getTime()));
        Log.d(TAG, "updateLabel: Date set to " + sdf.format(calendar.getTime()));
    }

    // Method to handle report submission
    private void submitReport() {
        Log.d(TAG, "submitReport: Preparing to submit report");
        String problemType;
        if ("Autre".equals(spinnerProblemType.getSelectedItem().toString())) {
            problemType = editTextCustomProblemType.getText().toString().trim();
        } else {
            problemType = spinnerProblemType.getSelectedItem().toString().trim();
        }
        String date = editTextDate.getText().toString().trim();
        String gravity = spinnerGravity.getSelectedItem().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (problemType.isEmpty() || date.isEmpty() || gravity.isEmpty() || description.isEmpty()) {
            Log.w(TAG, "submitReport: Missing required fields");
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!acceptTermsCheckbox.isChecked()) {
            Log.w(TAG, "submitReport: Terms and conditions not accepted");
            Toast.makeText(this, "Veuillez accepter les termes et conditions", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mAuth == null || db == null) {
            Log.e(TAG, "submitReport: Firebase services are not initialized");
            Toast.makeText(this, "Firebase services are not initialized.", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            long timestamp = System.currentTimeMillis();

            Map<String, Object> signalement = new HashMap<>();
            signalement.put("userId", userId);
            signalement.put("problemType", problemType);
            signalement.put("date", date);
            signalement.put("gravity", gravity);
            signalement.put("description", description);
            signalement.put("timestamp", timestamp);

            Log.d(TAG, "submitReport: Adding signalement to Firestore");
            db.collection("signalements")
                    .add(signalement)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "submitReport: DocumentSnapshot written with ID: " + documentReference.getId());
                        Toast.makeText(SignalerActivity.this, "Signalement ajouté", Toast.LENGTH_SHORT).show();
                        finish(); // Close the activity after adding the signalement
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "submitReport: Error adding document", e);
                        Toast.makeText(SignalerActivity.this, "Erreur lors de l'ajout du signalement", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Log.w(TAG, "submitReport: User not authenticated");
            Toast.makeText(this, "User not authenticated.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onImportPhotoButtonClick(View view) {
        Log.d(TAG, "onImportPhotoButtonClick: Import photo button clicked");
        // Logic for importing photo if needed
    }
}