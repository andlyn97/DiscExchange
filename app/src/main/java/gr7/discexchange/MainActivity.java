package gr7.discexchange;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import gr7.discexchange.model.Ad;

public class MainActivity extends AppCompatActivity{

    private FirebaseFirestore firestoreDb;
    private CollectionReference adCollectionReference;
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);

        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);
        navigationView.getMenu().findItem(R.id.menuLogout).setOnMenuItemClickListener(item -> {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "Logger ut", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                    });
            return true;
        });



        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navigationView, navController);

        final TextView textTitle = findViewById(R.id.textTitle);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> textTitle.setText(destination.getLabel()));

        // Test mot firestore
        firestoreDb = FirebaseFirestore.getInstance();
        adCollectionReference = firestoreDb.collection("ad");
        //generateTestDataToDb();

    }

    private void generateTestDataToDb() {
        ArrayList<Ad> ads = new ArrayList<>();
        // R.drawable skal byttes ut, må laste opp bilde til db for å kunne bruke det overalt.
        ads.add(new Ad("Explorer", "Latitude 64", 9, "7 5 0 2", "Hvit", "Ingen", "Aldri kastet, bare oppbevaringsskader.", "P2 C-line < 7/10", 0.0, "2021-10-15", "", ""));
        ads.add(new Ad("Firebird", "Innova", 7, "9 3 0 4", "Rød", "I rim", "Ingen store skader annet enn vanlig slitasje.", "River Opto-X", 0.0, "2021-10-15", "", ""));

        for (Ad ad : ads) {
            adCollectionReference.add(ad);
        }
    }
}