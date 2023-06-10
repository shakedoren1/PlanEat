package com.example.planeatapp;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    private String BASE_URL = "http://10.0.2.2:8080"; // replace this with your server's URL

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

        // Initialize retrofit and retrofitInterface here
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_task_list, container, false);

        // Call updateIngredientListInfo here
        updateIngredientListInfo(mParam1); // assuming mParam1 is the ID for the ingredient list

        return view;
    }

    private void updateIngredientListInfo(String id) {
        Log.e("Update Ingredient List Info", "passed: " + id );

        Call<ListDetails> call = retrofitInterface.executeListInfo(id);

        call.enqueue(new Callback<ListDetails>() {
            @Override
            public void onResponse(Call<ListDetails> call, Response<ListDetails> response) {
                if (response.isSuccessful()) {
                    ListDetails ingredientList = response.body();
                    if (ingredientList != null) {
                        // updating the fragment with the ingredient list received from the database
                        // TODO: Update your UI here with the retrieved ingredient list
                    }
                } else {
                    Log.e("Update Ingredient List Info", "Problem with retrieving ingredient list" + response.message());
                }
            }

            @Override
            public void onFailure(Call<ListDetails> call, Throwable t) {
                Log.e("Update Ingredient List Info", "Failed to retrieve ingredient list" + t.getMessage());
            }
        });
    }
}