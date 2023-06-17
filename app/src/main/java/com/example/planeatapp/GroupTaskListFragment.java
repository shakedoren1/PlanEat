package com.example.planeatapp;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

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

    private static String listID = "";
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://websiteserver.shakedoren1.repl.co"; // replace this with your server's URL
    private TextView itemListTextView;

    public GroupTaskListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GroupTaskListFragment.
     */
    public static GroupTaskListFragment newInstance(String param1, String param2) {
        GroupTaskListFragment fragment = new GroupTaskListFragment();
        Bundle args = new Bundle();
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
            listID = getArguments().getString("listID");
            Log.e("Insert List ID", "TaskList List ID: " + listID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_task_list, container, false);

        // Find the TextView
        itemListTextView = view.findViewById(R.id.item_list);
        // Removed the line that sets the text to "listID"

        // Call updateIngredientListInfo here
        updateIngredientListInfo(listID);

        return view;
    }

    private void updateIngredientListInfo(String id) {
        Log.e("Update Ingredient List Info", "passed: " + id );

        Call<IngredientList> call = retrofitInterface.executeListInfo(id);

        call.enqueue(new Callback<IngredientList>() {
            @Override
            public void onResponse(Call<IngredientList> call, Response<IngredientList> response) {
                if (response.isSuccessful()) {
                    IngredientList ingredientList = response.body();
                    if (ingredientList != null && itemListTextView != null) {
                        itemListTextView.setText(""); // Clear the TextView
                        appendItemsToTextView("Appetizers", ingredientList.getAppetizers());
                        appendItemsToTextView("Mains", ingredientList.getMains());
                        appendItemsToTextView("Sides", ingredientList.getSides());
                        appendItemsToTextView("Dessert", ingredientList.getDessert());
                        appendItemsToTextView("Drinks", ingredientList.getDrinks());
                        appendItemsToTextView("Other", ingredientList.getOther());
                    }
                } else {
                    Log.e("Update Ingredient List Info", "Problem with retrieving ingredient list: " + response.message());
                    if (itemListTextView != null) {
                        itemListTextView.setText("Oops there's no list yet!");
                    }
                }
            }

            @Override
            public void onFailure(Call<IngredientList> call, Throwable t) {
                Log.e("Update Ingredient List Info", "Failed to retrieve ingredient list" + t.getMessage());
                if (itemListTextView != null) {
                    itemListTextView.setText("Oops there's no list yet!");
                }
            }
        });
    }

    private void appendItemsToTextView(String category, List<PotluckItem> items) {
        if (items != null) {
            // Create a new SpannableStringBuilder, apply the bold style to it, and append it to the TextView
            SpannableStringBuilder categorySB = new SpannableStringBuilder(category + ":\n");
            categorySB.setSpan(new StyleSpan(Typeface.BOLD), 0, categorySB.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            itemListTextView.append(categorySB);

            // Iterate over the received potluck items and append them to the TextView
            for (PotluckItem item : items) {
                itemListTextView.append(item.getAmount() + " " + item.getItem() + "\n");
            }

            // Add a newline for spacing between categories
            itemListTextView.append("\n");
        } else {
            Log.e("Update Ingredient List Info", category + " is null");
        }
    }
}