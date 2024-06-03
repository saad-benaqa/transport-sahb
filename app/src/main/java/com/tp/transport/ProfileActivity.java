package com.tp.transport;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

public class ProfileActivity extends Activity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private EditText FirstName;
    private EditText LastName;
    private EditText numTele;
    private EditText email;
    private EditText password;
    private EditText passwordConfirmation;
    private ImageView imageView;
    private Button pickImageButton;
    private Button modifier;
    private Uri selectedImageUri;

    @Override
    protected void onResume() {
        super.onResume();
        loadUserInfo(); // Recharger les informations utilisateur à chaque reprise de l'activité
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initializeViews();
        setupBottomNavigationView();
        setupPickImageButton();
        setupBackButton();
        setupModifierButton();
        loadUserInfo();

        ImageView arrowBack = findViewById(R.id.back1);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, DashActivity.class);
                startActivity(intent);
                finish(); // Optional: Call this if you want to close the current activity
            }
        });
    }

    private void initializeViews() {
        FirstName = findViewById(R.id.prenom);
        LastName = findViewById(R.id.nom);
        numTele = findViewById(R.id.numtele);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        passwordConfirmation = findViewById(R.id.passwordConfirmation);
        imageView = findViewById(R.id.imageView);
        pickImageButton = findViewById(R.id.btnPick);
        modifier = findViewById(R.id.modifier);
    }

    private void setupBottomNavigationView() {
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_signale) {
                startActivity(new Intent(ProfileActivity.this, SignalementActivity.class));
                return true;
            } else if (itemId == R.id.nav_home) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                return true;
            } else if (itemId == R.id.nav_res) {
                startActivity(new Intent(ProfileActivity.this, DashActivity.class));
                return true;
            }
            return false;
        });
    }

    private void setupPickImageButton() {
        pickImageButton.setOnClickListener(v -> openGallery());
    }

    private void setupBackButton() {
        ImageView back = findViewById(R.id.back1);
        back.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, MainActivity.class)));
    }

    private void setupModifierButton() {
        modifier.setOnClickListener(v -> {
            // Mettre à jour le mot de passe
            updatePassword();

            // Mettre à jour l'image de profil si une nouvelle image a été choisie
            if (selectedImageUri != null) {
                uploadImageToFirebase(selectedImageUri);
            } else {
                startActivity(new Intent(ProfileActivity.this, DashActivity.class));
            }

            // Mettre à jour les autres informations utilisateur
            updateUserInfo();
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Storage reference
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profile_images/" + userId + ".jpg");

            // Upload image to Firebase Storage
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Get the download URL
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Save the URL to Firestore
                            saveImageUrlToFirestore(userId, uri.toString());
                            // Mettre à jour l'ImageView avec la nouvelle image
                            Glide.with(ProfileActivity.this).load(uri).into(imageView);

                            // Passer l'URL de l'image à DashActivity
                            Intent dashIntent = new Intent(ProfileActivity.this, DashActivity.class);
                            dashIntent.putExtra("image_url_key", uri.toString());
                            startActivity(dashIntent);
                        });
                    })
                    .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show());
        }
    }

    private void saveImageUrlToFirestore(String userId, String imageUrl) {
        // Sauvegarde de l'URL de l'image dans Firestore
        db.collection("utilisateurs").document(userId)
                .update("profile_image_url", imageUrl)
                .addOnSuccessListener(aVoid -> Toast.makeText(ProfileActivity.this, "Image de profil mise à jour avec succès", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Échec de la mise à jour de l'image de profil dans Firestore", Toast.LENGTH_SHORT).show());
    }

    private void loadUserInfo() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            db.collection("utilisateurs").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String prenom = documentSnapshot.getString("prenom");
                            String nom = documentSnapshot.getString("nom");
                            String numTeleStr = documentSnapshot.getString("num_telephone");
                            String emailStr = documentSnapshot.getString("email");
                            String profileImageUrl = documentSnapshot.getString("profile_image_url");

                            // Charger les informations utilisateur dans les vues correspondantes
                            FirstName.setText(prenom);
                            LastName.setText(nom);
                            email.setText(emailStr);
                            numTele.setText(numTeleStr);

                            // Charger l'image de profil si elle existe
                            if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                                Glide.with(ProfileActivity.this).load(profileImageUrl).into(imageView);
                            }
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Échec du chargement des données utilisateur", Toast.LENGTH_SHORT).show());
        }
    }

    private void updatePassword() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            final String newPassword = password.getText().toString();
            String passwordConfirmationStr = passwordConfirmation.getText().toString();

            if (!newPassword.equals(passwordConfirmationStr)) {
                // Si les mots de passe ne correspondent pas, affichez un message d'erreur
                Toast.makeText(ProfileActivity.this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
                return;
            }

            // Mettre à jour le mot de passe de l'utilisateur
            currentUser.updatePassword(newPassword)
                    .addOnSuccessListener(aVoid -> {
                        // Mettre à jour le mot de passe dans Firestore si nécessaire
                        String userId = currentUser.getUid();
                        db.collection("utilisateurs").document(userId)
                                .update("password", newPassword)
                                .addOnSuccessListener(aVoid1 -> Toast.makeText(ProfileActivity.this, "Mot de passe mis à jour avec succès", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Échec de la mise à jour du mot de passe dans Firestore", Toast.LENGTH_SHORT).show());
                    })
                    .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Échec de la mise à jour du mot de passe", Toast.LENGTH_SHORT).show());
        }
    }

    private void updateUserInfo() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String prenom = FirstName.getText().toString();
            String nom = LastName.getText().toString();
            String numTeleStr = numTele.getText().toString();
            String emailStr = email.getText().toString();

            db.collection("utilisateurs").document(userId)
                    .update("prenom", prenom, "nom", nom, "num_telephone", numTeleStr, "email", emailStr)
                    .addOnSuccessListener(aVoid -> Toast.makeText(ProfileActivity.this, "Informations utilisateur mises à jour avec succès", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Échec de la mise à jour des informations utilisateur", Toast.LENGTH_SHORT).show());
        }
    }
}
