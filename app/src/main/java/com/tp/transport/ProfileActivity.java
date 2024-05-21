package com.tp.transport;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ProfileActivity extends Activity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String PREFS_NAME = "MyPrefs"; // SharedPreferences name

    private EditText FirstName;

    private Uri selectedImageUri;

    private EditText LastName;
    private EditText email;


    private ImageView imageView;
    private Button pickImageButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initializeViews();
        setupBottomNavigationView();
        setupPickImageButton();
        setupBackButton();
        setupModifierButton();
    }

    private void initializeViews() {
        FirstName = findViewById(R.id.prenom);
        LastName = findViewById(R.id.nom);
        email = findViewById(R.id.email);
        imageView = findViewById(R.id.imageView);
        pickImageButton = findViewById(R.id.btnPick);
    }

    private void setupBottomNavigationView() {
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_signale) {
                    startActivity(new Intent(ProfileActivity.this, SignalementActivity.class));
                    return true;
                } else if (itemId == R.id.nav_home) {
                    startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                    return true;
                } else if (itemId == R.id.nav_res) {
                    startActivity(new Intent(ProfileActivity.this, EspResActivity.class));
                    return true;
                }
                return false;
            }
        });
    }

    private void setupPickImageButton() {
        pickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void setupBackButton() {
        ImageView back = findViewById(R.id.back1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });
    }

    private void setupModifierButton() {
        Button modifier = findViewById(R.id.modifier);
        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = FirstName.getText().toString() + " " + LastName.getText().toString();
                String Email = email.getText().toString();
                Intent intent = new Intent(ProfileActivity.this, DashActivity.class);
                intent.putExtra("text_key", text);
                intent.putExtra("email_key", Email);
                if (selectedImageUri != null) {
                    intent.putExtra("image_uri_key", selectedImageUri.toString());
                }
                startActivity(intent);
            }
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
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public String getEtFirstName() {
        if (FirstName != null) {
            return FirstName.getText().toString();
        } else {
            return "";
        }
    }
}
