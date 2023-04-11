package com.example.planeatapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
public class DateTimeFragment extends Fragment {

    private DatePicker mDatePicker;
    private TimePicker mTimePicker;

    private OnDateTimeEnteredListener mListener;

    public interface OnDateTimeEnteredListener {
        void onDateTimeEntered(String date, String time);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date_time, container, false);

        mDatePicker = view.findViewById(R.id.date_picker);
        mTimePicker = view.findViewById(R.id.time_picker);

        Button nextButton = view.findViewById(R.id.next_button);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new ConceptFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}
