package com.example.arturmusayelyan.alarmmaneger;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    private static final int NOTIFICATION_ID = 1;
    private static final String ACTION_NOTIFY = "com.example.android.standup.ACTION_NOTIFY";
    private ToggleButton toggleButton;
    private NotificationManager notificationManager;
    private AlarmManager alarmManager;

    private Intent notifyIntent;
    private PendingIntent notifyPendingIntent;
    private Intent contentIntent;
    private PendingIntent contentPendingIntent;

    private BroadcastReceiver broadcastReceiver;
    private final IntentFilter intentFilter = new IntentFilter("com.example.android.standup.ACTION_NOTIFY");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notifyIntent = new Intent(ACTION_NOTIFY);
        notifyPendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        contentIntent = new Intent(this, MainActivity.class);
        contentPendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        toggleButton = findViewById(R.id.alarm_toggle_button);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String toastmessage;
                if (isChecked) {
                    deliverNotification(MainActivity.this);
                    toastmessage = getString(R.string.alarm_on_toast);
                } else {
                    notificationManager.cancelAll();
                    toastmessage = getString(R.string.alarm_off_toast);
                }
                Toast.makeText(MainActivity.this, toastmessage, Toast.LENGTH_SHORT).show();
            }
        });

        long triggerTime = SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES;
        long repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;

        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, repeatInterval, notifyPendingIntent);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void deliverNotification(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.pets);
        builder.setContentTitle(context.getString(R.string.notification_title));
        builder.setContentText(context.getString(R.string.notification_text));
        builder.setContentIntent(contentPendingIntent);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setAutoCancel(true);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
