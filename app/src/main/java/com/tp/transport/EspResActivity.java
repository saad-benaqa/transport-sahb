package com.tp.transport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class EspResActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esp_res);

        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.nav_signale){
                    Intent intent = new Intent(EspResActivity.this, SignalementActivity.class);
                    startActivity(intent);
                    return true;
                }else if(item.getItemId()==R.id.nav_home) {
                    Intent intent = new Intent(EspResActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                }else if(item.getItemId()==R.id.nav_res) {
                    Intent intent = new Intent(EspResActivity.this, EspResActivity.class);
                    startActivity(intent);
                    return true;
                }


                return false;
            }
        });


        ImageView back = findViewById(R.id.back11);

        loginButton = findViewById(R.id.loginButton);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EspResActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });



        username = findViewById(R.id.username);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EspResActivity.this, ResponsableActivity.class);
                startActivity(intent);
            }
        });
    }
}