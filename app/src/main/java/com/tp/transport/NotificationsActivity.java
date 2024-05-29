package com.tp.transport;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView rvNotifications;
    private TextView tvNoNotifications;
    private NotificationsAdapter adapter;
    private List<NotificationItem> notificationList;
    private ImageView backres;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        rvNotifications = findViewById(R.id.rvNotifications);
        tvNoNotifications = findViewById(R.id.tvNoNotifications);
        backres = findViewById(R.id.backres);

        backres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Return to the previous activity
            }
        });

        notificationList = new ArrayList<>();
        // Load notifications from your data source
        loadNotifications();

        adapter = new NotificationsAdapter(notificationList, this);
        rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        rvNotifications.setAdapter(adapter);

        if (notificationList.isEmpty()) {
            tvNoNotifications.setVisibility(View.VISIBLE);
        } else {
            tvNoNotifications.setVisibility(View.GONE);
        }
    }

    private void loadNotifications() {
        // This is where you would load notifications from your data source
        // For now, we'll add some dummy data
        notificationList.add(new NotificationItem("Title 1", "Content 1"));
        notificationList.add(new NotificationItem("Title 2", "Content 2"));
    }

    public void deleteNotification(int position) {
        notificationList.remove(position);
        adapter.notifyItemRemoved(position);
        if (notificationList.isEmpty()) {
            tvNoNotifications.setVisibility(View.VISIBLE);
        }
    }
}
