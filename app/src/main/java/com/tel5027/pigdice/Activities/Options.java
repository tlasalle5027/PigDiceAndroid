package com.tel5027.pigdice.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdSize;
import com.adcolony.sdk.AdColonyAdView;
import com.adcolony.sdk.AdColonyAdViewListener;

import com.tel5027.pigdice.R;
import com.tel5027.pigdice.Util.Constants;

public class Options extends AppCompatActivity {

    private Editor prefEditor;

    public EditText name;
    public EditText pTwoName;

    private int difficulty = 1;
    private int finalScore = 100;

    private AdColonyAdView adview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options2);
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.PREFS_FILE, 0);
        prefEditor = pref.edit();

        AdColony.configure(this, Constants.APP_ID, Constants.OPTIONS_AD);

        boolean adFree = pref.getBoolean("adfree", false);
        if (!adFree) {

            final RelativeLayout adContainer = findViewById(R.id.ad_container);

            AdColonyAdViewListener listener = new AdColonyAdViewListener() {
                @Override
                public void onRequestFilled(AdColonyAdView ad) {
                    /** Add this ad object to whatever layout you have set up for this placement */
                    adview = ad;
                    adContainer.addView(adview);
                }
            };

            AdColony.requestAdView(Constants.OPTIONS_AD, listener, AdColonyAdSize.BANNER);
        }
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
        switch (view.getId()) {
            case R.id.easyButton:
                if (checked)
                    difficulty = 1;
                break;
            case R.id.intButton:
                if (checked)
                    difficulty = 2;
                break;
            case R.id.hardButton:
                if (checked)
                    difficulty = 3;
                break;
            case R.id.pvpButton:
                if (checked)
                    difficulty = 4;
                break;
        }
    }

    public void scoreRadioButtonClick(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
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

    @Override
    protected void onDestroy() {
        if (adview != null) {
            adview.destroy();
        }
        super.onDestroy();
    }
}
