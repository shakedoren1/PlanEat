package com.example.planeatapp;

import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
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

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private static String eventID; // The ID of the event in the data base
    private static String listID; // The ID of the list in the data base
    private boolean firstBuilt = true; // mark build
    private RelativeLayout relativeLayout; // the RelativeLayout in fragment_home
    private int circles = 0; // number of confirmations drawn
    private int friend_circle_back_id; // the id of the last circle drawn
    private ProgressBar loadingProgress; // the progress bar
    private View defaultView; // the home default view
    private RetrofitInterface retrofitInterface; // Server interface

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * A constructor that puts the current ID of the event in the data base inside the eventID.
     */
    public HomeFragment(String ID) {
        eventID = ID;
    }

    /**
     * Use this factory method to create a new instance of this fragment.
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get listID from arguments
        Bundle arguments = getArguments();
        if (arguments != null) {
            listID = arguments.getString("listID");
            eventID = arguments.getString("eventID");
        }

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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        defaultView = view.findViewById(R.id.default_home);
        loadingProgress = view.findViewById(R.id.loading_progress);

        // attaching the buttons
        AppCompatButton guestListButton = view.findViewById(R.id.guest_list_button);
        AppCompatButton groupTasksButton = view.findViewById(R.id.group_tasks_button);

        // sets the guestListButton
        guestListButton.setOnClickListener(v ->
                ((MainPageActivity) requireActivity()).replaceFragmentInMainPage(
                        new ListFragment("Friends list", new GuestListFragment(eventID))));

        // sets the groupTasksButton
        groupTasksButton.setOnClickListener(v -> {
            IngredientListFragment ingredientListFragment = new IngredientListFragment(eventID);

            // Pass listID to GroupTaskListFragment
            Bundle bundle = new Bundle();
            bundle.putString("listID", listID);
            ingredientListFragment.setArguments(bundle);

            Log.e("HomeFragment", "Home listID: " + listID);  // add this line

            ((MainPageActivity) requireActivity()).replaceFragmentInMainPage(
                    new ListFragment("Don't forget these", ingredientListFragment));
        });

        // only allows to be built one time
        if (firstBuilt) {
            updateEventInfo(eventID);
            updateFriendsList(eventID);
            firstBuilt = false;
        }

        return view;
    }

    /**
     * A GET request to server endpoint to get the event info from the database.
     */
    private void updateEventInfo(String id) {
        Log.e("Update Event Info", "passed: " + id );

        Call<EventDetails> call = retrofitInterface.executeEventInfo(id);

        call.enqueue(new Callback<EventDetails>() {
            @Override
            public void onResponse(Call<EventDetails> call, Response<EventDetails> response) {
                if (response.isSuccessful()) {
                    EventDetails eventInfo = response.body();
                    if (eventInfo != null) {
                        // updating the home fragment with all the details received from the database
                        String title = eventInfo.getTitle();
                        if (title != "")
                            updateText("title", title);
                        String date = eventInfo.getDate();
                        if (date != "")
                            updateText("date", date);
                        String time = eventInfo.getTime();
                        if (time != "")
                            updateText("time", time);
                        String place = eventInfo.getPlace();
                        if (place != "")
                            updateText("place", place);
                        String number = eventInfo.getNumber();
                        updateText("number", number);
                    }
                } else {
                    Log.e("Update Event Info", "Problem with retrieve event info" + response.message());
                }
            }

            @Override
            public void onFailure(Call<EventDetails> call, Throwable t) {
                Log.e("Update Event Info", "Failed to retrieve event info" + t.getMessage());
            }
        });
    }

    /**
     * A method that updates the text in the fragment received to the value received from database.
     */
    private void updateText(String name, String text) {
        // putting all the relevant text_views into a hashMap
        HashMap<String, Integer> textMap = new HashMap<>();
        textMap.put("title", R.id.event_title);
        textMap.put("date", R.id.date_text);
        textMap.put("time", R.id.time_text);
        textMap.put("place", R.id.location_text);
        textMap.put("number", R.id.potential_text);

        // putting the text received inside the relevant text_view
        if (textMap.containsKey(name)) {
            int nameId = textMap.get(name);
            TextView textView = getView().findViewById(nameId);
            textView.setText(text);
        }
    }

    /**
     * A GET request to server endpoint to get the friends list info from the database based on the event ID.
     */
    private void updateFriendsList(String id) {
        Log.e("Update Friends List", "passed: " + id );

        Call<List<Confirmation>> call = retrofitInterface.executeEventCon(id);

        call.enqueue(new Callback<List<Confirmation>>() {
            @Override
            public void onResponse(Call<List<Confirmation>> call, Response<List<Confirmation>> response) {
                if (response.isSuccessful()) {
                    List<Confirmation> confirmations = response.body();
                    if (confirmations != null) {
                        for (Confirmation confirmation : confirmations) {
                            if (circles < 7) { // The amount of circles to fit in the screen
                                String name = confirmation.getName();
                                String option = confirmation.getOption();
                                // Convert the first character of name to uppercase
                                char firstLetter = Character.toUpperCase(name.charAt(0));
                                // Call the drawCircle method with the uppercase first letter and option
                                drawCircle(firstLetter, option);
                            } else {
                                // Count the circles and update the answered_text
                                circles++;
                                TextView answeredText = getView().findViewById(R.id.answered_text);
                                answeredText.setText(String.valueOf(circles));
                            }
                        }
                        // dismissing the loading sign
                        defaultView.setVisibility(View.VISIBLE);
                        loadingProgress.setVisibility(View.GONE);
                    } else {
                        Log.e("Update Friends List", "No confirmations yet");
                    }
                } else {
                    Log.e("Update Friends List", "Problem with retrieve event confirmations" + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Confirmation>> call, Throwable t) {
                Log.e("Update Friends List", "Failed to retrieve event confirmations" + t.getMessage());
            }
        });
    }

    /**
     * A method to draw the circle for each confirmation received
     */
    private void drawCircle(char letter, String option) {
        drawBackCircle(option);
        drawFrontCircle();
        drawLetter(letter);
    }

    /**
     * A method to draw the back circle
     */
    private void drawBackCircle(String option) {
        // Retrieves the RelativeLayout from fragment_home
        relativeLayout = getView().findViewById(R.id.default_home);

        // Find the confirmed_bottom_text TextView and set its visibility to "invisible"
        TextView confirmedBottomText = getView().findViewById(R.id.confirmed_bottom_text);
        confirmedBottomText.setVisibility(View.INVISIBLE);

        // Creates a new instance of the View class
        View circleView = new View(getContext());

        // Set the ID of the circleView
        int generatedId = View.generateViewId();
        circleView.setId(generatedId);
        if (circles == 0) { // first circle
            friend_circle_back_id = generatedId;
        }

        // Sets the desired attributes for the view
        int circleSizeInDp = 25;
        int circleMarginInDp = 17;
        int circleMarginBTWInDp = 35;
        int circleElevationInDp = 10;

        // Converts dp to pixels
        float scale = getResources().getDisplayMetrics().density;
        int circleSize = (int) (circleSizeInDp * scale + 0.5f);
        int circleMargin = (int) (circleMarginInDp * scale + 0.5f);
        int circleMarginBTW = (int) (circleMarginBTWInDp * scale + 0.5f);
        int circleElevation = (int) (circleElevationInDp * scale + 0.5f);

        // updates the view
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(circleSize, circleSize);
        if (circles == 0) { // first circle
            layoutParams.addRule(RelativeLayout.ALIGN_START, R.id.guest_list_button);
            layoutParams.setMargins(circleMargin, circleMargin, circleMargin, circleMargin);
        } else {
            layoutParams.addRule(RelativeLayout.ALIGN_LEFT, friend_circle_back_id);
            layoutParams.setMargins(circleMarginBTW, circleMargin, circleMargin, circleMargin);
        }
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.guest_list_button);
        circleView.setLayoutParams(layoutParams);
        circleView.setElevation(circleElevation);

        // Set background based on the option parameter
        if (option.equals("Coming")) {
            circleView.setBackgroundResource(R.drawable.green_circle);
        } else {
            circleView.setBackgroundResource(R.drawable.red_circle);
        }

        // Adds the view to the RelativeLayout
        relativeLayout.addView(circleView);

        // Count the circles, update the answered_text and friend_circle_back_id
        circles++;
        TextView answeredText = getView().findViewById(R.id.answered_text);
        answeredText.setText(String.valueOf(circles));

        // Updates last_friend_id to the current id
        friend_circle_back_id = generatedId;
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
        int circleElevationInDp = 10;

        // Converts dp to pixels
        float scale = getResources().getDisplayMetrics().density;
        int circleSize = (int) (circleSizeInDp * scale + 0.5f);
        int circleMargin = (int) (circleMarginInDp * scale + 0.5f);
        int circleElevation = (int) (circleElevationInDp * scale + 0.5f);

        // updates the view
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(circleSize, circleSize);
        layoutParams.addRule(RelativeLayout.ALIGN_START, friend_circle_back_id);
        layoutParams.addRule(RelativeLayout.ALIGN_TOP, friend_circle_back_id);
        layoutParams.addRule(RelativeLayout.ALIGN_END, friend_circle_back_id);
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, friend_circle_back_id);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        layoutParams.setMargins(circleMargin, circleMargin, circleMargin, circleMargin);
        circleView.setLayoutParams(layoutParams);
        circleView.setElevation(circleElevation);
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

        // Sets the desired attributes for the view
        int ElevationInDp = 10;

        // Converts dp to pixels
        float scale = getResources().getDisplayMetrics().density;
        int Elevation = (int) (ElevationInDp * scale + 0.5f);

        // updates the view
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_START, friend_circle_back_id);
        layoutParams.addRule(RelativeLayout.ALIGN_TOP, friend_circle_back_id);
        layoutParams.addRule(RelativeLayout.ALIGN_END, friend_circle_back_id);
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, friend_circle_back_id);
        int textColor = ContextCompat.getColor(getContext(), R.color.black);
        letterView.setTextColor(textColor);
        letterView.setText(String.valueOf(letter));
        letterView.setGravity(Gravity.CENTER);
        letterView.setLayoutParams(layoutParams);
        letterView.setElevation(Elevation);

        // Adds the view to the RelativeLayout
        relativeLayout.addView(letterView);
    }
}