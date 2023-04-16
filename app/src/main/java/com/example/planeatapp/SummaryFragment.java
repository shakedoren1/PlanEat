package com.example.planeatapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SummaryFragment extends Fragment {

    private static final String ARG_DATE = "date";
    private static final String ARG_TIME = "time";
    private static final String ARG_CONCEPT = "concept";
    private static final String ARG_AMOUNT = "amount";

    private String mDate;
    private String mTime;
    private String mConcept;
    private int mAmount;

    public SummaryFragment() {
        // Required empty public constructor
    }

    public static SummaryFragment newInstance(String date, String time, String concept, int amount) {
        SummaryFragment fragment = new SummaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DATE, date);
        args.putString(ARG_TIME, time);
        args.putString(ARG_CONCEPT, concept);
        args.putInt(ARG_AMOUNT, amount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDate = getArguments().getString(ARG_DATE);
            mTime = getArguments().getString(ARG_TIME);
            mConcept = getArguments().getString(ARG_CONCEPT);
            mAmount = getArguments().getInt(ARG_AMOUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        TextView dateTextView = view.findViewById(R.id.dateTextView);
        dateTextView.setText(mDate);

        TextView timeTextView = view.findViewById(R.id.timeTextView);
        timeTextView.setText(mTime);

        TextView conceptTextView = view.findViewById(R.id.conceptTextView);
        conceptTextView.setText(mConcept);

        TextView amountTextView = view.findViewById(R.id.amountTextView);
        amountTextView.setText(String.valueOf(mAmount));

        return view;
    }

}