package com.example.planeatapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateEvent extends AppCompatActivity {

    private EditText descriptionField;
    private EditText whenField;
    private EditText timeField;
    private EditText placeField;
    private EditText conceptField;
    private EditText participantsField;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    // Server variables:
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:8080";

    private String eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        // Server setup
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        descriptionField = findViewById(R.id.description_field);
        whenField = findViewById(R.id.when_field);
        timeField = findViewById(R.id.time_field);
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
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = descriptionField.getText().toString();
                String when = whenField.getText().toString();
                String time = timeField.getText().toString();
                String place = placeField.getText().toString();
                String concept = conceptField.getText().toString();
                String number = participantsField.getText().toString();

                insertEventToDatabase(description, when, time, place, concept, number);

                InvitePopup invitePopup = InvitePopup.newInstance(description, when, time, place, concept);
                invitePopup.show(getSupportFragmentManager(), "invite_popup");
            }
        });
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
    private void insertEventToDatabase(String description, String when, String time, String place, String concept, String number) {

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
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful()) {
                    Map<String, String> responseData = response.body();
                    if (responseData != null && responseData.containsKey("insertedId")) {
                        // Assign the inserted ID to the eventID variable
                        eventID = responseData.get("insertedId");
                        Toast.makeText(CreateEvent.this, eventID, Toast.LENGTH_SHORT).show();
                        Log.e("Insert Event", "Event created successfully");
                    } else {
                        Toast.makeText(CreateEvent.this, "Failed to get inserted ID", Toast.LENGTH_SHORT).show();
                        Log.e("Insert Event", "Failed to get inserted ID");
                    }
                } else {
                    Toast.makeText(CreateEvent.this, "Failed to insert event", Toast.LENGTH_SHORT).show();
                    Log.e("Insert Event", "Event insertion failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Toast.makeText(CreateEvent.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Insert Event", "Event insertion failed: " + t.getMessage());
            }
        });
    }
}