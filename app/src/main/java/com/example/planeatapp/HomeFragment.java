package com.example.planeatapp;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
    private boolean firstBuilt = true; // mark build
    private int circles = 0; // number of confirmations drawn
    // Server variables:
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    // for emulator
//    private String BASE_URL = "http://10.0.2.2:8080";
    // based on WIFI IP
    private String BASE_URL = "http://websiteserver.shakedoren1.repl.co";
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * A constructor that puts inside the eventID the current ID of the event in the data base.
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

        // Server setup
        retrofit = new Retrofit.Builder()
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
        // attaching the buttons
        AppCompatButton guestListButton = view.findViewById(R.id.guest_list_button);
        AppCompatButton groupTasksButton = view.findViewById(R.id.group_tasks_button);

        // sets the guestListButton
        String confirmedTitle = guestListButton.getText().toString();
        guestListButton.setOnClickListener(v ->
                ((MainPageActivity) requireActivity()).replaceFragmentInMainPage(
                        new ListFragment("Friends list", new GuestListFragment())));

        // sets the groupTasksButton
        groupTasksButton.setOnClickListener(v ->
                ((MainPageActivity) requireActivity()).replaceFragmentInMainPage(
                        new ListFragment("Group task list", new GroupTaskListFragment())));

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
                        updateText("title", title);
                        String date = eventInfo.getDate();
                        updateText("date", date);
                        String time = eventInfo.getTime();
                        updateText("time", time);
                        String place = eventInfo.getPlace();
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
                            String name = confirmation.getName();
                            String option = confirmation.getOption();
                            // Convert the first character of name to uppercase
                            char firstLetter = Character.toUpperCase(name.charAt(0));
                            // Call the drawCircle method with the uppercase first letter and option
                            drawCircle(firstLetter, option);
                        }
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
        if (circles == 0) {
            drawFirstCircle(letter, option);
        } else {
            //implement
        }
    }

    /**
     * A method to draw the first circle
     */
    private void drawFirstCircle(char letter, String option) {
        // implement
    }
}