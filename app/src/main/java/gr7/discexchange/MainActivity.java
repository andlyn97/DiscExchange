package gr7.discexchange;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;

import gr7.discexchange.model.User;
import gr7.discexchange.viewmodel.DEViewModel;

public class MainActivity extends AppCompatActivity{
    public DrawerLayout drawerLayout;
    private TextView navUserNameTextView;
    private TextView navAddressTextView;
    private RoundedImageView navImageProfilePic;
    private DEViewModel viewModel;
    private ListenerRegistration userFirestoreListenerRegistration;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(DEViewModel.class);

        drawerLayout = findViewById(R.id.drawer_layout);


        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView drawerNavView = findViewById(R.id.navigationView);
        BottomNavigationView bottomNavView = findViewById(R.id.bottomNav);
        drawerNavView.setItemIconTintList(null);


        View headerView = drawerNavView.getHeaderView(0);

        navUserNameTextView = headerView.findViewById(R.id.navUserName);
        navAddressTextView = headerView.findViewById(R.id.navAddress);
        navImageProfilePic = headerView.findViewById(R.id.imageProfilePic);



        logout(drawerNavView);


        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.main);
        navGraph.setStartDestination(R.id.menuFeed);
        navController.setGraph(navGraph);


        viewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user == null) {
                    return;
                }
                navUserNameTextView.setText(user.getName());
                navAddressTextView.setText(user.getAddress());
                Glide.with(getApplicationContext()).load(user.getImageUrl()).into(navImageProfilePic);
            }
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

        setupFirestoreListener();
    }

    private void logout(NavigationView drawerNavView) {
        drawerNavView.getMenu().findItem(R.id.menuLogout).setOnMenuItemClickListener(item -> {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(task -> {
                        Toast.makeText(getApplicationContext(), "Logger ut", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    });
            return true;
        });
    }

    public void setupFirestoreListener() {
        userFirestoreListenerRegistration = FirebaseFirestore.getInstance().collection("user").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }

                for (DocumentChange docChange : value.getDocumentChanges()) {
                    User userFromFS = docChange.getDocument().toObject(User.class);
                    userFromFS.setUid(docChange.getDocument().getId());

                    if (docChange.getDocument().getId().equals(FirebaseAuth.getInstance().getUid())) {
                        viewModel.setUser(userFromFS);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupFirestoreListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(userFirestoreListenerRegistration != null) {
            userFirestoreListenerRegistration.remove();
        }
    }
}