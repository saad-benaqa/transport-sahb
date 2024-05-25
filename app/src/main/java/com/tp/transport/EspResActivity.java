package com.tp.transport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EspResActivity extends AppCompatActivity {

    private static final String TAG = "EspresActivity";

    private EditText codeConfidentielEditText;
    private Button validerButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esp_res);

        codeConfidentielEditText = findViewById(R.id.codeConfidentiel);
        validerButton = findViewById(R.id.validerButton);
        db = FirebaseFirestore.getInstance();

        validerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredCode = codeConfidentielEditText.getText().toString().trim();
                if (!enteredCode.isEmpty()) {
                    checkCodeConfidentiel(enteredCode);
                } else {
                    Toast.makeText(EspResActivity.this, "Veuillez entrer un code confidentiel", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkCodeConfidentiel(String codeConfidentiel) {
        CollectionReference responsablesCollection = db.collection("Responsables");
        responsablesCollection.whereEqualTo("codeConfidentiel", codeConfidentiel)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        Intent intent = new Intent(EspResActivity.this, ResponsableActivity.class);
                        intent.putExtra("codeConfidentiel", codeConfidentiel);
                        startActivity(intent);
                    } else {
                        Toast.makeText(EspResActivity.this, "Code confidentiel invalide", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Erreur lors de la recherche du code confidentiel : ", e);
                    Toast.makeText(EspResActivity.this, "Erreur lors de la vérification du code confidentiel", Toast.LENGTH_SHORT).show();
                });
    }
}
