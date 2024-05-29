package com.tp.transport;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SignalementActivity extends AppCompatActivity {

    private static final int REQUEST_CALENDAR_PERMISSIONS = 100;
    private FirebaseFirestore db;
    private List<GererSignalement> signalementList;
    private SignalementAdapter adapter;
    private FirebaseAuth mAuth;
    private ImageView retourButton;
    private Button signalerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signalements);

        // Request calendar permissions
        requestCalendarPermissions();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        signalementList = new ArrayList<>();
        adapter = new SignalementAdapter(signalementList, this::addNoteToCalendar);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewSignalements);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Initialize buttons
        signalerButton = findViewById(R.id.buttonSignaler);
        retourButton = findViewById(R.id.back11);

        loadSignalements();

        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                int itemId = item.getItemId();
                if (itemId == R.id.nav_signale) {
                    // Current activity, no need to navigate
                    return true;
                } else if (itemId == R.id.nav_home) {
                    intent = new Intent(SignalementActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_res) {
                    intent = new Intent(SignalementActivity.this, EspResActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        signalerButton.setOnClickListener(v -> {
            Intent intent = new Intent(SignalementActivity.this, SignalerActivity.class);
            startActivity(intent);
        });

        retourButton.setOnClickListener(v -> finish());
    }

    private void requestCalendarPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_CALENDAR,
                    Manifest.permission.WRITE_CALENDAR
            }, REQUEST_CALENDAR_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALENDAR_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted
            } else {
                Toast.makeText(this, "Calendar permissions are required to add events.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isCalendarAvailable() {
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(
                    CalendarContract.Calendars.CONTENT_URI,
                    new String[]{CalendarContract.Calendars._ID, CalendarContract.Calendars.CALENDAR_DISPLAY_NAME},
                    null,
                    null,
                    null
            );
            return (cursor != null && cursor.getCount() > 0);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void addNoteToCalendar(GererSignalement signalement) {
        if (isCalendarAvailable()) {
            Intent intent = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.Events.TITLE, "Signalement: " + signalement.getProblemType())
                    .putExtra(CalendarContract.Events.DESCRIPTION, signalement.getDescription())
                    .putExtra(CalendarContract.Events.EVENT_LOCATION, signalement.getLocation())
                    .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, "No calendar app found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No calendar synchronized with the device yet. Please set up a calendar account.", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadSignalements() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            db.collection("signalements")
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (DocumentChange document : task.getResult().getDocumentChanges()) {
                                GererSignalement signalement = document.getDocument().toObject(GererSignalement.class);
                                signalementList.add(signalement);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(SignalementActivity.this, "Erreur lors du chargement des signalements", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(SignalementActivity.this, "Utilisateur non authentifié", Toast.LENGTH_SHORT).show();
        }
    }
}
