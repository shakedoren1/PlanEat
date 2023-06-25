package com.example.planeatapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IngredientListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IngredientListFragment extends Fragment implements MainPageActivity.GPTResponseListener {

    private static String eventID;
    private RetrofitInterface retrofitInterface;
    private TextView itemListTextView;
    private Drawable checkboxIcon;

    public IngredientListFragment() {
        // Required empty public constructor
    }

    /**
     * A constructor that puts inside eventID the current event ID.
     */
    public IngredientListFragment(String ID) {
        eventID = ID;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GroupTaskListFragment.
     */
    public static IngredientListFragment newInstance() {
        IngredientListFragment fragment = new IngredientListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onGPTResponse(String response) {
        // Handle the response here
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainPageActivity) {
            ((MainPageActivity) context).setGPTResponseListener(this);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize retrofit and retrofitInterface here
        // replace this with your server's URL
        String BASE_URL = "http://websiteserver.shakedoren1.repl.co";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        if (getArguments() != null) {
            String listID = getArguments().getString("listID");
            Log.e("Insert List ID", "TaskList List ID: " + listID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ingredient_list, container, false);

        // Find the TextView
        itemListTextView = view.findViewById(R.id.item_list);
        // Removed the line that sets the text to "listID"

        // Initialize the icon drawable here, where the context is available
        checkboxIcon = ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.flatware);
        if (checkboxIcon != null) {
            checkboxIcon.setBounds(0, 0, checkboxIcon.getIntrinsicWidth(), checkboxIcon.getIntrinsicHeight());
        }
        // Call updateIngredientListInfo here
        updateIngredientListInfo(eventID);

        return view;
    }

    private void updateIngredientListInfo(String id) {
        Log.e("Update Ingredient List Info", "passed: " + id );

        Call<IngredientList> call = retrofitInterface.executeListByEventId(id);

        call.enqueue(new Callback<IngredientList>() {
            @Override
            public void onResponse(@NonNull Call<IngredientList> call, @NonNull Response<IngredientList> response) {
                Log.e("onResponse eventID", "passed: " + eventID );
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
            public void onFailure(@NonNull Call<IngredientList> call, @NonNull Throwable t) {
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
//            categorySB.setSpan(new StyleSpan(Typeface.BOLD), 0, categorySB.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            itemListTextView.append(categorySB);

            // Iterate over the received potluck items and append them to the TextView
            for (PotluckItem item : items) {
                // Create a SpannableStringBuilder for the item
                SpannableStringBuilder itemSB = new SpannableStringBuilder(" " + item.getAmount() + " " + item.getItem() + "\n");
                // Set the icon to the left of the text (at the start of the Spannable)
                if (checkboxIcon != null) {
                    itemSB.setSpan(new ImageSpan(checkboxIcon, ImageSpan.ALIGN_BASELINE), 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
                itemListTextView.append(itemSB);
            }

            // Add a newline for spacing between categories
            itemListTextView.append("\n");
        } else {
            Log.e("Update Ingredient List Info", category + " is null");
        }
    }
}