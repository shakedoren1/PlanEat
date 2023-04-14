package com.example.planeatapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import java.util.Calendar;
import java.util.Locale;

public class DateTimeFragment extends Fragment {

    private ImageButton backButton;
    private Button nextButton;
    private EditText dateEditText, timeEditText;

    public DateTimeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_date_time, container, false);

        // Find the views by their ids
        backButton = view.findViewById(R.id.back_button);
        nextButton = view.findViewById(R.id.next_button);
        dateEditText = view.findViewById(R.id.date_edit_text);
        timeEditText = view.findViewById(R.id.time_edit_text);

        // Set the click listener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the previous fragment
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Set the click listener for the next button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to the next fragment
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new ConceptFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        // Set the date picker to show a dd/mm/yyyy format
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a calendar instance
                final Calendar calendar = Calendar.getInstance();

                // Create a date picker dialog with the current date as the default
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        // Set the selected date to the date edit text in the dd/mm/yyyy format
                        dateEditText.setText(String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                // Show the date picker dialog
                datePickerDialog.show();
            }
        });

        // Set the time picker to show the time in the hh:mm format
        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a calendar instance
                final Calendar calendar = Calendar.getInstance();

                // Create a time picker dialog with the current time as the default
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        // Set the selected time to the time edit text in the hh:mm format
                        timeEditText.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

                // Show the time picker dialog
                timePickerDialog.show();
            }
        });

        return view;
    }
}