package com.example.mirakram.banmiok;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class GameActivity extends AppCompatActivity {
    TextView show, sual;
    CountDownT timer;
    Button ansver;
    EditText anstext;
    String sql="cavab", asd;
    String text, username, serh;
    int id, qid,state,count=0, flag=0;
    Object resultString=null;
    ImageView qimage;
    ProgressDialog progressDialog;
    private static final String TAG ="response" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_game);
        getuserpname();
        timertemasi();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        timertemasi();
    }

    public void timertemasi(){
        anstext=(EditText) findViewById(R.id.answer);
        ansver=(Button) findViewById(R.id.qverify);
        ansver.setClickable(true);
        anstext.setVisibility(View.VISIBLE);
        show=(TextView) findViewById(R.id.texttime);
        sual=(TextView) findViewById(R.id.textView3);
        if (isNetworkAvailable()) {
            flag=0;
            AsyncCallWS3 task = new AsyncCallWS3();
            task.execute();
            AdView mAdView;
            mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
            mAdView.loadAd(adRequest);
            ansver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flag=5;
                    timer.cancel();
                    if (anstext.getText().length() == 0) {
                        noans();
                    } else
                        answerval(anstext.getText().toString());
                }
            });

        }   /*
        */
        else
            takeaction();
    }



    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onBackPressed() {
        if(flag==0) {
            AlertDialog.Builder wrongans = new AlertDialog.Builder(GameActivity.this);
            wrongans.setTitle("Çıxmaq istədiyinizə əminsiniz?");
            wrongans.setMessage("Cavab təsdiqlənməmişdir \n Çıxış düyməsinə basaraq çıxışınız təsdiq etmiş olacaqsınız və sual cavabsız buraxıldı kimi qeydə alınacaq");
            wrongans.setPositiveButton("Çıxış", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    timer.cancel();
                    noans();
                }
            });
            wrongans.setNegativeButton("Davam edirəm", null);
            AlertDialog alertDialog = wrongans.create();
            alertDialog.show();
        }
        else {
            finish();
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////






    public class CountDownT extends CountDownTimer{
        public CountDownT(long InMillisSeconds, long TimeGap){
            super(InMillisSeconds, TimeGap);
        }
        @Override
        public void onTick(long i) {
            show.setText(String.format("%02d", i/1000));
        }


        @Override
        public void onFinish(){
            noans();
        }
    }
    private void answerval(String ans){
        String sql11 = "#1", sql12 = "#1", sql21 = "#1", sql22 = "#1";
        ans=ans.replaceAll("^ +| +$|( )+", "$1");
        sual.setText("Geriyə düyməsini basaraq Ana səhifəyə qayıda bilərsiniz");
        anstext.setVisibility(View.INVISIBLE);
        ansver.setClickable(false);
        ans=ans.trim();
        ans=ans.toLowerCase();
        sql=sql.trim();
        sql=sql.replace("N'", "");
        sql=sql.replace("'", "");
        sql=sql.toLowerCase();
        String sql2;
        sql2=sql.replace("ş", "sh");
        sql2=sql2.replace("ç", "ch");
        sql2=sql2.replace("ü", "u");
        sql2=sql2.replace("ə", "e");
        sql2=sql2.replace("ğ", "gh");
        sql2=sql2.replace("ı", "i");
        sql2=sql2.replace("ö", "o");

        SharedPreferences sharedpreferences = getSharedPreferences("appelyasiya", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("cavab", ans);
        editor.putString("dcavab", sql);
        editor.putInt("qid", qid);
        serh=serh.replace("?", "ə");
        editor.putString("serh", serh);


        if(sql.trim().contains("##")) {
            StringBuilder builder = new StringBuilder(sql);
            int f = builder.indexOf("##");
            int start = builder.indexOf("##") + "##".length();
            int end = builder.length();
            sql11 = builder.substring(0, f).trim();
            sql12 = builder.substring(start, end).trim();
            f = builder.indexOf("##");
            start = builder.indexOf("##") + "##".length();
            end = builder.length();
            sql21 = builder.substring(0, f).trim();
            sql22 = builder.substring(start, end).trim();
            Log.i(TAG, sql11);
            editor.putString("dcavab", sql11+" və ya "+ sql12);
        }




        if(ans.contains(sql)||ans.contains(sql2)||(ans.contains(sql12)&&(!sql12.equals("#1")))||(ans.contains(sql11)&&(!sql11.equals("#1")))||(ans.contains(sql21)&&(!sql21.equals("#1")))||(ans.contains(sql22)&&(!sql22.equals("#1")))||
                sql.contains(ans)||sql2.contains(ans)||(sql12.contains(ans)&&(!sql12.equals("#1")))||(sql11.contains(ans)&&(!sql11.equals("#1")))||(sql21.contains(ans)&&(!sql21.equals("#1")))||(sql22.contains(ans)&&(!sql22.equals("#1")))){
            state=1;
            editor.putInt("state", state);
            AsyncCallWS2 tasking = new AsyncCallWS2();
            tasking.execute();
        }
        else
        {
            state=0;
            AsyncCallWS2 tasking = new AsyncCallWS2();
            tasking.execute();
            editor.putInt("state", state);
            //SharedPreferences sharedpreferences = getSharedPreferences("appelyasiya", Context.MODE_PRIVATE);
            //SharedPreferences.Editor editor = sharedpreferences.edit();
            //editor.putString("cavab", ans);
            //editor.putString("dcavab", sql);
            //editor.putInt("qid", qid);
            //editor.commit();
        }
        editor.commit();
    }



    private void noans(){
        flag=1;
        state=0;

        anstext.setVisibility(View.INVISIBLE);
        ansver.setClickable(false);
        SharedPreferences sharedpreferences = getSharedPreferences("appelyasiya", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("cavab", "Cavabsız buraxılmışdır!");
        editor.putString("dcavab", sql);
        editor.putInt("qid", qid);
        editor.putInt("state", state);
        serh=serh.replace("?", "ə");
        editor.putString("serh", serh);
        editor.commit();
        AsyncCallWS2 tasking = new AsyncCallWS2();
        tasking.execute();
    }


    public void getuserpname(){
        SharedPreferences prefs = getSharedPreferences("MyPrefs", 0);
        username = prefs.getString("username", "problem");
    }


///////////////////////////////////////////////////////////////////////////////////////////////






    class AsyncCallWS2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(GameActivity.this,"Əlaqə qurulur","Xahiş edirik gözləyin...",false,false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            setstate(qid, state);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            finish();
            startActivity(new Intent("com.example.mirakram.banmiok.ResultActivity"));
        }

    }




    class AsyncCallWS3 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(GameActivity.this,"Əlaqə qurulur","Xahiş edirik gözləyin...",false,false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            getrandomid();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            flag=0;
            if(count==5){
                sual.setText("Geriyə düyməsini basaraq Ana səhifəyə qayıda bilərsiniz");
                anstext.setVisibility(View.INVISIBLE);
                ansver.setClickable(false);
                flag=5;
                AlertDialog.Builder wrongans = new AlertDialog.Builder(GameActivity.this);
                wrongans.setTitle("Suallar bitdi");
                wrongans.setMessage("Yeni suallar tezliklə əlavə olunacaq. Bu haqda sizə məlumat veriləcəkdir");
                wrongans.setPositiveButton("Oldu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog alertDialog = wrongans.create();
                alertDialog.show();
            }
            else{
                AsyncCallWS tasking=new AsyncCallWS();
                tasking.execute();
            }

        }

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    class AsyncCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(GameActivity.this,"Əlaqə qurulur","Xahiş edirik gözləyin...",false,false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            settrio(qid);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.i(TAG, asd);

            StringBuilder builder = new StringBuilder(asd);
            int end = builder.indexOf("@@");
            String sualtext = builder.substring(0, end).trim();
            sual.setText(sualtext);

            int start = builder.indexOf("@@")+2;
            end = builder.indexOf("==");
            sql = builder.substring(start, end).trim();

            start = builder.indexOf("==")+2;
            end = builder.indexOf("%%");
            serh = builder.substring(start, end).trim();
            anstext.getText().clear();
            timer = new CountDownT(80000, 1);
            timer.onTick(80000);
            timer.start();

        }

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void getrandomid() {
        String SOAP_ACTION = "http://tempuri.org/getrandomques";
        String METHOD_NAME = "getrandomques";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://13.82.57.25/WebService1.asmx?WSDL";



        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("username", username);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transport = new HttpTransportSE(URL);
        try {
            count=0;
            transport.call(SOAP_ACTION, envelope);
            resultString =  envelope.getResponse();
            id = Integer.parseInt(resultString.toString().trim());
            if(id==0){
                count=5;
                qid=0;
            }
            else
                qid=id;

        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void setstate(int n, int ans) {
        String SOAP_ACTION = "http://tempuri.org/sendstate";
        String METHOD_NAME = "sendstate";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://13.82.57.25/WebService1.asmx?WSDL";



        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("username",username);
        request.addProperty("qid", n);
        request.addProperty("ans", ans);
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
    public void getqimage(int n) {
        String SOAP_ACTION = "http://tempuri.org/getqimage";
        String METHOD_NAME = "getqimage";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://13.82.57.25/WebService1.asmx?WSDL";



        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("n", n);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transport = new HttpTransportSE(URL);
        try {
            transport.call(SOAP_ACTION, envelope);
            resultString =  envelope.getResponse();
            text=resultString.toString().trim();
            text=text.replaceAll("<Image>", "");
            text=text.replaceAll("<Q_image>","");
            text=text.replaceAll("</Image>", "");
            text=text.replaceAll("</Q_image>","");
            qimage.setVisibility(View.VISIBLE);

            Bitmap bm = StringToBitMap(text);
        } catch (Exception ex) {

            Log.e(TAG, "Error: " + ex.getMessage());
        }
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
        AlertDialog.Builder wrongans=new AlertDialog.Builder(GameActivity.this);
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
    public void settrio(int n) {
        String SOAP_ACTION = "http://tempuri.org/gettrio";
        String METHOD_NAME = "gettrio";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://13.82.57.25/WebService1.asmx?WSDL";



        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("qid", n);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transport = new HttpTransportSE(URL);
        try {
            transport.call(SOAP_ACTION, envelope);
            resultString =  envelope.getResponse();
            text=resultString.toString().trim();
            asd=text;



        } catch (Exception ex) {

            Log.e(TAG, "Error: " + ex.getMessage());
        }
    }
}