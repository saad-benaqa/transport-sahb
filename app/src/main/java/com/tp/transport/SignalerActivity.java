package com.tp.transport;

import static com.tp.transport.AddPicActivity.REQUEST_IMAGE_CAPTURE;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.osmdroid.util.GeoPoint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class SignalerActivity extends AppCompatActivity {

    private static final String TAG = "SignalerActivity";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private final List<Uri> photoUris = new ArrayList<>();
    private EditText editTextDate, editTextDescription, editTextCity, editTextZipcode, editTextStreet, editTextCustomProblemType;
    private Spinner spinnerProblemType, spinnerGravity;
    private FusedLocationProviderClient fusedLocationClient;
    private boolean locationPermissionGranted;
    private CheckBox acceptTermsCheckbox;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Calendar calendar;
    private RecyclerView recyclerViewPhotos;
    private PhotosAdapter photosAdapter;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private ActivityResultLauncher<Intent> takePhotoLauncher;

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
        RecyclerView recyclerViewPhotos = findViewById(R.id.recyclerViewPhotos);
        recyclerViewPhotos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        PhotosAdapter photosAdapter = new PhotosAdapter(photoUris);
        recyclerViewPhotos.setAdapter(photosAdapter);

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

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            if (data.getClipData() != null) {
                                int count = data.getClipData().getItemCount();
                                for (int i = 0; i < count; i++) {
                                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                                    photoUris.add(imageUri);
                                }
                            } else if (data.getData() != null) {
                                Uri imageUri = data.getData();
                                photoUris.add(imageUri);
                            }
                            photosAdapter.notifyDataSetChanged();
                        }
                    }
                }
        );

        takePhotoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Bundle extras = data.getExtras();
                            Bitmap imageBitmap = (Bitmap) extras.get("data");
                            Uri imageUri = getImageUri(imageBitmap);
                            photoUris.add(imageUri);
                            photosAdapter.notifyDataSetChanged();
                        }
                    }
                }
        );

        useMyLocationButton.setOnClickListener(v -> {
            getLocationPermission();
        });
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

            // Upload photos and get their URLs
            uploadPhotosAndSubmit(signalement);
        } else {
            Log.d(TAG, "submitReport: User not authenticated");
            Toast.makeText(this, "Utilisateur non authentifié.", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadPhotosAndSubmit(Map<String, Object> signalement) {
        if (photoUris.isEmpty()) {
            submitToFirestore(signalement);
            return;
        }

        List<String> photoUrls = new ArrayList<>();
        for (Uri photoUri : photoUris) {
            StorageReference photoRef = storageReference.child("images/" + UUID.randomUUID().toString());
            photoRef.putFile(photoUri)
                    .addOnSuccessListener(taskSnapshot -> photoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        photoUrls.add(uri.toString());
                        if (photoUrls.size() == photoUris.size()) {
                            signalement.put("photoUrls", photoUrls);
                            submitToFirestore(signalement);
                            sendNotificationToAllUsers("Un nouveau problème a été signalé", "Consulter votre trafic info", photoUrls);
                        }
                    }))
                    .addOnFailureListener(e -> {
                        Toast.makeText(SignalerActivity.this, "Erreur lors du téléchargement des photos", Toast.LENGTH_SHORT).show();
                    });
        }
    }


    private void submitToFirestore(Map<String, Object> signalement) {
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
    }

    public void onImportPhotoButtonClick(View view) {
        CharSequence[] options = {"Take Photo", "Choose from Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                dispatchTakePictureIntent();
            } else if (which == 1) {
                dispatchPickImageIntent();
            }
        });
        builder.show();
    }

    private void dispatchPickImageIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        pickImageLauncher.launch(intent);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            takePhotoLauncher.launch(takePictureIntent);
        }
    }

    private Uri getImageUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
        } else {
            dispatchTakePictureIntent();
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            getDeviceLocation();
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
                getDeviceLocation();
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
    private void sendNotificationToAllUsers(String title, String body, List<String> photoUrls) {
        new FetchBitmapTask(title, body, photoUrls).execute();
        saveNotificationToDatabase(title, body, photoUrls);
    }
    private class FetchBitmapTask extends AsyncTask<Void, Void, Bitmap> {
        private String title;
        private String body;
        private List<String> photoUrls;

        FetchBitmapTask(String title, String body, List<String> photoUrls) {
            this.title = title;
            this.body = body;
            this.photoUrls = photoUrls;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            if (photoUrls != null && !photoUrls.isEmpty()) {
                String photoUrl = photoUrls.get(0);
                return getBitmapFromURL(photoUrl);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            buildAndSendNotification(title, body, bitmap);
        }
    }
    private void buildAndSendNotification(String title, String body, @Nullable Bitmap bitmap) {
        Intent intent = new Intent(this, SignalerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        String channelId = "SignalementChannel";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_warning_notification)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)  // Set high priority for heads-up notification
                .setDefaults(NotificationCompat.DEFAULT_ALL);  // Use default settings for sound, vibration, etc.

        if (bitmap != null) {
            notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap)
                    .bigLargeIcon((Bitmap) null));
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Signalement Notifications", NotificationManager.IMPORTANCE_HIGH);  // Set channel importance to high
            channel.setDescription("Notifications for new signalements");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }

    private Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void saveNotificationToDatabase(String title, String body, List<String> photoUrls) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> notification = new HashMap<>();
        notification.put("title", title);
        notification.put("body", body);
        notification.put("photoUrls", photoUrls);
        notification.put("timestamp", System.currentTimeMillis());

        db.collection("notifications")
                .add(notification)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Notification saved to database");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error saving notification to database", e);
                });
    }


}