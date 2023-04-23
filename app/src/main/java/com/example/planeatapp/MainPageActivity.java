package com.example.planeatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.planeatapp.databinding.ActivityMainPageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainPageActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView; // BottomBar
    ActivityMainPageBinding binding; // fragments binding

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        // setting the fragments binding
        binding = ActivityMainPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // setting the BottomBar
        bottomNavigationView = findViewById(R.id.BottomNavigationView);
        bottomNavigationView.setBackground(null);
        replaceFragmentInMainPage(new HomeFragment()); // setting the default fragment to be the Home fragment
        binding.BottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    replaceFragmentInMainPage(new HomeFragment());
                    break;
                case R.id.insights:
                    replaceFragmentInMainPage(new InsightsFragment());
                    break;
                case R.id.notifications:
                    replaceFragmentInMainPage(new NotificationsFragment());
                    break;
                case R.id.user:
                    replaceFragmentInMainPage(new UserFragment());
                    break;
            }
            return true;
        });


        FloatingActionButton newEventBtn = findViewById(R.id.newEventButton);
        newEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPageActivity.this, NewEvent.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Use this public method to insert a new fragment to the fragment_container in the main page.
     *
     * @param fragment the fragment to insert.
     */
    public void replaceFragmentInMainPage(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}