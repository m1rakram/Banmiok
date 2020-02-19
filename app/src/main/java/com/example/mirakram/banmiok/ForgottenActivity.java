package com.example.mirakram.banmiok;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Random;

public class ForgottenActivity extends AppCompatActivity {
    private static final String TAG ="response" ;
    String user, text, mail;
    private InterstitialAd minter;
    Object resultString=null;
    Button tesdiqle;
    EditText forlogin;
    EditText forrmail;
    EditText forpass;
    EditText forrpass, code;
    TextInputLayout s, sr, sc;
    ProgressDialog progressDialog;
    int count=0, flag=0;
    int c=getver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_forgotten);

        AdView mAdView;
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        minter=new InterstitialAd(this);
        minter.setAdUnitId("ca-app-pub-8584547019777134/3536051147");
        AdRequest request=new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        minter.loadAd(request);


        forlogin=(EditText) findViewById(R.id.Text_fusername);
        forrmail=(EditText) findViewById(R.id.Text_fadress);
        forpass=(EditText) findViewById(R.id.Text_fpass);
        forrpass=(EditText) findViewById(R.id.Text_frpass);
        tesdiqle=(Button) findViewById(R.id.button3);
        code=(EditText)findViewById(R.id.code);
        s=(TextInputLayout)findViewById(R.id.hintfpass);
        sr=(TextInputLayout)findViewById(R.id.hintfrpass);
        sc=(TextInputLayout)findViewById(R.id.hintcode);
        s.setVisibility(View.INVISIBLE);
        sr.setVisibility(View.INVISIBLE);
        sc.setVisibility(View.INVISIBLE);
        tesdiqle.setText("Təsdiqlə");
        regonclick();
    }

    public void regonclick(){
        tesdiqle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minter.isLoaded()) {
                    minter.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                user=forlogin.getText().toString().trim();
                mail=forrmail.getText().toString().trim();
                AsyncCallWS task = new AsyncCallWS();
                task.execute();
                if(count==0){
                    matchemail();
                }
            }
        });
    }


    class AsyncCallWS2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ForgottenActivity.this,"Əməliiyat aparılır","Xahiş edirik gözləyin...",false,false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            updatepass();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if(flag==1){
                forrpass.setError("Şifrə 6 simvoldan az olmamalı və düzgün təkrar edilməlidir");
                forrpass.requestFocus();
            }
        }

    }
    class AsyncCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ForgottenActivity.this,"Əməliiyat aparılır","Xahiş edirik gözləyin...",false,false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            checkmail();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if(count==0){
                sc.setVisibility(View.VISIBLE);
                tesdiqle.setText("Kodu təsdiqlə");
            }
            else {
                if (count == 2) {
                    forlogin.setError("İstifadəçi adı düzgün deyil");
                    forlogin.requestFocus();
                } else {
                    if (count == 1) {
                        forrmail.setError("E-poçt adresi uyğun deyil");
                    }
                }
            }
        }

    }
    public int getver(){
        Random rand = new Random();
        int n = 1000+rand.nextInt(9000);
        return n;
    }


    public void checkmail(){
        String SOAP_ACTION = "http://tempuri.org/checkmail";
        String METHOD_NAME = "checkmail";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://13.82.57.25/WebService1.asmx?WSDL";

        count=0;

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("username", user);
        request.addProperty("mail", mail);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transport = new HttpTransportSE(URL);
        try {
            transport.call(SOAP_ACTION, envelope);
            resultString =  envelope.getResponse();
            text=resultString.toString().trim();
            if(text.equals("verifysuccessful")){
                count=0;
            }
            else {
                if (text.equals("incorrectmail")) {
                    count=1;
                }
                else{
                    count=2;
                }
            }
        } catch (Exception ex) {

            Log.e(TAG, "Error: " + ex.getMessage());
        }
    }

    public void updatepass() {
        flag=0;
        String SOAP_ACTION = "http://tempuri.org/updatepass";
        String METHOD_NAME = "updatepass";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://13.82.57.25/WebService1.asmx?WSDL";
        if(forrpass.getText().toString().trim().equals(forpass.getText().toString().trim())&&forrpass.getText().toString().trim().length()>5) {
            String pass=forpass.getText().toString().trim();
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("password", pass);
            request.addProperty("username", user);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transport = new HttpTransportSE(URL);
            try {
                transport.call(SOAP_ACTION, envelope);
                resultString = envelope.getResponse();
                String text = resultString.toString().trim();

            } catch (Exception ex) {

                Log.e(TAG, "Error: " + ex.getMessage());
            }
        }
        else{
           flag=1;
        }
    }




    public void matchemail() {

        String email = mail;
        String subject = "E-poçt adresi təsdiqlənməsi";
        String message = "    Salam hörmətli oyunçu, E-poçt adresinizin təsdiqlənməsi üçün bu şifrəni tətbiqetmədə daxil edin.\n Təsdiq nömrəniz:"+ c +"\n    Qeyd edək ki e-poçt adresinizin düzgün olması profilinizdəki dəyişiklərinizdə, yeni məlumatlar və mükafatlandırılmalar haqqında siz xəbərdar olacaqsınız";
        SendMail sm = new SendMail(getBaseContext(), email, subject, message);
        sm.execute();
        tesdiqle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ppassword=code.getText().toString().trim();
                int p=Integer.parseInt(ppassword);
                if (p==c) {
                    sr.setVisibility(View.VISIBLE);
                    s.setVisibility(View.VISIBLE);
                    tesdiqle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AsyncCallWS2 task = new AsyncCallWS2();
                            task.execute();
                            finish();
                            Intent menu = new Intent("com.example.mirakram.banmiok.LoginActivity");
                            startActivity(menu);
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "Şifrə səhvdir", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
