package com.tel5027.pigdice.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.adcolony.sdk.*;

import com.tel5027.pigdice.R;
import com.tel5027.pigdice.Util.Constants;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PurchasesUpdatedListener {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private BillingClient billingClient;

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
        skuList.add(Constants.REMOVE_ADS_ITEM);
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

        ImageButton removeAdsButton = findViewById(R.id.remove_ads);
        removeAdsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BillingFlowParams params = BillingFlowParams.newBuilder()
                        .setSku(Constants.REMOVE_ADS_ITEM)
                        .setType(BillingClient.SkuType.INAPP)
                        .build();
                int responseCode = billingClient.launchBillingFlow(MainActivity.this, params);
            }
        });

        AdColonyAppOptions options = new AdColonyAppOptions()
                .setGDPRConsentString("1")
                .setGDPRRequired(true)
                .setKeepScreenOn(true);

        AdColony.configure(this, options, Constants.APP_ID, Constants.NEW_GAME_AD);

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

        boolean adFree = pref.getBoolean("adfree", false);
        if(!adFree){
            AdColonyInterstitialListener listener = new AdColonyInterstitialListener() {
                @Override
                public void onRequestFilled(@org.jetbrains.annotations.NotNull AdColonyInterstitial ad) {

                    ad.show();
                }
            };
            AdColony.requestInterstitial(Constants.NEW_GAME_AD, listener);
        }

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
        if (purchase.getSku().equals(Constants.REMOVE_ADS_ITEM)) {
            editor.putBoolean("adfree", true).commit();
        }
    }
}
