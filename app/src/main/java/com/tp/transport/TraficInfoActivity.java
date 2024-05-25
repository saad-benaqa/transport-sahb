package com.tp.transport;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

public class TraficInfoActivity extends AppCompatActivity {

    private TraficInfoAdapter adapter;
    private List<TraficInfo> traficInfoList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trafic_info);

        db = FirebaseFirestore.getInstance();
        traficInfoList = new ArrayList<>();
        adapter = new TraficInfoAdapter(traficInfoList);

        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.nav_signale){
                    Intent intent = new Intent(TraficInfoActivity.this, SignalementActivity.class);
                    startActivity(intent);
                    return true;
                }else if(item.getItemId()==R.id.nav_home) {
                    Intent intent = new Intent(TraficInfoActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                }else if(item.getItemId()==R.id.nav_res) {
                    Intent intent = new Intent(TraficInfoActivity.this, EspResActivity.class);
                    startActivity(intent);
                    return true;
                }


                return false;
            }
        });

        // Configuration du RecyclerView et de l'adaptateur
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        ImageView back = findViewById(R.id.back1111);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TraficInfoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        loadSignalements();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadSignalements() {
        db.collection("signalements")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentChange document : task.getResult().getDocumentChanges()) {
                            TraficInfo signalement = document.getDocument().toObject(TraficInfo.class);
                            traficInfoList.add(signalement);
                        }
                        adapter.notifyDataSetChanged();
                    }  // Gérer l'erreur lors du chargement des données

                });
    }
}
