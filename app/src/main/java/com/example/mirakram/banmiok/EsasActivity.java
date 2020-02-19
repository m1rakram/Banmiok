package com.example.mirakram.banmiok;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class EsasActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Object resultString=null;
    String text;
    private static final String TAG ="response" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esas);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        AdView mAdView;
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
        setnames();
        esasOnClickListener();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.forfacebook);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent facebookIntent = getOpenFacebookIntent(EsasActivity.this);
                startActivity(facebookIntent);
            }
        });
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.forinstagram);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent instagramIntent = getOpenInstagramIntent(EsasActivity.this);
                startActivity(instagramIntent);
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    public void esasOnClickListener(){
        Button game=(Button) findViewById(R.id.esas_yarish);
        Button statistics=(Button) findViewById(R.id.esas_stat);
        Button exit=(Button) findViewById(R.id.esas_exit);
        /////////////////////////////////////////////////////////////////////////////////////////
        game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()) {
                    AlertDialog.Builder gaming=new AlertDialog.Builder(EsasActivity.this);
                    gaming.setTitle("Oyuna Başla");
                    gaming.setMessage("Oyun qaydaları: Sual veriləcək və aşağı hissədə cavab yazmaq üçün hissə olacaqdır. 2 cür cavab qəbul olunur: 1)Azərbaycan şrifti ilə 2)ş=sh ç=ch ğ=gh ö=o ə=e ü=u ı=i\n 1 sözün qarışıq variantda yazılması yolverilməzdir");
                    gaming.setPositiveButton("Oyuna başla", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent Game = new Intent("com.example.mirakram.banmiok.GameActivity");
                            startActivity(Game);
                        }
                    });
                    gaming.setNegativeButton("Geriyə", null);
                    AlertDialog alertDialog=gaming.create();
                    alertDialog.show();

                }
                else
                    takeaction();
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////
        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()) {
                    Intent Statis = new Intent("com.example.mirakram.banmiok.StatisticsActivity");
                    startActivity(Statis);
                }
                else
                    takeaction();
            }
        });
        /////////////////////////////////////////////////////////////////////////////////////////
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder exitting=new AlertDialog.Builder(EsasActivity.this);
                exitting.setTitle("Çıxış istəyi");
                exitting.setMessage("Proqramı bağlamaq istədiyinizə əminsiniz?");
                exitting.setPositiveButton("Bəli", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                });
                exitting.setNegativeButton("Xeyr", null);
                AlertDialog alertDialog=exitting.create();
                alertDialog.show();
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder exitting=new AlertDialog.Builder(EsasActivity.this);
            exitting.setTitle("Çıxış istəyi");
            exitting.setMessage("Proqramı bağlamaq istədiyinizə əminsiniz?");
            exitting.setPositiveButton("Bəli", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                }
            });
            exitting.setNegativeButton("Xeyr", null);
            AlertDialog alertDialog=exitting.create();
            alertDialog.show();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.esas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(isNetworkAvailable()) {
            if (id == R.id.esas_about) {
                Intent About = new Intent("com.example.mirakram.banmiok.AboutActivity");
                startActivity(About);
            } else if (id == R.id.Esas_button) {
                rateApp();
            } else if (id == R.id.esas_profile) {
                Intent i = new Intent(EsasActivity.this, ProfileActivity.class);
               ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(EsasActivity.this, findViewById(R.id.imageView2), "Profilpicture");
                startActivity(i, optionsCompat.toBundle());
            } else if (id == R.id.Esas_send) {
                Intent i = new Intent("com.example.mirakram.banmiok.SendActivity");
                startActivity(i);
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void setnames() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", 0);
        String restoredText = prefs.getString("names", "Diqqet problem");
        String username=prefs.getString("username", "problem");
        String pic=prefs.getString("picture", "hecne");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        ImageView profileimage=(ImageView) header.findViewById(R.id.imageView2);
        TextView som = (TextView) header.findViewById(R.id.textView21);

        profileimage.setImageBitmap(StringToBitMap(pic));

        som.setText(restoredText);
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
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }


    public void takeaction(){
        AlertDialog.Builder wrongans=new AlertDialog.Builder(EsasActivity.this);
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

    public void rateApp()
    {
        try
        {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        }
        catch (ActivityNotFoundException e)
        {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21)
        {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        }
        else
        {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }

    public static Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.facebook.android", 0); //Checks if FB is even installed.
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/bhosintellectual/")); //Trys to make intent with FB's URI
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/bhosintellectual/")); //catches and opens a url to the desired page
        }
    }
    public static Intent getOpenInstagramIntent(Context context) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.instagram.android", 0); //Checks if Instagram is even installed.
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/bhosintellectual/")); //Trys to make intent with Instagram's URI
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/bhosintellectual/")); //catches and opens a url to the desired page
        }
    }





    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


}
