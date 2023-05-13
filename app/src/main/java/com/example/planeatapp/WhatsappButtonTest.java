package com.example.planeatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.Manifest;

public class WhatsappButtonTest extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsapp_button_test);

        Button btn = findViewById(R.id.whatsapp_export);

        String num = "+972544949953";
        String text = "Test";

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean installed = isAppInstalled();
                if (installed) {
                    if (ContextCompat.checkSelfPermission(WhatsappButtonTest.this, Manifest.permission.SEND_SMS)
                            == PackageManager.PERMISSION_GRANTED) {
                        // Permission has already been granted
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+num+"&text="+text));
                        startActivity(intent);
                    } else {
                        // Permission has not been granted, request it
                        ActivityCompat.requestPermissions(WhatsappButtonTest.this,
                                new String[]{Manifest.permission.SEND_SMS},
                                MY_PERMISSIONS_REQUEST_SEND_SMS);
                    }
                }
                else {
                    Toast.makeText(WhatsappButtonTest.this, "Whatsapp is not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private boolean isAppInstalled() {
        PackageManager packageManager = getPackageManager();
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
}