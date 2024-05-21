package com.tp.transport;

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

import java.util.ArrayList;
import java.util.List;

public class TraficInfoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TraficInfoAdapter adapter;
    private List<TraficInfo> traficInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trafic_info);



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

        // Initialisation des données d'informations sur le trafic (à remplacer par vos données réelles)
        traficInfoList = new ArrayList<>();
        traficInfoList.add(new TraficInfo("Ville A", "01/04/2024", "Description 1", "Description détaillée 1"));
        traficInfoList.add(new TraficInfo("Ville B", "02/04/2024", "Description 2", "Description détaillée 2"));
        traficInfoList.add(new TraficInfo("Ville B", "02/04/2024", "Description 2", "Description détaillée 2"));
        traficInfoList.add(new TraficInfo("Ville B", "02/04/2024", "Description 2", "Description détaillée 2"));
        traficInfoList.add(new TraficInfo("Ville B", "02/04/2024", "Description 2", "Description détaillée 2"));
        traficInfoList.add(new TraficInfo("Ville B", "02/04/2024", "Description 2", "Description détaillée 2"));
        traficInfoList.add(new TraficInfo("Ville B", "02/04/2024", "Description 2", "Description détaillée 2"));
        traficInfoList.add(new TraficInfo("Ville B", "02/04/2024", "Description 2", "Description détaillée 2"));



        // Configuration du RecyclerView et de l'adaptateur
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TraficInfoAdapter(traficInfoList);
        recyclerView.setAdapter(adapter);


        ImageView back = findViewById(R.id.back1111);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TraficInfoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}

