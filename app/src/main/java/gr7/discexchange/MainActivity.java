package gr7.discexchange;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

import gr7.discexchange.model.Ad;

public class MainActivity extends AppCompatActivity {

    Fragment detailedAdFragment = new DetailedAdFragment();
    Fragment myFeedFragment = new MyFeedFragment();
    Fragment profileFragment = new ProfileFragment();
    Fragment popularFragment = new PopularFragment();
    private FirebaseFirestore firestoreDb;
    private CollectionReference adCollectionReference;


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuLogout) {
            Toast.makeText(getApplicationContext(), "User logged out", Toast.LENGTH_LONG).show();
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(task -> startActivity(new Intent(getApplicationContext(), LoginActivity.class)));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

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

        final TextView textTitle = findViewById(R.id.textTitle);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                textTitle.setText(destination.getLabel());
            }
        });

        // Test mot firestore

        firestoreDb = FirebaseFirestore.getInstance();
        adCollectionReference = firestoreDb.collection("ad");


        //generateTestDataToDb();



    }

    private void generateTestDataToDb() {
        ArrayList<Ad> ads = new ArrayList<>();
        ads.add(new Ad("Explorer", R.drawable.explorer, "Latitude 64", 9, "7 5 0 2", "Hvit", "Ingen", "Aldri kastet, bare oppbevaringsskader.", "P2 C-line < 7/10", 0.0, "2021-10-15", "", ""));
        ads.add(new Ad("Firebird", R.drawable.firebird, "Innova", 7, "9 3 0 4", "Rød", "I rim", "Ingen store skader annet enn vanlig slitasje.", "River Opto-X", 0.0, "2021-10-15", "", ""));

        for (Ad ad : ads) {
            adCollectionReference.add(ad);
        }
    }
}