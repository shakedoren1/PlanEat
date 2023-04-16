package com.example.planeatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class NewEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        Button nextButton = findViewById(R.id.newEventButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                // Hide logo and text views
                ImageView logo = findViewById(R.id.logo1);
                TextView title = findViewById(R.id.planTitle);
                TextView info = findViewById(R.id.planInfo);
                logo.setVisibility(View.GONE);
                title.setVisibility(View.GONE);
                info.setVisibility(View.GONE);

                // Replace container view with DateTimeFragment
                DateTimeFragment dateTimeFragment = new DateTimeFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.dt_fragment_container, dateTimeFragment);
                transaction.addToBackStack(null);
                transaction.commit();

                // Hide button
                nextButton.setVisibility(View.GONE);
            }
        });
    }
}