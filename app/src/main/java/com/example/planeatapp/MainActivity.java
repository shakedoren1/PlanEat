package com.example.planeatapp;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity
        implements DateTimeFragment.OnDateTimeEnteredListener,
        ConceptFragment.OnConceptEnteredListener,
        AmountFragment.OnAmountEnteredListener {

    private String mDate;
    private String mTime;
    private String mConcept;
    private int mAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if event was created already
        if (mDate != null && mTime != null && mConcept != null && mAmount != 0) {
            displaySummaryFragment();
        } else {
            displayDateTimeFragment();
        }
    }

    private void displayDateTimeFragment() {
        DateTimeFragment fragment = new DateTimeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    private void displayConceptFragment() {
        ConceptFragment fragment = new ConceptFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void displayAmountFragment() {
        AmountFragment fragment = new AmountFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void displaySummaryFragment() {
        SummaryFragment fragment = SummaryFragment.newInstance(mDate, mTime, mConcept, mAmount);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    public void onDateTimeEntered(String date, String time) {
        mDate = date;
        mTime = time;
        displayConceptFragment();
    }

    @Override
    public void onConceptEntered(String concept) {
        mConcept = concept;
        displayAmountFragment();
    }

    @Override
    public void onAmountEntered(int amount) {
        mAmount = amount;
        displaySummaryFragment();
    }
}