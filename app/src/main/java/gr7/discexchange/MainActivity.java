package gr7.discexchange;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Fragment detailedAdFragment = new DetailedAdFragment();
    Fragment myFeedFragment = new MyFeedFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, myFeedFragment);
        transaction.commit();


        Button detailedAdBtn = findViewById(R.id.detailedAdBtn);
        Button myFeedBtn = findViewById(R.id.myFeedBtn);


        detailedAdBtn.setOnClickListener(view -> {
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.fragment, detailedAdFragment);
            trans.commit();
        });
        myFeedBtn.setOnClickListener(view -> {
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.fragment, myFeedFragment);
            trans.commit();
        });
    }




}