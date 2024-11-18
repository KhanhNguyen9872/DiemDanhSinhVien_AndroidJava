package com.example.DiemDanhSV;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lap23.R;

public class NotificationActivity extends AppCompatActivity {

    private LinearLayout notificationsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        notificationsList = findViewById(R.id.notificationsList);

        // Add sample notifications
        addNotification("New Message", "You have received a new message.", R.drawable.ic_message);
        addNotification("App Update", "A new version of the app is available.", R.drawable.ic_update);
        addNotification("Friend Request", "You have a new friend request.", R.drawable.ic_mail);
    }

    private void addNotification(String title, String description, int iconRes) {
        // Inflate the notification item layout
        View notificationItem = LayoutInflater.from(this).inflate(R.layout.notification_item, notificationsList, false);

        // Set title and description
        TextView notificationTitle = notificationItem.findViewById(R.id.notificationTitle);
        TextView notificationDescription = notificationItem.findViewById(R.id.notificationDescription);
        ImageView notificationIcon = notificationItem.findViewById(R.id.notificationIcon);

        notificationTitle.setText(title);
        notificationDescription.setText(description);
        notificationIcon.setImageResource(iconRes);

        // Add the notification item to the list
        notificationsList.addView(notificationItem);
    }
}
