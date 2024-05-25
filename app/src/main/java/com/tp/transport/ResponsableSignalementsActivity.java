package com.tp.transport;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ResponsableSignalementsActivity extends AppCompatActivity {

    private static final String TAG = "RespSignalementsActivity";
    private TraficInfoAdapter adapter;
    private List<TraficInfo> traficInfoList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responsable_signalements);
        Log.d(TAG, "onCreate: Started");

        db = FirebaseFirestore.getInstance();
        traficInfoList = new ArrayList<>();
        adapter = new TraficInfoAdapter(traficInfoList);

        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_signale) {
                    startActivity(new Intent(ResponsableSignalementsActivity.this, SignalementActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.nav_home) {
                    startActivity(new Intent(ResponsableSignalementsActivity.this, MainActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.nav_res) {
                    startActivity(new Intent(ResponsableSignalementsActivity.this, EspResActivity.class));
                    return true;
                }
                return false;
            }
        });

        // Configuration du RecyclerView et de l'adaptateur
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        ImageView back = findViewById(R.id.back2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResponsableSignalementsActivity.this, MainActivity.class));
            }
        });

        loadSignalements();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadSignalements() {
        Log.d(TAG, "loadSignalements: Loading signalements");
        db.collection("signalements")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentChange document : task.getResult().getDocumentChanges()) {
                            TraficInfo signalement = document.getDocument().toObject(TraficInfo.class);
                            traficInfoList.add(signalement);
                        }
                        adapter.notifyDataSetChanged();
                        Log.d(TAG, "loadSignalements: Signalements loaded successfully");
                    } else {
                        Log.e(TAG, "loadSignalements: Failed to load signalements", task.getException());
                    }
                });
    }
}
