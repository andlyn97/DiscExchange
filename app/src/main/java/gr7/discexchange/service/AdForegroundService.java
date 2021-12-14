package gr7.discexchange.service;

import android.app.Notification;
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
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MainActivity.class);

        //TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        //taskStackBuilder.addNextIntentWithParentStack(notificationIntent);
        //PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(123, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 333, notificationIntent, 0);


        String message = intent.getStringExtra("EXTRA_ADNAME");

        Notification notification = new NotificationCompat.Builder(this, "AD_CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_baseline_feed_24)
                .setContentTitle("Ny annonse i feeden")
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setGroup("AD_NOTIFICATION")
                .build();

        // When this code run
        // ERROR: Context.startForegroundService() did not then call Service.startForeground()
        //NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        //manager.notify(123, notification);

        // When this code run
        // Can't discard notification
        startForeground(333, notification);

        return Service.START_NOT_STICKY;
    }
}
