package com.example.planeatapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;

import android.os.Handler;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class CreateEvent extends AppCompatActivity {
    private EditText descriptionField, whenField, timeField, placeField, conceptField, participantsField;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private ProgressBar loadingProgress;
    private RetrofitInterface retrofitInterface; // Server interface
    private String eventID, listID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        loadingProgress = findViewById(R.id.loading_progress);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES) // connect timeout
                .readTimeout(2, TimeUnit.MINUTES)    // socket timeout
                .writeTimeout(2, TimeUnit.MINUTES)   // write timeout
                .build();

        // Server setup
        String BASE_URL = "https://8cd39e75-ae9e-4a95-9ae7-d31da3478fc8-00-2a6lif9mxtnmr.riker.replit.dev";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient) // set the custom OkHttpClient
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        descriptionField = findViewById(R.id.description_field);
        whenField = findViewById(R.id.when_field);
        whenField.setFocusable(false);
        timeField = findViewById(R.id.time_field);
        timeField.setFocusable(false);
        placeField = findViewById(R.id.place_field);
        conceptField = findViewById(R.id.concept_field);
        participantsField = findViewById(R.id.participants_field);

        // Set up calendar for date and time pickers
        calendar = Calendar.getInstance();

        // Set up date picker dialog
        datePickerDialog = new DatePickerDialog(this,
                (view1, year, monthOfYear, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateWhenField();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        // Set up time picker dialog
        timePickerDialog = new TimePickerDialog(this,
                (view12, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    updateTimeField();
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(this));

        // Set up click listeners for when and time fields
        whenField.setOnClickListener(v -> datePickerDialog.show());
        timeField.setOnClickListener(v -> timePickerDialog.show());

        Button createEventButton = findViewById(R.id.create_event_button);
        createEventButton.setOnClickListener(v -> {
            // showing the progress bar
            loadingProgress.setVisibility(View.VISIBLE);

            String description = descriptionField.getText().toString();
            String when = whenField.getText().toString();
            String time = timeField.getText().toString();
            String place = placeField.getText().toString();
            String concept = conceptField.getText().toString();
            String number = participantsField.getText().toString();

            // Check if concept and number fields are empty
            if (concept.isEmpty() || number.isEmpty()) {
                // Show popup for missing info
                showMissingInfoPopup();
            } else {

                insertEventToDatabase(description, when, time, place, concept, number, new InsertEventCallback() {
                    @Override
                    public void onEventInserted(String eventId) {
                        // Lines to be executed after event insertion succeeded

                        // Now attempt to create the list
                        Handler handler = new Handler();
                        handler.post(() -> insertListToDatabase(concept, number, eventID, new InsertListCallback() {
                            @Override
                            public void onListInserted(String listId) {
                                // If list is successfully created, show the popup
                                // dismiss the progress bar here if you don't want to show it until the list is also created
                                loadingProgress.setVisibility(View.GONE);

                                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                                    InvitePopup invitePopup = InvitePopup.newInstance(description, when, time, place, concept, eventId, listId);
                                    invitePopup.show(getSupportFragmentManager(), "invite_popup");
                                }
                            }
                            @Override
                            public void onListInsertionFailed(String errorMessage) {
                                // Lines to be executed if list insertion failed
                                Toast.makeText(CreateEvent.this, "Failed to insert list: " + errorMessage, Toast.LENGTH_SHORT).show();
                                Log.e("Insert List", "List insertion failed: " + errorMessage);
                            }
                        }));

                        // Show the InvitePopup immediately after event insertion, without waiting for list creation
                        InvitePopup invitePopup = InvitePopup.newInstance(description, when, time, place, concept, eventId, "");
                        invitePopup.show(getSupportFragmentManager(), "invite_popup");
                    }

                    @Override
                    public void onEventInsertionFailed(String errorMessage) {
                        // Lines to be executed if event insertion failed
                        Toast.makeText(CreateEvent.this, "Failed to insert event: " + errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("Insert Event", "Event insertion failed: " + errorMessage);
                    }
                });
            }
        });
    }

    private void showMissingInfoPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Missing Info")
                .setMessage(Html.fromHtml("We're missing some important info about your event. Please make sure you tell us what the <b>concept</b> is and <b>how many people</b> are invited. Thank you! \uD83D\uDE0A"))
                .setPositiveButton("OK", null)
                .show();
    }

    private void updateWhenField() {
        whenField.setText(DateFormat.format("MMM dd, yyyy", calendar));
    }

    private void updateTimeField() {
        timeField.setText(DateFormat.format("h:mm a", calendar));
    }

    /**
     * A POST request to server endpoint to insert the event to the database.
     */
    private void insertEventToDatabase(String description, String when, String time, String place, String concept, String number, final InsertEventCallback callback) {

        HashMap <String, String> eventDetails = new HashMap<>();

        eventDetails.put("title", description);
        eventDetails.put("date", when);
        eventDetails.put("time", time);
        eventDetails.put("place", place);
        eventDetails.put("concept", concept);
        eventDetails.put("number", number);

        Call<Map<String, String>> call = retrofitInterface.executeNewEvent(eventDetails);

        call.enqueue(new Callback<Map<String, String>>() {
            @Override

            public void onResponse(@NonNull Call<Map<String, String>> call, @NonNull Response<Map<String, String>> response) {
                if (response.isSuccessful()) {
                    Map<String, String> responseData = response.body();
                    Log.d("Response", "Response data: " + responseData); // Log entire response data
                    if (responseData != null && responseData.containsKey("insertedId")) {
                        // Assign the inserted ID to the eventID variable
                        eventID = responseData.get("insertedId");
//                        Toast.makeText(CreateEvent.this, "Event created successfully", Toast.LENGTH_SHORT).show();
                        Log.e("Insert Event", "Event created successfully, ID: " + eventID);

                        callback.onEventInserted(eventID); // Invoke the callback

                    } else {
                        Toast.makeText(CreateEvent.this, "Failed to get inserted ID", Toast.LENGTH_SHORT).show();
                        Log.e("Insert Event", "Failed to get inserted ID");

                        callback.onEventInsertionFailed(response.message()); // Invoke the callback
                    }

                } else {
                    Toast.makeText(CreateEvent.this, "Failed to insert event", Toast.LENGTH_SHORT).show();
                    Log.e("Insert Event", "Event insertion failed: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, String>> call, @NonNull Throwable t) {
                Toast.makeText(CreateEvent.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Insert Event", "Event insertion failed: " + t.getMessage());
            }
        });
    }

    private void insertListToDatabase(String concept, String number, String eventID, final InsertListCallback callback) {

        HashMap <String, String> listDetails = new HashMap<>();

        listDetails.put("concept", concept);
        listDetails.put("number", number);
        listDetails.put("eventID", eventID);

        Call<Map<String, String>> call = retrofitInterface.executePrompt(listDetails);

        call.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, String>> call, @NonNull Response<Map<String, String>> response) {
                if (response.isSuccessful()) {
                    Map<String, String> responseData = response.body();
                    if (responseData != null && responseData.containsKey("insertedId")) {
                        // Assign the inserted ID to the listID variable
                        listID = responseData.get("insertedId");
//                        Toast.makeText(CreateEvent.this, "List created successfully", Toast.LENGTH_SHORT).show();
                        Log.e("Insert List", "List created successfully, ID: " + listID);

                        callback.onListInserted(listID); // Invoke the callback

                    } else {
                        Toast.makeText(CreateEvent.this, "Failed to get inserted ID", Toast.LENGTH_SHORT).show();
                        Log.e("Insert List", "Failed to get inserted ID");

                        callback.onListInsertionFailed(response.message()); // Invoke the callback
                    }

                } else {
                    Toast.makeText(CreateEvent.this, "Failed to insert list", Toast.LENGTH_SHORT).show();
                    Log.e("Insert List", "List insertion failed: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, String>> call, @NonNull Throwable t) {
                Toast.makeText(CreateEvent.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Insert List", "List insertion failed: " + t.getMessage());
            }
        });
    }

    interface InsertEventCallback {
        void onEventInserted(String eventId);
        void onEventInsertionFailed(String errorMessage);
    }

    interface InsertListCallback {
        void onListInserted(String listId);
        void onListInsertionFailed(String errorMessage);
    }
}