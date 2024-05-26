package com.tp.transport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

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
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private MapView map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.activity_main);
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        GeoPoint startPoint = new GeoPoint(43.65020,7.00517);
        IMapController mapController =map.getController();
        mapController.setZoom(18.0);
        mapController.setCenter(startPoint);
        ArrayList<OverlayItem> items = new ArrayList<>();
        OverlayItem home = new OverlayItem("accident ","route", new GeoPoint(43.65020,7.00517));
        Drawable m = home.getMarker(0);
        items.add(home);
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(getApplicationContext(),
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

        BottomNavigationView nav = findViewById(R.id.bottomNav);


        ImageView buttonSignaler = findViewById(R.id.buttonSignaler);
        ImageView trafic = findViewById(R.id.trafic);
        buttonSignaler.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, SignalerActivity.class);
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
                Intent intent = new Intent(MainActivity.this,ItineraireActivity.class);
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
    public void onPause(){
        super.onPause();
        map.onPause();

    }
    public void onResume(){
        super.onResume();
        map.onResume();

    }
}