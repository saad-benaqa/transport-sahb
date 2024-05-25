package com.tp.transport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class infoResEditActivity extends AppCompatActivity {

    private static final String TAG = "InfoResEditActivity";

    private EditText nomEditText;
    private EditText prenomEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText codeConfidentialEditText;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_res_edit);


        // Après la définition des EditText dans onCreate()

        Button modifierButton = findViewById(R.id.modifier);
        modifierButton.setOnClickListener(v -> {
            String nom = nomEditText.getText().toString().trim();
            String prenom = prenomEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Mettre à jour les valeurs dans Firestore
            String codeConfidentiel = getIntent().getStringExtra("codeConfidentiel");
            updateResponsableInfo(codeConfidentiel, nom, prenom, email, password);

            recreate();
        });


        db = FirebaseFirestore.getInstance();

        // Initialisez les EditText
        nomEditText = findViewById(R.id.nom1);
        prenomEditText = findViewById(R.id.prenom);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        codeConfidentialEditText = findViewById(R.id.codeConfidential);

        // Récupérez le code confidentiel depuis l'intent
        String codeConfidentiel = getIntent().getStringExtra("codeConfidentiel");
        Log.d(TAG, "Code confidentiel reçu : " + codeConfidentiel);

        // Recherchez et définissez les informations personnelles
        fetchResponsableInfo(codeConfidentiel);
    }

    private void fetchResponsableInfo(String codeConfidentiel) {
        CollectionReference responsablesCollection = db.collection("Responsables");
        responsablesCollection.whereEqualTo("codeConfidentiel", codeConfidentiel)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            try {
                                String nom = documentSnapshot.getString("nom");
                                String prenom = documentSnapshot.getString("prenom");
                                String email = documentSnapshot.getString("email");
                                String password = documentSnapshot.getString("password");

                                nomEditText.setText(nom);
                                prenomEditText.setText(prenom);
                                emailEditText.setText(email);
                                passwordEditText.setText(password);
                                codeConfidentialEditText.setText(codeConfidentiel);

                                Log.d(TAG, "Informations récupérées et définies.");
                            } catch (Exception e) {
                                Log.e(TAG, "Erreur lors de la définition des valeurs dans les EditText : ", e);
                                Toast.makeText(infoResEditActivity.this, "Erreur lors de l'affichage des informations", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(infoResEditActivity.this, "Aucun responsable trouvé avec ce code confidentiel", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Erreur lors de la recherche de responsable : ", e);
                    Toast.makeText(infoResEditActivity.this, "Erreur lors de la recherche de responsable", Toast.LENGTH_SHORT).show();
                });




        // Configuration du BottomNavigationView
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_signale) {
                Intent intent = new Intent(infoResEditActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_home) {
                Intent intent = new Intent(infoResEditActivity.this, DashActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_res) {
                Intent intent = new Intent(infoResEditActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    private void updateResponsableInfo(String codeConfidentiel, String nom, String prenom, String email, String password) {
        CollectionReference responsablesCollection = db.collection("Responsables");
        responsablesCollection.whereEqualTo("codeConfidentiel", codeConfidentiel)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            // Mettre à jour les valeurs du document
                            Map<String, Object> updates = new HashMap<>();
                            if (!TextUtils.isEmpty(nom)) {
                                updates.put("nom", nom);
                            }
                            if (!TextUtils.isEmpty(prenom)) {
                                updates.put("prenom", prenom);
                            }
                            if (!TextUtils.isEmpty(email)) {
                                updates.put("email", email);
                            }
                            if (!TextUtils.isEmpty(password)) {
                                updates.put("password", password);
                            }

                            documentSnapshot.getReference().update(updates)
                                    .addOnCompleteListener(updateTask -> {
                                        if (updateTask.isSuccessful()) {
                                            Toast.makeText(this, "Informations mises à jour avec succès", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Log.e(TAG, "Erreur lors de la mise à jour des informations : ", updateTask.getException());
                                            Toast.makeText(this, "Erreur lors de la mise à jour des informations", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Log.e(TAG, "Erreur lors de la recherche du document : ", task.getException());
                        Toast.makeText(this, "Erreur lors de la recherche du document", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}




