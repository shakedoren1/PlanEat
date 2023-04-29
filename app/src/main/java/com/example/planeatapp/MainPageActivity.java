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
    private FragmentManager fragmentManager; // fragments manager

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

        newEventBtn.setOnClickListener(v -> {
            SummaryFragment summaryFragment = new SummaryFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, summaryFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
    }

    /**
     * Use this public method to insert a new fragment to the fragment_container in the main page.
     *
     * @param fragment the fragment to insert.
     */
    public void replaceFragmentInMainPage(Fragment fragment){
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null); // Add the replaced fragment to the back stack
        fragmentTransaction.commit();
    }

    /**
     * Allows the default back button of Android to work with the fragments.
     * Make sure when the fragments are replaced they are added to the stack.
     */
    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            // pop the last fragment
            getSupportFragmentManager().popBackStackImmediate();
            getSupportFragmentManager().popBackStack();
            // sets the bottom bar to the right state
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (currentFragment instanceof HomeFragment || currentFragment instanceof ListFragment) {
                bottomNavigationView.setSelectedItemId(R.id.home);
            } else if (currentFragment instanceof InsightsFragment) {
                bottomNavigationView.setSelectedItemId(R.id.insights);
            } else if (currentFragment instanceof NotificationsFragment) {
                bottomNavigationView.setSelectedItemId(R.id.notifications);
            } else if (currentFragment instanceof UserFragment) {
                bottomNavigationView.setSelectedItemId(R.id.user);
            }
        }
    }
}