package com.tp.transport;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RecapActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recap);

        TextView textViewProblemType = findViewById(R.id.problem_type);
        TextView textViewContactEmail = findViewById(R.id.contact_email);
        TextView textViewGravity = findViewById(R.id.gravity);
        TextView textViewDescription = findViewById(R.id.description);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String problemType = intent.getStringExtra("problemType");
        String contactEmail = intent.getStringExtra("contactEmail");
        String gravity = intent.getStringExtra("gravity");
        String description = intent.getStringExtra("description");

        // Set the received data to the TextViews
        textViewProblemType.setText("Type de problème : " + problemType);
        textViewContactEmail.setText("Email de contact : " + contactEmail);
        textViewGravity.setText("Gravité : " + gravity);
        textViewDescription.setText("Description : " + description);
    }
}
