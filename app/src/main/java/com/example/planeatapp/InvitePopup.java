package com.example.planeatapp;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

public class InvitePopup extends DialogFragment {

    private static final String DESCRIPTION_KEY = "description_key";
    private static final String WHEN_KEY = "when_key";
    private static final String TIME_KEY = "time_key";
    private static final String PLACE_KEY = "place_key";
    private static final String CONCEPT_KEY = "concept_key";

    private String description;
    private String when;
    private String time;
    private String place;
    private String concept;

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    public static InvitePopup newInstance(String description, String when, String time, String place, String concept) {
        InvitePopup invitePopup = new InvitePopup();
        Bundle args = new Bundle();
        args.putString(DESCRIPTION_KEY, description);
        args.putString(WHEN_KEY, when);
        args.putString(TIME_KEY, time);
        args.putString(PLACE_KEY, place);
        args.putString(CONCEPT_KEY, concept);
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
        }
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_invite_popup, null);
        String message = "Hey friends! " +
                "You're invited to join " + description + " and celebrate! " +
                "The theme is " + concept + " on " + when + " at " + time + "! " +
                "Hope to see you at " + place + "! " +
                "To RSVP, click below!";
        String num = "972544949953";
        TextView messageTextView = view.findViewById(R.id.message_textview);
        messageTextView.setText(message);
        AppCompatButton sendButton = view.findViewById(R.id.whatsapp_export);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isWhatsAppInstalled = isAppInstalled();
                if (isWhatsAppInstalled) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                    sendIntent.setType("text/plain");
                    sendIntent.setPackage("com.whatsapp");
                    PackageManager packageManager = getContext().getPackageManager();
                    if (sendIntent.resolveActivity(packageManager) != null) {
                        startActivity(sendIntent);
                    } else {
                        Toast.makeText(getContext(), "WhatsApp not installed.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "WhatsApp not installed.", Toast.LENGTH_SHORT).show();
                }
            }

            private boolean isAppInstalled() {
                PackageManager packageManager = getContext().getPackageManager();
                boolean is_installed;
                try {
                    packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    is_installed = true;
                } catch (PackageManager.NameNotFoundException e) {
                    is_installed = false;
                    e.printStackTrace();
                }
                return is_installed;
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view).setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getContext(), MainPageActivity.class);
                startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button skipButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                if (skipButton != null) {
                    skipButton.setX((dialog.getWindow().getDecorView().getWidth() - skipButton.getWidth()) / 2);
                }
            }
        });
        return dialog;
    }
}