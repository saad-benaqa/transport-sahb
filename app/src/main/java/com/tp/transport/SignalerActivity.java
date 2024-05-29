package com.tp.transport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SignalerActivity extends AppCompatActivity {

    private static final String TAG = "SignalerActivity";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private EditText editTextDate, editTextDescription, editTextCity, editTextZipcode, editTextStreet, editTextCustomProblemType;
    private Spinner spinnerProblemType, spinnerGravity;
    private Button useMyLocationButton;
    private FusedLocationProviderClient fusedLocationClient;
    private boolean locationPermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signaler);

        // Initialize UI components
        spinnerProblemType = findViewById(R.id.spinner_problem_type);
        editTextDate = findViewById(R.id.edit_text_date);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextCity = findViewById(R.id.edit_text_city);
        editTextZipcode = findViewById(R.id.edit_text_zipcode);
        editTextStreet = findViewById(R.id.edit_text_street);
        editTextCustomProblemType = findViewById(R.id.edit_text_custom_problem_type);
        Button submitButton = findViewById(R.id.submit_button);
        spinnerGravity = findViewById(R.id.spinner_gravity);
        useMyLocationButton = findViewById(R.id.use_my_location_button);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        useMyLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationPermission();
                if (locationPermissionGranted) {
                    getDeviceLocation();
                }
            }
        });
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
