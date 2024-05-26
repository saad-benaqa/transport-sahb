package com.tp.transport;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResponsableActivity1 extends AppCompatActivity {

    private EditText editTextNom, editTextPrenom, editTextEmail, editTextPassword;
    private TextView textViewID, textViewActivationInfo, textViewPrompt;
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responsable1);

        editTextNom = findViewById(R.id.editTextNom);
        editTextPrenom = findViewById(R.id.editTextPrenom);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        textViewID = findViewById(R.id.textViewID);
        textViewActivationInfo = findViewById(R.id.textViewActivationInfo);
        textViewPrompt = findViewById(R.id.textViewPrompt);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        textViewActivationInfo.setText("Votre compte sera activé dans 12 heures.");

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = editTextNom.getText().toString();
                String prenom = editTextPrenom.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(ResponsableActivity1.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                } else {
                    String idResponsable = nom.toUpperCase() + "0" + prenom + "33";
                    textViewID.setText("ID responsable : " + idResponsable);

                    sendEmail(email, idResponsable);
                }
            }
        });
    }

    private void sendEmail(String email, String idResponsable) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:")); // This ensures only email apps respond
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"hamza.ouba.2004@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Nouvelles informations de responsable à vérifier");
        emailIntent.putExtra(Intent.EXTRA_TEXT, " - Nom: " + editTextNom.getText().toString() +
                "\n - Prénom: " + editTextPrenom.getText().toString() +
                "\n - Email: " + email +
                "\n - ID Responsable: " + idResponsable);

        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);
        } else {
            Toast.makeText(this, "Aucun client email installé.", Toast.LENGTH_SHORT).show();
        }
    }
}
