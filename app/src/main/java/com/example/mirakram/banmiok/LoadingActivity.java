package com.example.mirakram.banmiok;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class LoadingActivity extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        imageView=(ImageView)findViewById(R.id.imageView);
        Animation animation= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash);
        imageView.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                boolean s=sharedpreferences.getBoolean("check", false);
                if(s) {
                    startActivity(new Intent("com.example.mirakram.banmiok.EsasActivity"));
                    finish();
                }else{
                    startActivity(new Intent("com.example.mirakram.banmiok.LoginActivity"));
                    finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
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
        AlertDialog.Builder wrongans=new AlertDialog.Builder(LoadingActivity.this);
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
