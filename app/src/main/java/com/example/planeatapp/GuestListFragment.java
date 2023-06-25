package com.example.planeatapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuestListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuestListFragment extends Fragment {

    private static String eventID; // The ID of the event in the data base
    private RelativeLayout relativeLayout; // the RelativeLayout in fragment_home
    private boolean firstFriend = true; // mark first friend
    private int last_view_id; // the id of the last friend drawn
    private ProgressBar loadingProgress; // the progress bar
    private View defaultMessage; // the default message written on the screen
    private RetrofitInterface retrofitInterface; // Server interface

    public GuestListFragment() {
        // Required empty public constructor
    }

    /**
     * A constructor that puts inside eventID the current event ID.
     */
    public GuestListFragment(String ID) {
        eventID = ID;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GuestListFragment.
     */
    public static GuestListFragment newInstance() {
        GuestListFragment fragment = new GuestListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Server setup
        String BASE_URL = "http://websiteserver.shakedoren1.repl.co";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_guest_list, container, false);
        loadingProgress = view.findViewById(R.id.loading_progress);
        defaultMessage = view.findViewById(R.id.default_message);

        // Updating from the data base the friends confirmation
        updateFriends(eventID);

        return view;
    }

    /**
     * A GET request to server endpoint to get the friends list info from the database based on the event ID.
     */
    private void updateFriends(String id) {
        Log.e("Update Friends confirmations", "passed: " + id);

        Call<List<Confirmation>> call = retrofitInterface.executeEventCon(id);

        call.enqueue(new Callback<List<Confirmation>>() {
            @Override
            public void onResponse(@NonNull Call<List<Confirmation>> call, @NonNull Response<List<Confirmation>> response) {
                if (response.isSuccessful()) {
                    List<Confirmation> confirmations = response.body();
                    if (confirmations != null) {
                        for (Confirmation confirmation : confirmations) {
                            String name = confirmation.getName();
                            String option = confirmation.getOption();
                            // Convert the first character of name to uppercase
                            char firstLetter = Character.toUpperCase(name.charAt(0));
                            // Call the drawFriend method with the name, uppercase first letter and option
                            drawFriend(name, firstLetter, option);
                        }
                    } else {
                        Log.e("Update Friends confirmations", "No confirmations yet");
                    }
                    // dismissing the loading sign
                    loadingProgress.setVisibility(View.GONE);
                } else {
                    Log.e("Update Friends confirmations", "Problem with retrieve event confirmations" + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Confirmation>> call, @NonNull Throwable t) {
                Log.e("Update Friends confirmations", "Failed to retrieve event confirmations" + t.getMessage());
            }
        });
    }

    /**
     * A method to draw the circle for each confirmation received
     */
    private void drawFriend(String name, char letter, String option) {
        defaultMessage.setVisibility(View.GONE);
        drawBackCircle(option);
        drawFrontCircle();
        drawLetter(letter);
        drawName(name);
        drawLine();
    }

    /**
     * A method to draw the back circle
     */
    private void drawBackCircle(String option) {
        // Retrieves the RelativeLayout from fragment_home
        relativeLayout = Objects.requireNonNull(getView()).findViewById(R.id.fragment_guest_list_id);

        // Creates a new instance of the View class
        View circleView = new View(getContext());

        // Set the ID of the circleView
        int generatedId = View.generateViewId();
        circleView.setId(generatedId);

        // Sets the desired attributes for the view
        int friendSizeInDp = 25;
        int friendMarginTopInDp = 15;
        int friendMarginStartInDp = 30;

        // Converts dp to pixels
        float scale = getResources().getDisplayMetrics().density;
        int friendSize = (int) (friendSizeInDp * scale + 0.5f);
        int friendMarginTop = (int) (friendMarginTopInDp * scale + 0.5f);
        int friendMarginStart = (int) (friendMarginStartInDp * scale + 0.5f);

        // updates the view
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(friendSize, friendSize);
        layoutParams.setMargins(friendMarginStart, friendMarginTop, 0, 0);
        if (firstFriend) { // first friend
            layoutParams.addRule(RelativeLayout.BELOW, R.id.arrival_approved_explanation);
            firstFriend = false;
        } else {
            layoutParams.addRule(RelativeLayout.BELOW, last_view_id);
        }
        circleView.setLayoutParams(layoutParams);

        // Set background based on the option parameter
        if (option.equals("Coming")) {
            circleView.setBackgroundResource(R.drawable.green_circle);
        } else {
            circleView.setBackgroundResource(R.drawable.red_circle);
        }

        // Adds the view to the RelativeLayout
        relativeLayout.addView(circleView);

        // Updates last_view_id to the current id
        last_view_id = generatedId;
    }

    /**
     * A method to draw the front circle
     */
    private void drawFrontCircle() {
        // Creates a new instance of the View class
        View circleView = new View(getContext());

        // Set the ID of the circleView
        int generatedId = View.generateViewId();
        circleView.setId(generatedId);

        // Sets the desired attributes for the view
        int circleSizeInDp = 20;
        int circleMarginInDp = 3;

        // Converts dp to pixels
        float scale = getResources().getDisplayMetrics().density;
        int circleSize = (int) (circleSizeInDp * scale + 0.5f);
        int circleMargin = (int) (circleMarginInDp * scale + 0.5f);

        // updates the view
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(circleSize, circleSize);
        layoutParams.addRule(RelativeLayout.ALIGN_START, last_view_id);
        layoutParams.addRule(RelativeLayout.ALIGN_TOP, last_view_id);
        layoutParams.addRule(RelativeLayout.ALIGN_END, last_view_id);
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, last_view_id);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        layoutParams.setMargins(circleMargin, circleMargin, circleMargin, circleMargin);
        circleView.setLayoutParams(layoutParams);
        circleView.setBackgroundResource(R.drawable.light_circle);

        // Adds the view to the RelativeLayout
        relativeLayout.addView(circleView);
    }

    /**
     * A method to draw the letter inside the circle
     */
    private void drawLetter(char letter) {
        // Creates a new instance of the View class
        TextView letterView = new TextView(getContext());

        // Set the ID of the letterView
        int generatedId = TextView.generateViewId();
        letterView.setId(generatedId);

        // updates the view
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_START, last_view_id);
        layoutParams.addRule(RelativeLayout.ALIGN_TOP, last_view_id);
        layoutParams.addRule(RelativeLayout.ALIGN_END, last_view_id);
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, last_view_id);
        int textColor = ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.black);
        letterView.setTextColor(textColor);
        letterView.setText(String.valueOf(letter));
        letterView.setGravity(Gravity.CENTER);
        letterView.setLayoutParams(layoutParams);

        // Adds the view to the RelativeLayout
        relativeLayout.addView(letterView);
    }

    /**
     * A method to draw the full name
     */
    private void drawName(String name) {
        // Creates a new instance of the View class
        TextView nameView = new TextView(getContext());

        // Set the ID of the circleView
        int generatedId = TextView.generateViewId();
        nameView.setId(generatedId);

        // Sets the desired attributes for the view
        int nameMarginInDp = 10;

        // Converts dp to pixels
        float scale = getResources().getDisplayMetrics().density;
        int nameMargin = (int) (nameMarginInDp * scale + 0.5f);

        // updates the view
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_TOP, last_view_id);
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, last_view_id);
        layoutParams.addRule(RelativeLayout.RIGHT_OF, last_view_id);
        layoutParams.setMargins(nameMargin, 0, 0, 0);
        int textColor = ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.black);
        nameView.setTextColor(textColor);
        nameView.setText(String.valueOf(name));
        nameView.setGravity(Gravity.CENTER);
        nameView.setLayoutParams(layoutParams);

        // Adds the view to the RelativeLayout
        relativeLayout.addView(nameView);

        // Updates last_view_id to the current id
        last_view_id = generatedId;
    }

    /**
     * A method to draw the line
     */
    private void drawLine() {
        // Creates a new instance of the View class
        TextView lineView = new TextView(getContext());

        // Set the ID of the circleView
        int generatedId = TextView.generateViewId();
        lineView.setId(generatedId);

        // Sets the desired attributes for the view
        int heightInDp = 1;
        int marginStartInDP = 60;
        int marginTopInDP = 15;

        // Converts dp to pixels
        float scale = getResources().getDisplayMetrics().density;
        int height = (int) (heightInDp * scale + 0.5f);
        int marginStart = (int) (marginStartInDP * scale + 0.5f);
        int marginTop = (int) (marginTopInDP * scale + 0.5f);

        // updates the view
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height);
        layoutParams.addRule(RelativeLayout.BELOW, last_view_id);
        layoutParams.setMargins(marginStart, marginTop, marginStart, 0);
        lineView.setLayoutParams(layoutParams);
        lineView.setBackgroundResource(R.drawable.line_background);

        // Adds the view to the RelativeLayout
        relativeLayout.addView(lineView);

        // Updates last_view_id to the current id
        last_view_id = generatedId;
    }
}