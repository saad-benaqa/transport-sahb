package com.tp.transport;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class SignLog extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_log);

        // Récupération des références des layouts utilisateur et responsable
        LinearLayout utilisateurLayout = findViewById(R.id.utilisateur);
        LinearLayout responsableLayout = findViewById(R.id.Responsable);

        // Définition des écouteurs de clic pour chaque layout
        utilisateurLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirection vers LoginActivity
                Intent intent = new Intent(SignLog.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        responsableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirection vers l'activité ResponsableActivity
                Intent intent = new Intent(SignLog.this, EspResActivity.class);
                startActivity(intent);
            }
        });
    }
}