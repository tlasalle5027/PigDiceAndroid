package com.tel5027.pigdice.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import com.tel5027.pigdice.MyApplication;
import com.tel5027.pigdice.R;
import com.tel5027.pigdice.Util.Constants;

import org.solovyev.android.checkout.ActivityCheckout;
import org.solovyev.android.checkout.Billing;
import org.solovyev.android.checkout.BillingRequests;
import org.solovyev.android.checkout.Checkout;
import org.solovyev.android.checkout.EmptyRequestListener;
import org.solovyev.android.checkout.Inventory;
import org.solovyev.android.checkout.ProductTypes;
import org.solovyev.android.checkout.Purchase;

import javax.annotation.Nonnull;

public class Options extends AppCompatActivity {

    private Editor prefEditor;

    public EditText name;
    public EditText pTwoName;

    private int difficulty = 1;
    private int finalScore = 100;

    private final ActivityCheckout mCheckout = Checkout.forActivity(this, MyApplication.get().getBilling());
    private Inventory mInventory;

    private class PurchaseListener extends EmptyRequestListener<Purchase> {
        @Override
        public void onSuccess(Purchase purchase) {
            prefEditor.putBoolean("remove_ads", true);
        }

        @Override
        public void onError(int response, Exception e) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    e.getMessage(),
                    Toast.LENGTH_SHORT);

            toast.show();
        }
    }

    private class InventoryCallback implements Inventory.Callback {
        @Override
        public void onLoaded(Inventory.Products products) {
            // your code here
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options2);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.PREFS_FILE, 0);
        prefEditor = pref.edit();

        mCheckout.start();

        mCheckout.createPurchaseFlow(new PurchaseListener());

        mInventory = mCheckout.makeInventory();
        mInventory.load(Inventory.Request.create()
                .loadAllPurchases()
                .loadSkus(ProductTypes.IN_APP, "remove_ads"), new InventoryCallback());

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView oAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        oAdView.loadAd(adRequest);
    }

    @Override
    protected void onDestroy() {
        mCheckout.stop();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCheckout.onActivityResult(requestCode, resultCode, data);
    }

    public void saveOptions(View view) {
        name = findViewById(R.id.nameText);
        pTwoName = findViewById(R.id.pTwoNameText);

        prefEditor.putString("player_one_name", name.getText().toString());
        prefEditor.putString("player_two_name", pTwoName.getText().toString());
        prefEditor.putInt("difficulty", difficulty);
        prefEditor.putInt("end_score", finalScore);
        prefEditor.commit();

        finish();
    }

    public void clearOptions(View view) {
        RadioButton easyButton = findViewById(R.id.easyButton);
        RadioButton hundredButton = findViewById(R.id.oneHundredPoints);
        name = findViewById(R.id.nameText);
        pTwoName = findViewById(R.id.pTwoNameText);
        name.setText("");
        pTwoName.setText("");
        easyButton.toggle();
        hundredButton.toggle();
    }

    public void difficultyRadioButtonClick(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.easyButton:
                if (checked)
                    difficulty = 1;
                    break;
            case R.id.intButton:
                if (checked)
                    difficulty = 2;
                    break;
            case R.id.hardButton:
                if(checked)
                    difficulty = 3;
                    break;
            case R.id.pvpButton:
                if(checked)
                    difficulty = 4;
                    break;
        }
    }

    public void scoreRadioButtonClick(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.fiftyPoints:
                if (checked)
                    finalScore = 50;
                break;
            case R.id.oneHundredPoints:
                if (checked)
                    finalScore = 100;
                break;
            case R.id.oneHundredFiftyPoints:
                if (checked)
                    finalScore = 150;
                break;
            case R.id.twoHundredPoints:
                if (checked)
                    finalScore = 200;
                break;
            case R.id.marathonMode:
                if (checked)
                    finalScore = 500;
                break;
        }
    }

    public void removeAds(View view) {
        mCheckout.whenReady(new Checkout.EmptyListener() {
            @Override
            public void onReady(BillingRequests requests) {
                requests.purchase(ProductTypes.IN_APP, "remove_ads", null, mCheckout.getPurchaseFlow());
            }
        });
    }
}
