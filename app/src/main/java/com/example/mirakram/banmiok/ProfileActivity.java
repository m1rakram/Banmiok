package com.example.mirakram.banmiok;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ProfileActivity extends AppCompatActivity {
    Uri imageurl;
    byte[] b;
    Object resultString=null;
    String text, byting, scores, password, test;
    private static final String TAG ="response" ;
    private ProgressDialog progressDialog;
    private static final int PICK_IMAGE=100;
    ImageView profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        AdView mAdView;
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
        AsyncCallWS2 task2 = new AsyncCallWS2();
        task2.execute();
        profile=(ImageView) findViewById(R.id.imageView4);
        Button changim=(Button) findViewById(R.id.floating);
        Button sifreni=(Button) findViewById(R.id.button8);
        changim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()) {
                    openGallery();
                }
                else
                    takeaction();
            }
        });

        sifreni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(ProfileActivity.this);

                final View view = layoutInflater.inflate(R.layout.dialoglayout, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(ProfileActivity.this).create();
                alertDialog.setTitle("Şifrənin dəyişilməsi");
                alertDialog.setCancelable(false);
                alertDialog.setMessage("Aşağıdakı bölmələri dolduraraq şifrənizi dəyişə bilərsiniz");


                final EditText etComments = (EditText) view.findViewById(R.id.etComments);
                final EditText etComments2 = (EditText) view.findViewById(R.id.etComments2);
                final EditText etComments3 = (EditText) view.findViewById(R.id.etComments3);

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences prefs = getSharedPreferences("MyPrefs", 0);
                        String restoredText = prefs.getString("password", "Diqqet problem");
                        if(!etComments.getText().toString().trim().equals(restoredText)){
                            Toast.makeText(ProfileActivity.this, "Keçmiş şifrə düzgün daxil edilmədi", Toast.LENGTH_LONG).show();
                        }
                        else{
                            if(etComments2.getText().toString().trim().length()<6){
                                Toast.makeText(ProfileActivity.this, "Yeni şifrə 6 simvoldan az olmamalıdır", Toast.LENGTH_LONG).show();
                            }
                            else {
                                if (!etComments2.getText().toString().trim().equals(etComments3.getText().toString().trim())) {
                                    Toast.makeText(ProfileActivity.this, "Yeni şifrə düzgün təkrar edilmədi", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    password=etComments2.getText().toString().trim();
                                    AsyncCallWS3 task=new AsyncCallWS3();
                                    task.execute();
                                    SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString("password", password);
                                    editor.commit();
                                    Toast.makeText(ProfileActivity.this, "Şifrəniz dəyişildi", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                });


                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.cancel();
                    }
                });



                alertDialog.setView(view);
                alertDialog.show();
            }
        });
    }


    public void openGallery(){
        Intent gallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    @Override
    public void onActivityResult(int requestcode, int resultcode, Intent data){
        super.onActivityResult(requestcode,resultcode,data);
        if(resultcode==RESULT_OK&&requestcode==PICK_IMAGE) {
            try {
                Uri imageUri = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                selectedImage = getResizedBitmap(selectedImage, 200);// 400 is for example, replace with desired size

                profile.setImageBitmap(selectedImage);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
            Bitmap ourbitmap = ((BitmapDrawable)profile.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ourbitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            profile.setImageBitmap(ourbitmap);
            b = baos.toByteArray();
            test = Base64.encodeToString(b, Base64.DEFAULT);
            byting=test;

            Log.i(TAG, byting);
            AsyncCallWS task = new AsyncCallWS();
            task.execute();

    }

    @Override
    public void onBackPressed() {
        finish();
        Intent i=new Intent("com.example.mirakram.banmiok.EsasActivity");
        startActivity(i);
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

    class AsyncCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ProfileActivity.this,"Əlaqə qurulur","Xahiş edirik gözləyin...",false,false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            updateimage(byting);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("picture", byting);
            editor.commit();
        }
    }

    public String updateimage(String bites){
        String SOAP_ACTION = "http://tempuri.org/saveimage";
        String METHOD_NAME = "saveimage";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://13.82.57.25/WebService1.asmx?WSDL";

        SharedPreferences prefs = getSharedPreferences("MyPrefs", 0);
        String user = prefs.getString("username", "Diqqet problem");

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("bytes", bites);
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





    public void setnames(){
        SharedPreferences prefs = getSharedPreferences("MyPrefs", 0);
        String restoredText = prefs.getString("names", "Diqqet problem");
        String byting=prefs.getString("picture", "dsdads");
        ImageView profile=(ImageView) findViewById(R.id.imageView4);
        profile.setImageBitmap(StringToBitMap(byting));
        TextView som=(TextView) findViewById(R.id.textView22);
        som.setText(restoredText+" "+scores);
    }
    public  void logout(View view){
        AlertDialog.Builder exitting=new AlertDialog.Builder(ProfileActivity.this);
        exitting.setTitle("Profildən çıxış");
        exitting.setMessage("Profilinizdən çıxmaq istədiyinizə əminsiniz?");
        exitting.setPositiveButton("Bəli", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", 0);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();
                finish();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        exitting.setNegativeButton("Xeyr", null);
        AlertDialog alertDialog=exitting.create();
        alertDialog.show();

    }
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }


    public void takeaction(){
        AlertDialog.Builder wrongans=new AlertDialog.Builder(ProfileActivity.this);
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

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }



    class AsyncCallWS2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ProfileActivity.this,"Əlaqə qurulur","Xahiş edirik gözləyin...",false,false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            String n, m;
            int f;
            m=gettrue();
            n=getfalse();
            if(Integer.parseInt(n)+Integer.parseInt(m)==0) {
               f=0;
            }
            else {
                f = Integer.parseInt(m) * 100 / (Integer.parseInt(n) + Integer.parseInt(m));
            }
            scores = "\n" + m + "/" + n + "  " + Integer.toString(f) + "%";
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            setnames();
        }

    }

    public String getfalse(){
        String SOAP_ACTION = "http://tempuri.org/getsinglefalses";
        String METHOD_NAME = "getsinglefalses";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://13.82.57.25/WebService1.asmx?WSDL";

        SharedPreferences prefs = getSharedPreferences("MyPrefs", 0);
        String user = prefs.getString("username", "Diqqet problem");

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
    public String gettrue(){
        String SOAP_ACTION = "http://tempuri.org/getsingletrues";
        String METHOD_NAME = "getsingletrues";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://13.82.57.25/WebService1.asmx?WSDL";

        SharedPreferences prefs = getSharedPreferences("MyPrefs", 0);
        String user = prefs.getString("username", "Diqqet problem");

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


    class AsyncCallWS3 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ProfileActivity.this,"Əlaqə qurulur","Xahiş edirik gözləyin...",false,false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            updatepass(password);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
        }

    }


    public void updatepass(String pass){
        String SOAP_ACTION = "http://tempuri.org/updatepass";
        String METHOD_NAME = "updatepass";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://13.82.57.25/WebService1.asmx?WSDL";

        SharedPreferences prefs = getSharedPreferences("MyPrefs", 0);
        String user = prefs.getString("username", "Diqqet problem");

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("username", user);
        request.addProperty("password", pass);
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
    }

    public Bitmap getbit(Uri fileUri){
        int MAX_IMAGE_SIZE = 200 * 1024; // max final file size
        Bitmap bmpPic = BitmapFactory.decodeFile(fileUri.getPath());
        if ((bmpPic.getWidth() >= 1024) && (bmpPic.getHeight() >= 1024)) {
            BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
            bmpOptions.inSampleSize = 1;
            while ((bmpPic.getWidth() >= 1024) && (bmpPic.getHeight() >= 1024)) {
                bmpOptions.inSampleSize++;
                bmpPic = BitmapFactory.decodeFile(fileUri.getPath(), bmpOptions);
            }
            Log.d(TAG, "Resize: " + bmpOptions.inSampleSize);
        }
        int compressQuality = 104; // quality decreasing by 5 every loop. (start from 99)
        int streamLength = MAX_IMAGE_SIZE;
        while (streamLength >= MAX_IMAGE_SIZE) {
            ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
            compressQuality -= 5;
            Log.d(TAG, "Quality: " + compressQuality);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
            byte[] bmpPicByteArray = bmpStream.toByteArray();
            streamLength = bmpPicByteArray.length;
            Log.d(TAG, "Size: " + streamLength);
        }
        try {
            File file = new File("outputfile.txt");
            FileOutputStream bmpFile = new FileOutputStream(file, true);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpFile);
            bmpFile.flush();
            bmpFile.close();
        } catch (Exception e) {
            Log.e(TAG, "Error on saving file");
        }
        return bmpPic;
    }



}
