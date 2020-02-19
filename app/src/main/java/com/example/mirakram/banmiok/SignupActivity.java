package com.example.mirakram.banmiok;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "response";
    public Button Tesdiqle;
    public TextView txting;
    public EditText ET_FNAME, ET_SNAME, ET_MAIL, ET_USERNAME, ET_PASS, ET_RPASS, txtm;
    private String fname, sname, mail, password, username;
    Object resultString = null;
    int count = 0,flag;
    int c=getver();
    TextInputLayout ttt;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_signup);
        AdView mAdView;
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
        regOnClickListener();
        txtm=(EditText)findViewById(R.id.textem);
        txting=(TextView)findViewById(R.id.soz);
        ttt=(TextInputLayout)findViewById(R.id.hintem);
        txting.setVisibility(View.INVISIBLE);
        ttt.setVisibility(View.INVISIBLE);
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public void regOnClickListener() {
        ET_FNAME = (EditText) findViewById(R.id.textName);
        ET_SNAME = (EditText) findViewById(R.id.textSurname);
        ET_MAIL = (EditText) findViewById(R.id.textMail);
        ET_USERNAME = (EditText) findViewById(R.id.textUser);
        ET_PASS = (EditText) findViewById(R.id.textPass);
        ET_RPASS = (EditText) findViewById(R.id.textRPass);
        Tesdiqle = (Button) findViewById(R.id.button2);


        Tesdiqle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!namesval(ET_FNAME.getText().toString())) {
                    ET_FNAME.setError("Ad minimum 3 simvoldan ibarət olmalıdır");
                    ET_FNAME.requestFocus();
                } else {
                    if (!namesval(ET_SNAME.getText().toString())) {
                        ET_SNAME.setError("Soyad minimum 3 simvoldan ibarət olmalıdı");
                        ET_SNAME.requestFocus();
                    } else {
                        if (!mailval(ET_MAIL.getText().toString().trim())) {
                            ET_MAIL.setError("E-poçt adresini düzgün daxil edin");
                            ET_MAIL.requestFocus();
                        } else {
                            if (!usernameval(ET_USERNAME.getText().toString())) {
                                ET_USERNAME.setError("İstifadəçi adı minimum 3 simvoldan ibarət olmalıdı");
                                ET_USERNAME.requestFocus();
                            } else {
                                if (!passwordval(ET_PASS.getText().toString())) {
                                    ET_PASS.setError("Şifrəniz minimum 6 simvoldan ibarət olmalıdı");
                                    ET_PASS.requestFocus();
                                } else {
                                    if (!ET_PASS.getText().toString().equals(ET_RPASS.getText().toString())) {
                                        ET_RPASS.setError("Şifrəni yanlış təkrar etdiniz");
                                        ET_RPASS.requestFocus();

                                    } else {
                                        fname = ET_FNAME.getText().toString();
                                        sname = ET_SNAME.getText().toString();
                                        mail = ET_MAIL.getText().toString().trim();
                                        username = ET_USERNAME.getText().toString();
                                        password = ET_PASS.getText().toString();
                                            SignupActivity.Classs2 tasking = new SignupActivity.Classs2();
                                            tasking.execute();
                                    }
                                }
                            }

                        }
                    }
                }
            }
        });


    }

    private boolean namesval(String temp) {
        if (temp.length() < 3) {
            return false;
        } else
            return true;

    }

    private boolean mailval(String temp) {

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!temp.matches(emailPattern)) {
            return false;
        } else
            return true;
    }

    private boolean passwordval(String temp) {
        if (temp.length() < 6) {
            return false;
        } else
            return true;
    }

    private boolean usernameval(String temp) {
        if (temp.length()>2) {
            return true;
        } else
            return false;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    private class Classs2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(SignupActivity.this,"Əlaqə qurulur","Xahiş edirik gözləyin...",false,false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            mailrepition();
            usernamerepition();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if(flag==2){
                ET_USERNAME.setError("Daxil etdiyiniz istifadəçi adı artıq istifadə olunub");
                ET_USERNAME.requestFocus();
            }
            else if(flag==1){
                ET_MAIL.setError("Daxil etdiyiniz e poct artıq istifadə olunub");
                ET_MAIL.requestFocus();
            }
            else{
                matchemail();
            }
        }

    }
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(SignupActivity.this,"Əlaqə qurulur","Xahiş edirik gözləyin...",false,false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            signingup();
            updateimage();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
        }

    }

    public int getver(){
        Random rand = new Random();
        int n = 1000+rand.nextInt(9000);
        return n;
    }
    public void signingup() {
        String SOAP_ACTION = "http://tempuri.org/Signup";
        String METHOD_NAME = "Signup";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://13.82.57.25/WebService1.asmx?WSDL";


        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("fname", fname);
        request.addProperty("sname", sname);
        request.addProperty("mail", mail.toLowerCase());
        request.addProperty("username", username.toLowerCase());
        request.addProperty("pass", password.toLowerCase());
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

    public void matchemail() {

        txting.setVisibility(View.VISIBLE);
        ttt.setVisibility(View.VISIBLE);
        ScrollView sv = (ScrollView)findViewById(R.id.scrl);
        sv.smoothScrollTo(0, sv.getBottom());
        Tesdiqle.setText("Təsdiqlə");
        String email = mail;
        String subject = "E-poçt adresi təsdiqlənməsi";
        String message = "    Salam Hörmətli " + fname + " " + sname + ", E-poçt adresinizin təsdiqlənməsi üçün bu şifrəni tətbiqetmədə daxil edin.\n Təsdiq nömrəniz:"+ c +"\n    Qeyd edək ki e-poçt adresinizin düzgün olması profilinizdəki dəyişiklərinizdə, yeni məlumatlar və mükafatlandırılmalar haqqında siz xəbərdar olacaqsınız";
        SendMail sm = new SendMail(getBaseContext(), email, subject, message);
        sm.execute();
        Tesdiqle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ppassword = txtm.getText().toString().trim();
                int p=Integer.parseInt(ppassword);
                count=0;
                if (p==c) {
                    SignupActivity.AsyncCallWS task = new SignupActivity.AsyncCallWS();
                    task.execute();
                    SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.putString("names", fname+" "+sname);
                    editor.putString("mail", mail);
                    editor.putString("picture", emptybyte());
                    editor.putBoolean("check", false);
                    editor.commit();
                    AlertDialog.Builder signing=new AlertDialog.Builder(SignupActivity.this);
                    signing.setTitle("Profiliniz yaradıldı");
                    signing.setMessage("Sorğunuz qəbul edildi. Tətbiqetməni profilinizlə işlətmək üçün birbaşa Ana səhifəyə yönləndirilirsiniz");
                    signing.setPositiveButton("Oldu", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            Intent intent=new Intent("com.example.mirakram.banmiok.EsasActivity");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    });
                    AlertDialog a=signing.create();
                    a.show();
                } else {
                    Toast.makeText(getApplicationContext(), "Şifrə səhvdir", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
    public void updateimage() {
        String SOAP_ACTION = "http://tempuri.org/saveimage";
        String METHOD_NAME = "saveimage";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://13.82.57.25/WebService1.asmx?WSDL";


        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("bytes", emptybyte());
        request.addProperty("username", username);
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
    public String emptybyte(){
        Uri imageUri=getUriToResource(getApplicationContext(), R.drawable.profile);
        InputStream imageStream = null;
        try {
            imageStream = getContentResolver().openInputStream(imageUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
        selectedImage = getResizedBitmap(selectedImage, 200);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        String test = Base64.encodeToString(b, Base64.DEFAULT);
        return test;
    }


    public void mailrepition() {
        String SOAP_ACTION = "http://tempuri.org/checkmailrepitition";
        String METHOD_NAME = "checkmailrepitition";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://13.82.57.25/WebService1.asmx?WSDL";


        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("mail", mail);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transport = new HttpTransportSE(URL);
        try {
            transport.call(SOAP_ACTION, envelope);
            resultString = envelope.getResponse();
            String text = resultString.toString().trim();
            flag=0;
            if(text.equals("problem"))
            {
                flag=1;
            }
        } catch (Exception ex) {

            Log.e(TAG, "Error: " + ex.getMessage());
        }
    }

    public void usernamerepition() {
        String SOAP_ACTION = "http://tempuri.org/checkusernamerepitition";
        String METHOD_NAME = "checkusernamerepitition";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://13.82.57.25/WebService1.asmx?WSDL";


        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("username", username);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transport = new HttpTransportSE(URL);
        try {
            transport.call(SOAP_ACTION, envelope);
            resultString = envelope.getResponse();
            String text = resultString.toString().trim();
            if(text.equals("problem"))
            {
                flag=2;
            }
        } catch (Exception ex) {

            Log.e(TAG, "Error: " + ex.getMessage());
        }
    }
    public static final Uri getUriToResource(@NonNull Context context,
                                             @AnyRes int resId)
            throws Resources.NotFoundException {
        /** Return a Resources instance for your application's package. */
        Resources res = context.getResources();
        /**
         * Creates a Uri which parses the given encoded URI string.
         * @param uriString an RFC 2396-compliant, encoded URI
         * @throws NullPointerException if uriString is null
         * @return Uri for this given uri string
         */
        Uri resUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + res.getResourcePackageName(resId)
                + '/' + res.getResourceTypeName(resId)
                + '/' + res.getResourceEntryName(resId));
        /** return uri */
        return resUri;
    }
}
