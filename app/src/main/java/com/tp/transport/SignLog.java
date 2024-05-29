package com.tp.transport;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignLog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_log);

        TextView textView = findViewById(R.id.message);
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        textView.setTextColor(Color.BLUE);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        textView.setTextColor(Color.WHITE);
                        break;
                }
                return false;
            }
        });

        LinearLayout utilisateurLayout = findViewById(R.id.utilisateur);
        LinearLayout responsableLayout = findViewById(R.id.responsable);

        utilisateurLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignLog.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        responsableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignLog.this, EspResActivity.class);
                startActivity(intent);
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignLog.this, ResponsableActivity1.class);
                startActivity(intent);
            }
        });
    }
}
