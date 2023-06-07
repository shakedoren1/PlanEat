package com.example.planeatapp;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupTaskListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupTaskListFragment extends Fragment implements MainPageActivity.GPTResponseListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GroupTaskListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupTaskListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupTaskListFragment newInstance(String param1, String param2) {
        GroupTaskListFragment fragment = new GroupTaskListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onGPTResponse(String response) {
        // Handle the response here
    }

    private void updateEventInfo(String id) {
        Log.e("Update Event Info", "passed: " + id );

        Call<EventDetails> call = retrofitInterface.executeEventInfo(id);

        call.enqueue(new Callback<EventDetails>() {
            @Override
            public void onResponse(Call<EventDetails> call, Response<EventDetails> response) {
                if (response.isSuccessful()) {
                    EventDetails eventInfo = response.body();
                    if (eventInfo != null) {
                        // updating the home fragment with all the details received from the database
                        String content = eventInfo.getConcept();
                        updateText("Ingredient List", content);
                    }
                } else {
                    Log.e("Update List", "Problem retrieving list from DB" + response.message());
                }
            }


            @Override
            public void onFailure(Call<EventDetails> call, Throwable t) {
                Log.e("Update Event Info", "Failed to retrieve event info" + t.getMessage());
            }
        });
    }

    private void updateText(String name, String text) {
        // putting all the relevant text_views into a hashMap
        HashMap<String, Integer> textMap = new HashMap<>();
        textMap.put("item", R.id.item_list);

        // putting the text received inside the relevant text_view
        if (textMap.containsKey(name)) {
            int nameId = textMap.get(name);
            TextView textView = getView().findViewById(nameId);
            textView.setText(text);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainPageActivity) {
            ((MainPageActivity) context).setGPTResponseListener(this);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_task_list, container, false);
    }
}