package com.tp.transport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private List<NotificationItem> notificationList;
    private Context context;

    public NotificationsAdapter(List<NotificationItem> notificationList, Context context) {
        this.notificationList = notificationList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationItem notification = notificationList.get(position);
        holder.tvNotificationTitle.setText(notification.getTitle());
        holder.tvNotificationContent.setText(notification.getContent());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation scaleUp = AnimationUtils.loadAnimation(context, R.anim.scale_up);
                v.startAnimation(scaleUp);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNotificationTitle;
        TextView tvNotificationContent;
        ImageView ivNotificationIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNotificationTitle = itemView.findViewById(R.id.tvNotificationTitle);
            tvNotificationContent = itemView.findViewById(R.id.tvNotificationContent);
            ivNotificationIcon = itemView.findViewById(R.id.ivNotificationIcon);
        }
    }
}
