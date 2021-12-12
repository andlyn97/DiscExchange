package gr7.discexchange;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;

import gr7.discexchange.model.Ad;
import gr7.discexchange.service.AdForegroundService;
import gr7.discexchange.viewmodel.AdsViewModel;
import gr7.discexchange.viewmodel.UserViewModel;

public class MainActivity extends AppCompatActivity{
    public DrawerLayout drawerLayout;
    private TextView navUserNameTextView;
    private TextView navAddressTextView;
    private RoundedImageView navImageProfilePic;
    private UserViewModel userViewModel;
    private AdsViewModel adsViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        adsViewModel = new ViewModelProvider(this).get(AdsViewModel.class);
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



        final String CHANNEL_ID = "AD_CHANNEL_ID";
        final String CHANNEL_NAME = "Ny annonse";

        createNotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);

        Intent adForegroundServiceIntent = new Intent(this, AdForegroundService.class);
        FirebaseFirestore.getInstance().collection("ad").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null || adsViewModel.getAds().getValue() == null ) {
                    return;
                }
                for (Ad viewModelAd : adsViewModel.getAds().getValue()) {
                    if(value.getDocumentChanges().get(0).getDocument().getId().equals(viewModelAd.getUid())) {
                        return;
                    }
                }

                Ad ad = value.getDocumentChanges().get(0).getDocument().toObject(Ad.class);
                if(ad == null) {
                    return;
                }
                if(ad.getUserUid().equals(FirebaseAuth.getInstance().getUid())) {
                    return;
                }

                adForegroundServiceIntent.putExtra("EXTRA_ADNAME", ad.getName());
                startForegroundService(adForegroundServiceIntent);
            }
        });
    }

    private void createNotificationChannel(String channelId, String ChannelName, int importance) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, ChannelName, importance);
            channel.setLightColor(Color.GREEN);
            channel.enableLights(true);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    /*private void superHackyChatNotifications() {
        Intent chatForgroundServiceIntent = new Intent(this, ChatForegroundService.class);
        FirebaseFirestore.getInstance().collection("messageRoom").whereArrayContains("usersUid", FirebaseAuth.getInstance().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<DocumentSnapshot> refs = value.getDocuments();
                for (DocumentSnapshot ref : refs) {
                    ref.getReference().collection("messages").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            Message message = value.getDocumentChanges().get(0).getDocument().toObject(Message.class);
                            Log.d("Debug12", message.getMessage());
                            chatForgroundServiceIntent.putExtra("EXTRA_MESSAGE", message.getMessage());
                            startForegroundService(chatForgroundServiceIntent);
                        }
                    });
                }
            }
        });
    }*/

    /*private void createChatNotificationChannel() {
        final String CHANNEL_ID = "chatChannelId";
        final String CHANNEL_NAME = "Ny chat melding";
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setLightColor(Color.GREEN);
            channel.enableLights(true);
            channel.setDescription("Test");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }*/



}