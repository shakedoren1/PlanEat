package com.example.planeatapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

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
}