package com.tel5027.pigdice.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

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
                    prefEdit.putInt("dice_style", 1).commit();
                break;
            case R.id.dice002Button:
                if (checked)
                    prefEdit.putInt("dice_style", 2).commit();
                break;
            case R.id.dice003Button:
                if (checked)
                    prefEdit.putInt("dice_style", 3).commit();
                break;
            case R.id.dice004Button:
                if (checked)
                    prefEdit.putInt("dice_style", 4).commit();
                break;
            case R.id.dice005Button:
                if (checked)
                    if(pref.contains("dice_005_owned")){
                        ((RadioButton)view).setText(R.string.dice_005);
                        prefEdit.putInt("dice_style", 5).commit();
                    }
                    else if(pref.getInt("PigPoints", -1) >= 200){
                        String bText = getString(R.string.dice_005);
                        purchaseMessage(this, view,200, 5, "dice_005_owned", bText);
                    }
                    else{
                        Toast.makeText(this, "Not enough PigPoints!", Toast.LENGTH_SHORT).show();
                        prefEdit.putInt("PigPoints", 2000).commit();
                    }
                break;
        }

    }

    public void purchaseMessage(Context ctx, final View view, final int pigPoints, final int diceStyle, final String prefKey, final String buttonText){
        new AlertDialog.Builder(ctx, android.R.style.Theme_Material_Dialog_Alert)
                .setTitle("Confirm purchase")
                .setMessage("Purchase this dice for " + Integer.toString(pigPoints) + " PigPoints?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int currentPoints = pref.getInt("PigPoints", -1);
                        prefEdit.putInt("PigPoints", (currentPoints - pigPoints)).commit();
                        prefEdit.putBoolean(prefKey, true).commit();
                        ((RadioButton)view).setText(buttonText);
                        prefEdit.putInt("dice_style", diceStyle).commit();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
