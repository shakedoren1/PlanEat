package com.example.planeatapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class InvitePopup extends DialogFragment {
    private static final String DESCRIPTION_KEY = "description_key", WHEN_KEY = "when_key",
            TIME_KEY = "time_key", PLACE_KEY = "place_key", CONCEPT_KEY = "concept_key",
            EVENT_KEY = "event_key", LIST_KEY = "list_key";
    private String description, when, time, place, concept, eventID, listID;

    public static InvitePopup newInstance(String description, String when, String time, String place, String concept, String ID, String listID) {
        InvitePopup invitePopup = new InvitePopup();
        Bundle args = new Bundle();
        args.putString(DESCRIPTION_KEY, description);
        args.putString(WHEN_KEY, when);
        args.putString(TIME_KEY, time);
        args.putString(PLACE_KEY, place);
        args.putString(CONCEPT_KEY, concept);
        args.putString(EVENT_KEY, ID);
        args.putString(LIST_KEY, listID);
        invitePopup.setArguments(args);
        return invitePopup;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            description = arguments.getString(DESCRIPTION_KEY);
            when = arguments.getString(WHEN_KEY);
            time = arguments.getString(TIME_KEY);
            place = arguments.getString(PLACE_KEY);
            concept = arguments.getString(CONCEPT_KEY);
            eventID = arguments.getString(EVENT_KEY);
            listID =  arguments.getString(LIST_KEY);
        }
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_invite_popup, null);
        String message = "You're invited to join " + description + " and celebrate! " +
                "The theme is " + concept + " on " + when + " at " + time + "! " + "Hope to see you at "
                + place + "! " + "To RSVP, click below! \n https://planeat-website.shakedoren1.repl.co/?eventID=" + eventID;
        String forWhatsapp = "*Hey friends!* \uD83D\uDE04 \n You're invited to join " + description + " and celebrate! " +
                "The theme is " + concept + " on " + when + " at " + time + "! " + "Hope to see you at "
                + place + "! " + "To RSVP, click below! \n https://planeat-website.shakedoren1.repl.co/?eventID=" + eventID
                + "\n *See you there!* \uD83E\uDD73";
        TextView messageTextView = view.findViewById(R.id.message_textview);
        messageTextView.setText(message);
        AppCompatButton sendButton = view.findViewById(R.id.whatsapp_export);
        sendButton.setOnClickListener(view1 -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, forWhatsapp);
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.whatsapp");

            runMain();

            //Start whatsapp
            startActivity(sendIntent);
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setView(view).setNegativeButton("Later", (dialogInterface, i) -> runMain());
        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(dialogInterface -> {
            Button skipButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            if (skipButton != null) {
                skipButton.setX((dialog.getWindow().getDecorView().getWidth() - skipButton.getWidth()) / 2);
            }
        });
        return dialog;
    }

    /**
     * A method to run the MainPageActivity with eventID and listID sent
     */
    private void runMain() {
        Intent intent = new Intent(getContext(), MainPageActivity.class);
        intent.putExtra("event_id", eventID); // sending the event id to the main page activity
        intent.putExtra("list_id", listID); // sending the list id to the main page activity
        Log.e("Insert List ID", "Popup listID: " + listID);
        startActivity(intent);
    }
}