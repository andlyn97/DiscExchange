package gr7.discexchange.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import gr7.discexchange.MainActivity;
import gr7.discexchange.R;

public class AdForegroundService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent notificationIntent = new Intent(this, MainActivity.class);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addNextIntentWithParentStack(notificationIntent);

        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(123, PendingIntent.FLAG_UPDATE_CURRENT);

        String message = intent.getStringExtra("EXTRA_ADNAME");

        Notification notification = new NotificationCompat.Builder(this, "AD_CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_baseline_feed_24)
                .setContentTitle("Ny annonse i feeden")
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        startForeground(123, notification);


        return Service.START_NOT_STICKY;
    }
}
