package com.tp.transport;

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

import java.util.ArrayList;
import java.util.List;

public class SignalementActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SignalementAdapter adapter;
    private List<GererSignalement> signalementList;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signalements);

        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.nav_signale){
                    Intent intent = new Intent(SignalementActivity.this, SignalementActivity.class);
                    startActivity(intent);
                    return true;
                }else if(item.getItemId()==R.id.nav_home) {
                    Intent intent = new Intent(SignalementActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                }else if(item.getItemId()==R.id.nav_res) {
                    Intent intent = new Intent(SignalementActivity.this, EspResActivity.class);
                    startActivity(intent);
                    return true;
                }


                return false;
            }
        });

        ImageView back = findViewById(R.id.back11);

        Button buttonSignaler = findViewById(R.id.buttonSignaler);

        signalementList = new ArrayList<>();
        signalementList.add(new GererSignalement("Ville A", "01/04/2024", "Description 1", "Description détaillée 1"));
        signalementList.add(new GererSignalement("Ville B", "02/04/2024", "Description 2", "Description détaillée 2"));
        // Ajoutez d'autres signalements selon vos besoins

        // Configuration du RecyclerView
        recyclerView = findViewById(R.id.recyclerViewSignalements);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SignalementAdapter(signalementList);
        recyclerView.setAdapter(adapter);

        // Configuration du bouton "Signaler"

        buttonSignaler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action à effectuer lors du clic sur le bouton "Signaler"
                Toast.makeText(SignalementActivity.this, "Bouton Signaler cliqué", Toast.LENGTH_SHORT).show();
                // Ajoutez votre logique pour gérer le signalement ici
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignalementActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        buttonSignaler.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignalementActivity.this, SignalerActivity.class);
                //Toast.makeText(SignalementActivity.this, "Bouton Signaler cliqué", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });






    }
}
