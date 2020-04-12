package com.tel5027.pigdice.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.tel5027.pigdice.R;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity {

    private InterstitialAd startGameAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        startGameAd = new InterstitialAd(this);
        startGameAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        startGameAd.loadAd(new AdRequest.Builder().build());

        startGameAd.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(int errorCode){
                Intent i = new Intent(MainActivity.this, GameActivity.class);
                startActivity(i);
            }

            @Override
            public void onAdClosed(){
                startGameAd.loadAd(new AdRequest.Builder().build());
                Intent i = new Intent(MainActivity.this, GameActivity.class);
                startActivity(i);
            }
        });
    }

    public void openOptions(View view) {
        Intent i = new Intent(MainActivity.this, Options.class);
        startActivity(i);

    }

    public void openGame(View view) {
        if (startGameAd.isLoaded()) {
            startGameAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
            Intent i = new Intent(MainActivity.this, GameActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
