package com.example.planeatapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ConceptFragment extends Fragment {

    private OnConceptEnteredListener mListener;

    public interface OnConceptEnteredListener {
        void onConceptEntered(String concept);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_concept, container, false);

        Button nextButton = view.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to the next fragment
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.amount_fragment_container, new AmountFragment());
                transaction.addToBackStack(null);
                transaction.commit();

                // Hide views
                nextButton.setVisibility(View.GONE);
                getView().findViewById(R.id.bbq_button).setVisibility(View.GONE);
                getView().findViewById(R.id.homemade_button).setVisibility(View.GONE);
                getView().findViewById(R.id.picnic_button).setVisibility(View.GONE);
                getView().findViewById(R.id.italian_button).setVisibility(View.GONE);
                getView().findViewById(R.id.other_button).setVisibility(View.GONE);
                getView().findViewById(R.id.concept_title).setVisibility(View.GONE);
                getView().findViewById(R.id.next_button).setVisibility(View.GONE);
            }
        });

        Button bbqButton = view.findViewById(R.id.bbq_button);
        Button homemadeButton = view.findViewById(R.id.homemade_button);
        Button picnicButton = view.findViewById(R.id.picnic_button);
        Button italianButton = view.findViewById(R.id.italian_button);
        Button otherButton = view.findViewById(R.id.other_button);

        bbqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onConceptEntered("BBQ");
            }
        });

        homemadeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onConceptEntered("Homemade Meal");
            }
        });

        picnicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onConceptEntered("Picnic");
            }
        });

        italianButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onConceptEntered("Italian");
            }
        });

        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onConceptEntered("Other");
            }
        });

        return view;
    }

    public void setOnConceptEnteredListener(OnConceptEnteredListener listener) {
        mListener = listener;
    }
}