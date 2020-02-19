package com.example.mirakram.banmiok;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {
    String text, s="hhfhj", musername, mpicture;
    Object resultString = null;
    String byting;
    int count=0, defined;
    TextView  ozuser, ozsira, ozfaiz, ozdy;
    ListView mListView;
    ImageView prof;
    LinearLayout lin;
    private ProgressDialog progressDialog;
    private static final String TAG = "response";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        AdView mAdView;
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        mListView = (ListView) findViewById(R.id.listView);

        lin=(LinearLayout) findViewById(R.id.lin);
        ozuser=(TextView) findViewById(R.id.ozuser);
        ozsira=(TextView) findViewById(R.id.ozsira);
        ozfaiz=(TextView) findViewById(R.id.ozfaiz);
        ozdy=(TextView) findViewById(R.id.ozdy);
        prof=(ImageView) findViewById(R.id.ozimaging);
        SharedPreferences prefs = getSharedPreferences("MyPrefs", 0);
        musername = prefs.getString("username", "Diqqet problem");
        mpicture = prefs.getString("picture", "Diqqet problem");
        ozuser.setText(musername);
        prof.setImageBitmap(StringToBitMap(mpicture));
        AsyncCallWS task=new AsyncCallWS();
        task.execute();
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


    class AsyncCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(StatisticsActivity.this,"Əlaqə qurulur","Xahiş edirik gözləyin...",false,false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            getscore();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            setstat(text);
            lin.setVisibility(View.VISIBLE);
            if(defined!=100){
                ozfaiz.setText("#%");
                ozdy.setText("#/#");
                ozsira.setText("#");
                AlertDialog.Builder alert=new AlertDialog.Builder(StatisticsActivity.this);
                alert.setTitle("Məlumatlandırma");
                alert.setMessage("Sizin reyting cədvəlində yarışmağınız üçün minimum 10 suala cavab verməlinisiz. Hal hazırda siz yalnız profil parametrlərindən öz nəticələrinizə baxa bilirsiniz. Oyunda 10 suala cavab verdikdən sonra isə siz nəticələrinizə bu səhifədən də baxa biləcəksiniz");
                alert.setNegativeButton("Oldu", null);
                AlertDialog alertDialog=alert.create();
                alertDialog.show();
            }
        }

    }




    public void getscore() {
        String SOAP_ACTION = "http://tempuri.org/getscore";
        String METHOD_NAME = "getscore";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://13.82.57.25//WebService1.asmx?WSDL";



        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

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



    public void setstat(String so){


        ArrayList<Person> peopleList = new ArrayList<>();

        while(!so.trim().isEmpty()) {
            count++;
            String c=String.valueOf(count);


            StringBuilder builder = new StringBuilder(so);
            int start = builder.indexOf("umage>") + "umage>".length();
            int end = builder.indexOf("</User_umage");
            String imege = builder.substring(start, end).trim();

            end = end + "</User_umage>".length();
            so = so.substring(end, so.length());

            builder = new StringBuilder(so);
            start = builder.indexOf("username>") + "username>".length();
            end = builder.indexOf("</User_username");
            String name = builder.substring(start, end).trim();

            end = end + "</User_username>".length();
            so = so.substring(end, so.length());


            builder = new StringBuilder(so);
            start = builder.indexOf("YANLIS>") + "YANLIS>".length();
            end = builder.indexOf("</YANLIS");
            String neg = builder.substring(start, end).trim();

            end = end + "</YANLIS>".length();
            so = so.substring(end, so.length());

            builder = new StringBuilder(so);
            start = builder.indexOf("DUZ>") + "DUZ>".length();
            end = builder.indexOf("</DUZ");
            String pos = builder.substring(start, end).trim();

            end = end + "</DUZ>".length();
            so = so.substring(end, so.length());

            builder = new StringBuilder(so);
            start = builder.indexOf("Faizi>") + "Faizi>".length();
            end = builder.indexOf("</Faizi");
            String perc = builder.substring(start, end).trim();

            end = end + "</Faizi></EVENT>".length();
            so = so.substring(end, so.length());
            if(name.trim().equals(musername.trim())){
                ozdy.setText(pos+"/"+neg);
                ozfaiz.setText(perc+"%");
                ozsira.setText(c);
                defined=100;
            }

            Person john = new Person(name, pos+"/"+neg, perc+"%", imege, c);
            peopleList.add(john);
        }

        PersonListAdapter adapter = new PersonListAdapter(this, R.layout.adapter_view_layout, peopleList);
        mListView.setAdapter(adapter);


    }
}
