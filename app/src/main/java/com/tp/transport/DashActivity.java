package com.tp.transport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class DashActivity extends AppCompatActivity {
    private TextView textdenom, textdemail;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);

        BottomNavigationView nav = findViewById(R.id.bottomNav);
        textdenom = findViewById(R.id.textdenom);
        textdemail = findViewById(R.id.textView4);
        ImageView imageView = findViewById(R.id.imageView6);
        imageView.setOutlineProvider(ViewOutlineProvider.BACKGROUND);


        // Retrieve the Intent that started this activity
        Intent intent = getIntent();
        String imageUriString = intent.getStringExtra("image_uri_key");
        String text = intent.getStringExtra("text_key");
        String email = intent.getStringExtra("email_key");

        // Set the text in the TextViews
        if (text != null) {
            textdenom.setText(text);
        }

        if (email != null) {
            textdemail.setText(email);
        }

        // Load the image
        if (imageUriString != null) {
            loadProfileImage(imageUriString);
        }

        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.nav_signale){
                    Intent intent = new Intent(DashActivity.this, SignalementActivity.class);
                    startActivity(intent);
                    return true;
                }else if(item.getItemId()==R.id.nav_home) {
                    Intent intent = new Intent(DashActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                }else if(item.getItemId()==R.id.nav_res) {
                    Intent intent = new Intent(DashActivity.this, EspResActivity.class);
                    startActivity(intent);
                    return true;
                }


                return false;

            }
        });

        // Get the ConstraintLayouts
        ConstraintLayout informationsLayout = findViewById(R.id.informationsLayout);
        ConstraintLayout signalementsLayout = findViewById(R.id.signalementsLayout);
        ConstraintLayout traficLayout = findViewById(R.id.traficsLayout);
        ConstraintLayout espaceResponsable = findViewById(R.id.EspaceResponsable);
        ConstraintLayout logout = findViewById(R.id.logout);

        // Set onClickListeners for the layouts
        informationsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityProfile();
            }
        });

        signalementsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivitySignalements();
            }
        });

        traficLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityTrafic();
            }
        });

        espaceResponsable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityEspRes();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
    }

    // Method to load the profile image from a URI string
    private void loadProfileImage(String imageUriString) {
        Uri imageUri = Uri.parse(imageUriString);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to open ProfileActivity
    private void openActivityProfile() {
        Intent intent = new Intent(DashActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    // Method to open SignalementActivity
    private void openActivitySignalements() {
        Intent intent = new Intent(DashActivity.this, SignalementActivity.class);
        startActivity(intent);
    }

    // Method to open TraficInfoActivity
    private void openActivityTrafic() {
        Intent intent = new Intent(DashActivity.this, TraficInfoActivity.class);
        startActivity(intent);
    }

    // Method to open EspResActivity
    private void openActivityEspRes() {
        Intent intent = new Intent(DashActivity.this, EspResActivity.class);
        startActivity(intent);
    }

    // Method to log out
    private void logOut() {
        Intent intent = new Intent(DashActivity.this, LogOutActivity.class);
        startActivity(intent);
    }
}
