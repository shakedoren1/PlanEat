package com.example.planeatapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {

    private String listTitleText; // the title of the list
    private Fragment insideFragment; // the inside fragment to load

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * A constructor that puts inside listTitleText the title of the list,
     * and inside insideFragment the fragment to load.
     */
    public ListFragment(String title, Fragment fragment) {
        listTitleText = title;
        insideFragment = fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ListFragment.
     */
    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        // Set the title
        TextView listTitleTextView = view.findViewById(R.id.list_title);
        listTitleTextView.setText(listTitleText);

        // the back button
        ImageButton backButton = view.findViewById(R.id.back_button);
        // sets the backButton
        backButton.setOnClickListener(v -> ((MainPageActivity) requireActivity()).replaceFragmentInMainPage(new HomeFragment()));
        return view;
    }
}