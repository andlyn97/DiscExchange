package gr7.discexchange;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class MyFeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_feed);

        setupRecyclerView();

    }

    private void setupRecyclerView() {
        RecyclerView adRecyclerView = findViewById(R.id.adRecyclerView);
    }
}