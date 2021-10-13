package gr7.discexchange;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button detailedAdBtn = findViewById(R.id.detailedAdBtn);
        detailedAdBtn.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), DetailedAdActivity.class)));

        Button myFeedBtn = findViewById(R.id.myFeedBtn);
        myFeedBtn.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), MyFeedActivity.class)));

    }

}