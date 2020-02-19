package com.example.mirakram.banmiok;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class AppelyasiyaActivity extends AppCompatActivity {
    String email;
    String subject;
    String message;
    EditText editTextMessage;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appelyasiya);

        AdView mAdView;
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        SharedPreferences prefs = getSharedPreferences("appelyasiya", 0);
        final int restoredText = prefs.getInt("qid", 0);
        final String cavab=prefs.getString("cavab", "Xəta var");
        final String dcavab=prefs.getString("dcavab", "Xəta var");
        SharedPreferences prefer = getSharedPreferences("MyPrefs", 0);
        final String username = prefer.getString("username", "Username Xətası");
        final String mail = prefer.getString("mail", "Mail Xətası");
        Button but=(Button) findViewById(R.id.button10);
        editTextMessage = (EditText) findViewById(R.id.editText);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = "banmiokapp@gmail.com";
                subject ="Username: "+username+ " Sual id: "+restoredText+" "+ "Yazdığı cavab:"+ cavab+" Düzgün cavab:"+dcavab;
                message = editTextMessage.getText().toString().trim()+"\n "+mail;
                AsyncCallWS2 t=new AsyncCallWS2();
                t.execute();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder exitting=new AlertDialog.Builder(AppelyasiyaActivity.this);
        exitting.setTitle("Geriyə qayıtmaq istəyi");
        exitting.setMessage("Geriyə qayıtmaq istədiyinizə istədiyinizə əminsiniz?");
        exitting.setPositiveButton("Bəli", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                Intent menu=new Intent("com.example.mirakram.banmiok.EsasActivity");
                startActivity(menu);
            }
        });
        exitting.setNegativeButton("Xeyr", null);
        AlertDialog alertDialog=exitting.create();
        alertDialog.show();

    }

    class AsyncCallWS2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(AppelyasiyaActivity.this,"Əlaqə qurulur","Xahiş edirik gözləyin...",false,false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            sending();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            AlertDialog.Builder wrongans = new AlertDialog.Builder(AppelyasiyaActivity.this);
            wrongans.setTitle("E-poçt göndərildi");
            wrongans.setMessage("Appelyasiya şikayətinizə tezliklə cavab veriləcək" );
            final AlertDialog alertDialog = wrongans.create();
            alertDialog.show();
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    alertDialog.cancel();
                    finish();
                }
            }, 2000);
        }

    }
    public void sending(){
        SendMail sm = new SendMail(getBaseContext(),email, subject, message);
        sm.execute();
    }

}
