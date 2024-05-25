package com.tp.transport;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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

public class SignalementActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private List<GererSignalement> signalementList;
    private SignalementAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signalements);

        db = FirebaseFirestore.getInstance();
        signalementList = new ArrayList<>();
        adapter = new SignalementAdapter(signalementList);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewSignalements);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadSignalements();

        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                if (item.getItemId() == R.id.nav_signale) {
                    intent = new Intent(SignalementActivity.this, SignalementActivity.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.nav_home) {
                    intent = new Intent(SignalementActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.nav_res) {
                    intent = new Intent(SignalementActivity.this, EspResActivity.class);
                    startActivity(intent);
                    return true;
                }

                return false;
            }
        });

        ImageView back = findViewById(R.id.back11);
        Button buttonSignaler = findViewById(R.id.buttonSignaler);

        // Configuration du bouton "Signaler"
        buttonSignaler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignalementActivity.this, SignalerActivity.class);
                startActivity(intent);
            }
        });

        // Configuration du bouton de retour
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignalementActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadSignalements() {
        db.collection("signalements")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentChange document : task.getResult().getDocumentChanges()) {
                            GererSignalement signalement = document.getDocument().toObject(GererSignalement.class);
                            signalementList.add(signalement);
                        }
                        adapter.notifyDataSetChanged(); // Mettre à jour l'adaptateur après l'ajout de tous les signalements
                    } else {
                        Toast.makeText(SignalementActivity.this, "Erreur lors du chargement des signalements", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
