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
 * Use the {@link GuestListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuestListFragment extends Fragment {

    private String GuestListText; // the title of the guest list

    public GuestListFragment() {
        // Required empty public constructor
    }

    /**
     * A constructor that puts inside the Guest_List the updated confirmed number from the home page.
     */
    public GuestListFragment(String confirmed) {
        GuestListText = confirmed;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GuestListFragment.
     */
    public static GuestListFragment newInstance(String param1, String param2) {
        GuestListFragment fragment = new GuestListFragment();
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
        View view = inflater.inflate(R.layout.fragment_guest_list, container, false);
        // Set the title
        TextView listTitleTextView = view.findViewById(R.id.guest_list);
        listTitleTextView.setText(GuestListText);
        return view;
    }
}