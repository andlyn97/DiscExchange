package gr7.discexchange.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import gr7.discexchange.R;
import gr7.discexchange.SettingsFragment;

public class ChatForegroundService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent notificationIntent = new Intent(this, SettingsFragment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        String message = intent.getStringExtra("EXTRA_MESSAGE");

        Notification notification = new NotificationCompat.Builder(this, "chatChannelId")
                .setSmallIcon(R.drawable.ic_baseline_chat_24)
                .setContentTitle("Du har fått en ny melding")
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();


        startForeground(1, notification);

        return Service.START_REDELIVER_INTENT;
    }
}
