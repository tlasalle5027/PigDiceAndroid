package com.tel5027.pigdice.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.tel5027.pigdice.R;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.tel5027.pigdice.Util.Constants;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PurchasesUpdatedListener {

    private InterstitialAd startGameAd;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String ITEM_SKU_ADREMOVAL = "remove_ads";
    private BillingClient billingClient;
    private ImageButton removeAdsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        billingClient = BillingClient.newBuilder(MainActivity.this).setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener() {

            @Override
            public void onBillingSetupFinished(int responseCode) {
            }

            @Override
            public void onBillingServiceDisconnected() {
            }
        });

        List skuList = new ArrayList();
        skuList.add(ITEM_SKU_ADREMOVAL);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        billingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                if(skuDetailsList != null && responseCode == BillingClient.BillingResponse.OK){
                    for (SkuDetails skuDetails : skuDetailsList){
                        String price = skuDetails.getPrice();
                    }

                }
            }
        });

        removeAdsButton = findViewById(R.id.remove_ads);
        removeAdsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BillingFlowParams params = BillingFlowParams.newBuilder()
                        .setSku(ITEM_SKU_ADREMOVAL)
                        .setType(BillingClient.SkuType.INAPP)
                        .build();
                int responseCode = billingClient.launchBillingFlow(MainActivity.this, params);
            }
        });

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        startGameAd = new InterstitialAd(this);
        startGameAd.setAdUnitId("ca-app-pub-2961110423231573/9746550124");
        startGameAd.loadAd(new AdRequest.Builder().build());

        startGameAd.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(int errorCode){
                Log.d("TAG", "The interstitial failed to load. Error Code: " + errorCode);

            }

            @Override
            public void onAdClosed(){
                startGameAd.loadAd(new AdRequest.Builder().build());
                Intent i = new Intent(MainActivity.this, GameActivity.class);
                startActivity(i);
            }
        });

        pref = getApplicationContext().getSharedPreferences(Constants.PREFS_FILE, 0);
        editor = pref.edit();
  }

    public void openOptions(View view) {
        Intent i = new Intent(MainActivity.this, Options.class);
        startActivity(i);

    }

    public void openGame(View view) {
        if(!(pref.contains("dice_style"))){
            editor.putInt("dice_style", 1);
            editor.commit();
        }

        Boolean adFree = pref.getBoolean("adfree", false);
        if(!adFree){
            if (startGameAd.isLoaded()) {
                startGameAd.show();
            }
        }

        Log.d("TAG", "The interstitial wasn't loaded yet.");
        Intent i = new Intent(MainActivity.this, GameActivity.class);
        startActivity(i);
    }

    public void openDiceStyles(View view) {
        Intent i = new Intent(MainActivity.this, DiceSelection.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed(){
        finish();
    }


    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        if(purchases != null && responseCode == BillingClient.BillingResponse.OK){
            for(Purchase purchase : purchases){
                handlePurchase(purchase);
            }

        } else{
            if (responseCode == BillingClient.BillingResponse.USER_CANCELED){
                Toast.makeText(this, "Purchase cancelled", Toast.LENGTH_SHORT).show();
            }else{
                if(responseCode == BillingClient.BillingResponse.ITEM_ALREADY_OWNED){
                    Toast.makeText(this, "You already bought that", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void handlePurchase(Purchase purchase) {
        if (purchase.getSku().equals(ITEM_SKU_ADREMOVAL)) {
            editor.putBoolean("adfree", true).commit();
        }
    }
}
