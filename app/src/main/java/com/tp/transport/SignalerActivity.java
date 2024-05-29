package com.tp.transport;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SignalerActivity extends AppCompatActivity {

    private static final String TAG = "SignalerActivity";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private EditText editTextDate, editTextDescription, editTextCity, editTextZipcode, editTextStreet, editTextCustomProblemType;
    private Spinner spinnerProblemType, spinnerGravity;
    private FusedLocationProviderClient fusedLocationClient;
    private boolean locationPermissionGranted;
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

        // Initialize UI components
        spinnerProblemType = findViewById(R.id.spinner_problem_type);
        editTextDate = findViewById(R.id.edit_text_date);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextCity = findViewById(R.id.edit_text_city);
        editTextZipcode = findViewById(R.id.edit_text_zipcode);
        editTextStreet = findViewById(R.id.edit_text_street);
        editTextCustomProblemType = findViewById(R.id.edit_text_custom_problem_type);
        spinnerGravity = findViewById(R.id.spinner_gravity);
        Button useMyLocationButton = findViewById(R.id.use_my_location_button);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


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
        useMyLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationPermission();
                if (locationPermissionGranted) {
                    getDeviceLocation();
                }
            }
        });


        // Populate the spinner with suggested problem types
        Log.d(TAG, "onCreate: Populating spinner with suggested problem types");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, suggestedProblemTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProblemType.setAdapter(adapter);

        Log.d(TAG, "onCreate: Initializing gravity levels");
        String[] gravityLevels = {"Faible", "Moyen", "Élevé"};

        ArrayAdapter<String> gravityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gravityLevels);
        gravityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGravity.setAdapter(gravityAdapter);

        spinnerProblemType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected: Position - " + position);
                if (position == parent.getCount() - 1) {
                    editTextCustomProblemType.setVisibility(View.VISIBLE);
                } else {
                    editTextCustomProblemType.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "onNothingSelected");
            }
        });

        calendar = Calendar.getInstance();

        // Initialize date picker dialog
        Log.d(TAG, "onCreate: Initializing date picker dialog");
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.d(TAG, "onDateSet: " + dayOfMonth + "-" + monthOfYear + "-" + year);
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SignalerActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Initialize back button
        Log.d(TAG, "onCreate: Initializing back button");
        ImageView backButton = findViewById(R.id.back111);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(SignalerActivity.this, SignalementActivity.class);
            startActivity(intent);
        });

        submitButton.setOnClickListener(v -> {
            Log.d(TAG, "submitButton: Clicked");
            submitReport();
        });

        BottomNavigationView nav = findViewById(R.id.bottomNav);
        /*nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d(TAG, "onNavigationItemSelected: " + item.getItemId());
                Intent intent;
                int itemId = item.getItemId();
                if (itemId == R.id.nav_signale) {
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
        });*/
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editTextDate.setText(sdf.format(calendar.getTime()));
    }

    private void submitReport() {
        Log.d(TAG, "submitReport: Started");
        String problemType;
        if ("Autre".equals(spinnerProblemType.getSelectedItem().toString())) {
            problemType = editTextCustomProblemType.getText().toString().trim();
        } else {
            problemType = spinnerProblemType.getSelectedItem().toString().trim();
        }
        String date = editTextDate.getText().toString().trim();
        String gravity = spinnerGravity.getSelectedItem().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String city = editTextCity.getText().toString().trim();
        String zipcode = editTextZipcode.getText().toString().trim();
        String street = editTextStreet.getText().toString().trim();

        if (problemType.isEmpty() || date.isEmpty() || gravity.isEmpty() || description.isEmpty() || city.isEmpty() || zipcode.isEmpty() || street.isEmpty()) {
            Log.d(TAG, "submitReport: Validation failed - fields are empty");
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!acceptTermsCheckbox.isChecked()) {
            Log.d(TAG, "submitReport: Validation failed - terms not accepted");
            Toast.makeText(this, "Veuillez accepter les termes et conditions", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            long timestamp = System.currentTimeMillis();


            String email = user.getEmail();

            Map<String, Object> signalement = new HashMap<>();
            signalement.put("userId", userId);
            signalement.put("problemType", problemType);
            signalement.put("date", date);
            signalement.put("gravity", gravity);
            signalement.put("description", description);
            signalement.put("city", city);
            signalement.put("zipcode", zipcode);
            signalement.put("street", street);
            signalement.put("timestamp", timestamp);
            signalement.put("contactEmail", email);

            db.collection("signalements")
                    .add(signalement)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "submitReport: Signalement added");
                        Toast.makeText(SignalerActivity.this, "Signalement ajouté", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Log.d(TAG, "submitReport: Error adding signalement", e);
                        Toast.makeText(SignalerActivity.this, "Erreur lors de l'ajout du signalement", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Log.d(TAG, "submitReport: User not authenticated");
            Toast.makeText(this, "Utilisateur non authentifié.", Toast.LENGTH_SHORT).show();
        }
    }


    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        }
    }

    private void getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            Location lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                GeoPoint geoPoint = new GeoPoint(lastKnownLocation.getLatitude(),
                                        lastKnownLocation.getLongitude());
                                fillAddressFields(geoPoint);
                            } else {
                                Toast.makeText(SignalerActivity.this, "Unable to get last location", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignalerActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private void fillAddressFields(GeoPoint geoPoint) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(
                    geoPoint.getLatitude(), geoPoint.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                editTextCity.setText(address.getLocality());
                editTextZipcode.setText(address.getPostalCode());
                editTextStreet.setText(address.getThoroughfare());
            } else {
                Toast.makeText(this, "No address found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Log.e(TAG, "Geocoder exception: " + e.getMessage());
        }
    }
}
