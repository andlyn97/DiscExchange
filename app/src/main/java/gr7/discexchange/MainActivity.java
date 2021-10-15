package gr7.discexchange;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    Fragment detailedAdFragment = new DetailedAdFragment();
    Fragment myFeedFragment = new MyFeedFragment();
    Fragment profileFragment = new ProfileFragment();
    Fragment popularFragment = new PopularFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);

        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navigationView, navController);

        /*FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, myFeedFragment);
        transaction.commit();


        Button detailedAdBtn = findViewById(R.id.detailedAdBtn);
        Button myFeedBtn = findViewById(R.id.myFeedBtn);


        detailedAdBtn.setOnClickListener(view -> {
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.fragment, popularFragment);
            trans.commit();
        });
        myFeedBtn.setOnClickListener(view -> {
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.fragment, myFeedFragment);
            trans.commit();
        });*/
    }




}