package gr7.discexchange;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import gr7.discexchange.adapter.AdRecycleAdapter;
import gr7.discexchange.model.Ad;

public class MyFeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_feed);

        setupRecyclerView();

    }

    private void setupRecyclerView() {
        RecyclerView adRecyclerView = findViewById(R.id.adRecyclerView);

        adRecyclerView.setAdapter(new AdRecycleAdapter(this, Ad.getData()));

        adRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}