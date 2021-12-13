package gr7.discexchange.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;

import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.DialogCompat;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class InternetConnectionService extends Service {

    // Inspiration / stole from:
    // https://stackoverflow.com/questions/41828490/check-internet-connection-in-background
    // https://stackoverflow.com/questions/21073844/disable-app-interaction-with-user-on-internet-connection-lost-android
    static final String CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    private BroadcastReceiver bReceiver;
    private AlertDialog alertDialog;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter iFilter = new IntentFilter(CONNECTIVITY_CHANGE);
        bReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context ctx, Intent i) {
                // ctx does not have the right theme, so the app crashes when trying to make a alertDialog
                String action = i.getAction();
                if(!CONNECTIVITY_CHANGE.equals(action)) {
                    return;
                }
                if(hasInternet()){
                    Toast.makeText(ctx, "Internett er på", Toast.LENGTH_LONG).show();
                    /*if(alertDialog == null) {
                        return;
                    }
                    if(alertDialog != null || alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }*/
                    return;
                }
                Toast.makeText(ctx, "Ingen forbindelse til internett", Toast.LENGTH_LONG).show();
                /*if(alertDialog == null || !alertDialog.isShowing()) {
                    alertDialog = new AlertDialog.Builder(ctx)
                            .setTitle("Ingen internett tilgang")
                            .setMessage("Sjekk tilkoblingen din")
                            .setCancelable(false)
                            .create();
                    alertDialog.show();
                }*/
            }
        };
        registerReceiver(bReceiver, iFilter);
        return Service.START_STICKY;
    }

    private boolean hasInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bReceiver);
    }

}
