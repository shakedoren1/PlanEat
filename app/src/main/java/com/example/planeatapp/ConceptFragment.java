package com.example.planeatapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
public class ConceptFragment extends Fragment {

    private EditText mConceptEditText;

    private OnConceptEnteredListener mListener;

    public interface OnConceptEnteredListener {
        void onConceptEntered(String concept);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_concept, container, false);

        mConceptEditText = view.findViewById(R.id.concept_edit_text);

        Button nextButton = view.findViewById(R.id.next_button);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new AmountFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}