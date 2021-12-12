package gr7.discexchange;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;

import gr7.discexchange.model.Ad;
import gr7.discexchange.service.AdForegroundService;
import gr7.discexchange.viewmodel.UserViewModel;

public class MainActivity extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    private TextView navUserNameTextView;
    private TextView navAddressTextView;
    private RoundedImageView navImageProfilePic;
    private UserViewModel userViewModel;
    private static final String TAG = MainActivity.class.getName();
    private static SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String mode = pref.getString("screenmode", null);

        switch (mode) {
            case "systemvalgt":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            case "lyst":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "morkt":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }

        Log.d("Maome", "isConnected = " + isConnected());

        /*if (isConnected()) {
            Log.d("Maome", "Er koblet til nettverk");
        } else if (!isConnected()) {
            Log.d("Maome", "Er ikke koblet til nettverk");
        }*/

        setContentView(R.layout.activity_main);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        if(userViewModel.getUser() == null) {
            Navigation.findNavController(this, R.id.navHostFragment).navigate(R.id.notMenuEditProfile);
        }
        drawerLayout = findViewById(R.id.drawer_layout);


        findViewById(R.id.imageMenu).setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

        NavigationView drawerNavView = findViewById(R.id.navigationView);
        BottomNavigationView bottomNavView = findViewById(R.id.bottomNav);
        drawerNavView.setItemIconTintList(null);


        View headerView = drawerNavView.getHeaderView(0);

        navUserNameTextView = headerView.findViewById(R.id.navUserName);
        navAddressTextView = headerView.findViewById(R.id.navAddress);
        navImageProfilePic = headerView.findViewById(R.id.imageProfilePic);



        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.main);
        navGraph.setStartDestination(R.id.menuFeed);
        navController.setGraph(navGraph);


        userViewModel.getUser().observe(this, user -> {
            if(user == null) {
                return;
            }
            navUserNameTextView.setText(user.getName());
            navAddressTextView.setText(user.getAddress());
            Glide.with(getApplicationContext()).load(user.getImageUrl()).into(navImageProfilePic);
        });


        navImageProfilePic.setOnClickListener(view -> {
            navController.navigate(R.id.menuProfile);
            drawerLayout.closeDrawer(GravityCompat.START);
        });
        navUserNameTextView.setOnClickListener(view -> {
            navController.navigate(R.id.menuProfile);
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        NavigationUI.setupWithNavController(drawerNavView, navController);
        NavigationUI.setupWithNavController(bottomNavView, navController);


        final TextView textTitle = findViewById(R.id.textTitle);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> textTitle.setText(destination.getLabel()));



        createNotificationChannel();
        Intent adForegroundServiceIntent = new Intent(this, AdForegroundService.class);
        FirebaseFirestore.getInstance().collection("ad").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null) {
                    return;
                }

                Ad ad = value.getDocumentChanges().get(0).getDocument().toObject(Ad.class);
                adForegroundServiceIntent.putExtra("EXTRA_ADNAME", ad.getName());
                startForegroundService(adForegroundServiceIntent);
            }
        });
    }

    private void createNotificationChannel() {
        final String CHANNEL_ID = "AD_CHANNEL_ID";
        final String CHANNEL_NAME = "Ny annonse";
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setLightColor(Color.GREEN);
            channel.enableLights(true);
            channel.setDescription("AD CHANNEL");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetwork() == null ) {
            return false;
        }

        return cm.getActiveNetworkInfo().isConnectedOrConnecting();

    }

}