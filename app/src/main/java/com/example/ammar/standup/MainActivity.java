package com.example.ammar.standup;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    ToggleButton mAlarmToggle;
    NotificationManager mNotificationManager;
    private int NOTIFICATION_ID = 0;
    private static final String ACTION_NOTIFY =
            "com.example.ammar.standup.ACTION_NOTIFY";
    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAlarmToggle = (ToggleButton) findViewById(R.id.alarmToggle);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent notifyIntent = new Intent(ACTION_NOTIFY);
        boolean alarmUp = (PendingIntent.getBroadcast(this, 0, notifyIntent,
                PendingIntent.FLAG_NO_CREATE) != null);

        mAlarmToggle.setChecked(alarmUp);
        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast(
                this,
                NOTIFICATION_ID,
                notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        mAlarmToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String toastMessage;
                if (isChecked) {
                    toastMessage = getString(R.string.alarm_on_toast);
                    long triggerTime = SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES;
                    long repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
                    alarmManager.setInexactRepeating(
                            AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            triggerTime, // trigger time when alarm fires
                            repeatInterval, // time interval between alarms
                            notifyPendingIntent
                    );
//                    deliverNotification(MainActivity.this);
                } else {
                    toastMessage = getString(R.string.alarm_off_toast);
                    alarmManager.cancel(notifyPendingIntent);
//                    mNotificationManager.cancelAll();
                }
//                Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void nextAlarm(View view) {
        String nextAlarm = Settings.System.getString(getContentResolver(),
                Settings.System.NEXT_ALARM_FORMATTED);
//        long time = alarmManager.getNextAlarmClock().getTriggerTime();
        Toast.makeText(this, nextAlarm, Toast.LENGTH_SHORT).show();
    }

    /*private void deliverNotification(Context context) {
        Intent contentIntent = new Intent(this, MainActivity.class);
        PendingIntent pd = PendingIntent.getActivity(
                this,
                NOTIFICATION_ID,
                contentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_text))
                .setSmallIcon(R.drawable.ic_stand_up)
                .setContentIntent(pd)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }*/
}
