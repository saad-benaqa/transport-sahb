package com.tp.transport;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    private static final String TAG = "NotificationsActivity";
    private RecyclerView recyclerView;
    private NotificationsAdapter notificationsAdapter;
    private List<Notification> notificationsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        recyclerView = findViewById(R.id.recyclerViewNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        notificationsList = new ArrayList<>();
        notificationsAdapter = new NotificationsAdapter(notificationsList);
        recyclerView.setAdapter(notificationsAdapter);
        setupBottomNavigationView();

        loadNotifications();
        ImageView arrowBack = findViewById(R.id.backres);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationsActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Optional: Call this if you want to close the current activity
            }
        });
    }

    private void loadNotifications() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("notifications")
                .orderBy("timestamp")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w(TAG, "Listen failed.", error);
                            return;
                        }

                        notificationsList.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.exists()) {
                                Notification notification = doc.toObject(Notification.class);
                                notificationsList.add(notification);
                            }
                        }
                        notificationsAdapter.notifyDataSetChanged();
                    }
                });
    }
    private void setupBottomNavigationView() {
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_signale) {
                startActivity(new Intent(NotificationsActivity.this, SignalementActivity.class));
                return true;
            } else if (itemId == R.id.nav_home) {
                startActivity(new Intent(NotificationsActivity.this, MainActivity.class));
                return true;
            } else if (itemId == R.id.nav_res) {
                startActivity(new Intent(NotificationsActivity.this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }
}
