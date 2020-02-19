package com.example.mirakram.banmiok;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class SendActivity extends AppCompatActivity {
    private EditText editTextSubject;
    private EditText editTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        AdView mAdView;
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        Button but=(Button) findViewById(R.id.send);
        editTextSubject = (EditText) findViewById(R.id.Subject);
        editTextMessage = (EditText) findViewById(R.id.Content);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = editTextSubject.getText().toString().trim();
                String message = editTextMessage.getText().toString().trim();
                sendEmail(subject, message);
            }
        });

    }
    protected void sendEmail(String sub, String mess) {
        Log.i("Send email", "");

        String[] TO = {"banmiokapp@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, sub);
        emailIntent.putExtra(Intent.EXTRA_TEXT, mess);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(SendActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

}
