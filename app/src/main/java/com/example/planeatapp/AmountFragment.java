package com.example.planeatapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

public class AmountFragment extends Fragment {

    private NumberPicker mAmountPicker;

    private OnAmountEnteredListener mListener;

    public interface OnAmountEnteredListener {
        void onAmountEntered(int amount);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_amount, container, false);

        mAmountPicker = view.findViewById(R.id.amount_picker);
        mAmountPicker.setMinValue(8);
        mAmountPicker.setMaxValue(35);
        mAmountPicker.setValue(8);

        Button nextButton = view.findViewById(R.id.next_button);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.summary_fragment_container, new SummaryFragment());
                transaction.addToBackStack(null);
                transaction.commit();

                // Hide views
                nextButton.setVisibility(View.GONE);
                getView().findViewById(R.id.amount_title).setVisibility(View.GONE);
                getView().findViewById(R.id.amount_picker).setVisibility(View.GONE);
                getView().findViewById(R.id.next_button).setVisibility(View.GONE);
            }

        });

        return view;
    }
}