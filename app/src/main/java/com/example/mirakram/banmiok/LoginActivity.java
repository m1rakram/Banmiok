package com.example.mirakram.banmiok;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class LoginActivity extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs" ;
    private static final String TAG ="response" ;
    public EditText usernameArea;
    public EditText passArea;
    public TextView firstUse;
    public TextView forgottenPassword;
    public Button verify;
    CheckBox check;
    private ProgressDialog progressDialog;
    Object resultString=null;
    public String pass, user;
    String text;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AdView mAdView;
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        if( getIntent().getBooleanExtra("Exit me", false)){
            finish();
            return; // add this to prevent from doing unnecessary stuffs
        }
        OnTextClickListener();
    }
    public void OnTextClickListener() {

        firstUse = (TextView) findViewById(R.id.signupButton);
        forgottenPassword = (TextView) findViewById(R.id.forgottenPass);
        passArea = (EditText) findViewById(R.id.passWord);
        usernameArea = (EditText) findViewById(R.id.userName);
        verify = (Button) findViewById(R.id.button);
        check=(CheckBox)findViewById(R.id.checkBox3);

        ////////////////////////////////////////////////////////////////////////////////////////////
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()) {
                    user = usernameArea.getText().toString();
                    pass = passArea.getText().toString();
                        AsyncCallWS task = new AsyncCallWS();
                        task.execute();
                }
                else
                    takeaction();
            }
        });


        //////////////// ilk istifadeciyemse
        firstUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()) {
                    Intent Signup = new Intent("com.example.mirakram.banmiok.SignupActivity");
                    startActivity(Signup);
                }
                else
                    takeaction();
            }
        });


        //////////////// kodu itirmisemse
        forgottenPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()) {
                    Intent Forgotten = new Intent("com.example.mirakram.banmiok.ForgottenActivity");
                    startActivity(Forgotten);
                }
                else
                    takeaction();
            }
        });
    }







    class AsyncCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(LoginActivity.this,"Əlaqə qurulur","Xahiş edirik gözləyin...",false,false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            logging(user,pass);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if(count==1){
                passArea.setError("Şifrəniz uyğun deyil");
                passArea.requestFocus();
            }
            else {
                if (count == 2) {
                    usernameArea.setError("İstifadəçi adı düzgün deyil");
                    usernameArea.requestFocus();
                }
            }
        }

    }
    public void logging(String tempus,String temppass) {
        count=0;
        String SOAP_ACTION = "http://tempuri.org/Login";
        String METHOD_NAME = "Login";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://13.82.57.25/WebService1.asmx?WSDL";



        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("username",tempus );
        request.addProperty("password",temppass );

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transport = new HttpTransportSE(URL);
        try {
            transport.call(SOAP_ACTION, envelope);
            resultString =  envelope.getResponse();
            String text=resultString.toString().trim();
            if(text.equals("loginsuccessful")) {
                SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("username", user.toLowerCase());
                editor.putString("password", pass);
                editor.putString("names", getfname()+" "+getsname());
                editor.putString("mail", getmail());
                String byting=getpic();
                byting=byting.replaceAll("<image>", "");
                byting=byting.replaceAll("<User_image>","");
                editor.putString("picture", byting);
                editor.putBoolean("check", check.isChecked());
                editor.commit();
                finish();
                Intent menu = new Intent("com.example.mirakram.banmiok.EsasActivity");
                startActivity(menu);
            }
            else {
                if (text.equals("incorrectpassword")){
                    count=1;
                    passArea.setError("Şifrəniz uyğun deyil");
                    passArea.requestFocus();
                }
                else
                {
                    count=2;
                    usernameArea.setError("İstifadəçi adı düzgün deyil");
                    usernameArea.requestFocus();
                }
            }
        } catch (Exception ex) {

            Log.e(TAG, "Error: " + ex.getMessage());
        }
    }
    public String getfname(){
        String SOAP_ACTION = "http://tempuri.org/getname";
        String METHOD_NAME = "getname";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://13.82.57.25/WebService1.asmx?WSDL";



        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("username", user);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transport = new HttpTransportSE(URL);
        try {
            transport.call(SOAP_ACTION, envelope);
            resultString =  envelope.getResponse();
            text=resultString.toString().trim();
        } catch (Exception ex) {

            Log.e(TAG, "Error: " + ex.getMessage());
        }
        return text;
    }
    public String getsname() {
        String SOAP_ACTION = "http://tempuri.org/getsname";
        String METHOD_NAME = "getsname";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://13.82.57.25//WebService1.asmx?WSDL";



        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("username", user);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transport = new HttpTransportSE(URL);
        try {
            transport.call(SOAP_ACTION, envelope);
            resultString =  envelope.getResponse();
            text=resultString.toString().trim();
        } catch (Exception ex) {

            Log.e(TAG, "Error: " + ex.getMessage());
        }
        return text;
    }

    public String getpic() {
        String SOAP_ACTION = "http://tempuri.org/getprofilimage";
        String METHOD_NAME = "getprofilimage";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://13.82.57.25//WebService1.asmx?WSDL";



        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("username", user);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transport = new HttpTransportSE(URL);
        try {
            transport.call(SOAP_ACTION, envelope);
            resultString =  envelope.getResponse();
            text=resultString.toString();
        } catch (Exception ex) {

            Log.e(TAG, "Error: " + ex.getMessage());
        }
        return text;
    }


    public String getmail() {
        String SOAP_ACTION = "http://tempuri.org/getmail";
        String METHOD_NAME = "getmail";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://13.82.57.25/WebService1.asmx?WSDL";



        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("username", user);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transport = new HttpTransportSE(URL);
        try {
            transport.call(SOAP_ACTION, envelope);
            resultString =  envelope.getResponse();
            text=resultString.toString().trim();
        } catch (Exception ex) {

            Log.e(TAG, "Error: " + ex.getMessage());
        }
        return text;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }





    public void takeaction(){
            AlertDialog.Builder wrongans=new AlertDialog.Builder(LoginActivity.this);
            wrongans.setTitle("İnternet bağlantınız yoxdur");
            wrongans.setMessage("Xahiş olunur internetlə bağlı parametrləri gözdən keçirəsiniz");
            wrongans.setPositiveButton("Oldu", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                }
            });
            AlertDialog alertDialog=wrongans.create();
            alertDialog.show();

    }





}
