package com.tp.transport;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MapView map;
    private RecyclerView horizontalRecyclerView;
    private SignalementAdapter adapter;
    private List<GererSignalement> signalementList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.activity_main);

        // Initialiser la carte
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        GeoPoint startPoint = new GeoPoint(43.65020, 7.00517);
        IMapController mapController = map.getController();
        mapController.setZoom(18.0);
        mapController.setCenter(startPoint);
        ArrayList<OverlayItem> items = new ArrayList<>();
        OverlayItem home = new OverlayItem("accident", "route", new GeoPoint(43.65020, 7.00517));
        Drawable m = home.getMarker(0);
        items.add(home);
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<>(getApplicationContext(),
                items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                return true;
            }

            @Override
            public boolean onItemLongPress(int index, OverlayItem item) {
                return false;
            }
        });
        mOverlay.setFocusItemsOnTap(true);
        map.getOverlays().add(mOverlay);

        // Configurer le RecyclerView horizontal
        horizontalRecyclerView = findViewById(R.id.horizontalRecyclerView);
        horizontalRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Récupérer les signalements de TraficInfo
        signalementList = getSignalementsFromTraficInfo();

        adapter = new SignalementAdapter(signalementList, signalement -> {
            // Gérer le clic sur un élément
        });
        horizontalRecyclerView.setAdapter(adapter);

        // Configurer les autres éléments de l'interface
        BottomNavigationView nav = findViewById(R.id.bottomNav);

        ImageView buttonSignaler = findViewById(R.id.buttonSignaler);
        ImageView trafic = findViewById(R.id.trafic);
        buttonSignaler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotificationsActivity.class);
                startActivity(intent);
            }
        });
        trafic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TraficInfoActivity.class);
                startActivity(intent);
            }
        });
        EditText searchEditText = findViewById(R.id.editTextText3);
        searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ItineraireActivity.class);
                startActivity(intent);
            }
        });

        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_signale) {
                    Intent intent = new Intent(MainActivity.this, SignalementActivity.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.nav_home) {
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.nav_res) {
                    Intent intent = new Intent(MainActivity.this, DashActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    private List<GererSignalement> getSignalementsFromTraficInfo() {
        // Récupérer les signalements de votre source de données TraficInfo
        List<GererSignalement> signalements = new ArrayList<>();
        // Ajouter des exemples de signalements
        signalements.add(new GererSignalement());
        signalements.add(new GererSignalement());
        // ... ajouter plus de signalements si nécessaire
        return signalements;
    }
}
