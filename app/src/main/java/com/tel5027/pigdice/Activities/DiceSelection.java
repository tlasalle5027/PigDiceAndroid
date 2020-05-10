package com.tel5027.pigdice.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.tel5027.pigdice.R;
import com.tel5027.pigdice.Util.Constants;

public class DiceSelection extends AppCompatActivity {

    private SharedPreferences pref;
    private Editor prefEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice_selection);

        pref = getApplicationContext().getSharedPreferences(Constants.PREFS_FILE, 0);
        prefEdit = pref.edit();
    }

    public void selectDice(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.dice001Button:
                if (checked)
                    prefEdit.putInt("dice_style", 1);
                break;
            case R.id.dice002Button:
                if (checked)
                    prefEdit.putInt("dice_style", 2);
                break;
        }

        prefEdit.commit();

    }
}
