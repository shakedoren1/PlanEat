package com.example.planeatapp;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of this fragment.
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // attaching the buttons
        AppCompatButton guestListButton = view.findViewById(R.id.guest_list_button);
        AppCompatButton groupTasksButton = view.findViewById(R.id.group_tasks_button);

        // sets the guestListButton
        String confirmedTitle = guestListButton.getText().toString();
        guestListButton.setOnClickListener(v ->
                ((MainPageActivity) requireActivity()).replaceFragmentInMainPage(
                        new ListFragment("Guest list", new GuestListFragment(confirmedTitle))));

        // sets the groupTasksButton
        groupTasksButton.setOnClickListener(v ->
                ((MainPageActivity) requireActivity()).replaceFragmentInMainPage(
                        new ListFragment("Group task list", new GroupTaskListFragment())));

        return view;
    }
}