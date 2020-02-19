package com.example.mirakram.banmiok;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class ResultActivity extends AppCompatActivity {
    private InterstitialAd minter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result2);

        minter=new InterstitialAd(this);
        minter.setAdUnitId("ca-app-pub-8584547019777134/3536051147");
        AdRequest request=new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        minter.loadAd(request);

        AdView mAdView;
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        SharedPreferences prefs = getSharedPreferences("appelyasiya", 0);
        final int qid = prefs.getInt("qid", 0);
        final String dcavab=prefs.getString("dcavab", "Xəta var");
        final String serh=prefs.getString("serh", "Bu sualın şərhi sistemdə qeyd edilməmişdir");

        final int state=prefs.getInt("state", 1);
        TextView s=(TextView) findViewById(R.id.submitting);
        if(state==0){
            s.setText("Cavabınız yanlışdır.\n\n Düzgün cavab:"+ dcavab+"\n\nSualın şərhi düyməsinə basaraq izahı ilə tanış ola bilərsiniz.");
        }
        else{
            s.setText("Cavabınız tamamilə doğrudur\nSualın şərhi düyməsinə basaraq izahı ilə tanış ola bilərsiniz.");
        }
        Button anas=(Button) findViewById(R.id.anas);
        anas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent("com.example.mirakram.banmiok.EsasActivity"));
            }
        });

        Button serhing=(Button) findViewById(R.id.serhing);
        serhing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minter.isLoaded()) {
                    minter.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                AlertDialog.Builder a=new AlertDialog.Builder(ResultActivity.this);
                a.setTitle("Sualın şərhi");
                a.setMessage(serh);
                a.setNegativeButton("Oldu", null);
                AlertDialog alertDialog=a.create();
                alertDialog.show();
            }
        });

        Button novbeti=(Button) findViewById(R.id.novbeti);
        novbeti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent("com.example.mirakram.banmiok.GameActivity"));
            }
        });

        Button apping=(Button)findViewById(R.id.apping);
        apping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent("com.example.mirakram.banmiok.AppelyasiyaActivity"));
            }
        });
    }
}
