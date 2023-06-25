package com.example.planeatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.planeatapp.databinding.ActivityMainPageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainPageActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView; // BottomBar
    ActivityMainPageBinding binding; // fragments binding
    private String eventID; // the event id in the data base
    private String listID; // the list id in the data base
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        // Retrieve the ID value from the intent
        eventID = getIntent().getStringExtra("event_id");
        listID = getIntent().getStringExtra("list_id");

        String gptResponse = getIntent().getStringExtra("GPTResponse");
        setGPTResponse(gptResponse);

        // Inside onCreate or another suitable function
        requestQueue = Volley.newRequestQueue(this);

        // setting the fragments binding
        binding = ActivityMainPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // setting the BottomBar
        bottomNavigationView = findViewById(R.id.BottomNavigationView);
        bottomNavigationView.setBackground(null);
        replaceFragmentInMainPage(new HomeFragment(eventID)); // setting the default fragment to be the Home fragment
        binding.BottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragmentInMainPage(new HomeFragment(eventID));
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
            Intent intent = new Intent(this, CreateEvent.class);
            startActivity(intent);
        });
    }

    public interface GPTResponseListener {
        void onGPTResponse(String response);
    }

    private GPTResponseListener gptResponseListener;

    public void setGPTResponseListener(GPTResponseListener gptResponseListener) {
        this.gptResponseListener = gptResponseListener;
    }

    public void onResponse(String response) {
        if(gptResponseListener != null){
            gptResponseListener.onGPTResponse(response);
        }
    }

    // setting the GPT Response
    public void setGPTResponse(String gptResponse) {
        // variable to store the GPT Response
    }

    /**
     * Use this public method to insert a new fragment to the fragment_container in the main page.
     *
     * @param fragment the fragment to insert.
     */
    public void replaceFragmentInMainPage(Fragment fragment){
        // Pass listID to HomeFragment
        if (fragment instanceof HomeFragment) {
            Bundle bundle = new Bundle();
            bundle.putString("listID", listID);
            bundle.putString("eventID", eventID);
            fragment.setArguments(bundle);
        }
        // fragments manager
        FragmentManager fragmentManager = getSupportFragmentManager();
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