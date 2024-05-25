package com.tp.transport;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    private EditText prenomEditText, nomEditText, numteleEditText, emailEditText, passwordEditText, confPasswordEditText;
    private Button signupButton;
    private TextView loginRedirectText;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        prenomEditText = findViewById(R.id.prenom);
        nomEditText = findViewById(R.id.nom);
        numteleEditText = findViewById(R.id.numtele);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confPasswordEditText = findViewById(R.id.confpassword);
        signupButton = findViewById(R.id.signupButton);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Sign up button clicked");
                handleSignup();
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void handleSignup() {
        String prenom = prenomEditText.getText().toString().trim();
        String nom = nomEditText.getText().toString().trim();
        String numTele = numteleEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confPassword = confPasswordEditText.getText().toString().trim();

        Log.d(TAG, "Prenom: " + prenom);
        Log.d(TAG, "Nom: " + nom);
        Log.d(TAG, "NumTel: " + numTele);
        Log.d(TAG, "Email: " + email);
        Log.d(TAG, "Password: " + password);
        Log.d(TAG, "ConfPassword: " + confPassword);

        if (prenom.isEmpty() || nom.isEmpty() || numTele.isEmpty() || email.isEmpty() || password.isEmpty() || confPassword.isEmpty()) {
            Toast.makeText(SignupActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Some fields are empty");
            return;
        }

        if (!password.equals(confPassword)) {
            Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Passwords do not match");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User registration successful");
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            saveUserInfo(user, prenom, nom, numTele, password);
                        }
                    } else {
                        Log.w(TAG, "User registration failed", task.getException());
                        Toast.makeText(SignupActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserInfo(FirebaseUser user, String prenom, String nom, String numTele, String password) {
        String userId = user.getUid();

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("prenom", prenom);
        userMap.put("nom", nom);
        userMap.put("num_telephone", numTele);
        userMap.put("password", password);
        userMap.put("email", user.getEmail());

        db.collection("utilisateurs").document(userId)
                .set(userMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SignupActivity.this, "User info saved", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "User info saved successfully");
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error saving user info", e);
                    Toast.makeText(SignupActivity.this, "Error saving user info", Toast.LENGTH_SHORT).show();
                });
    }
}
