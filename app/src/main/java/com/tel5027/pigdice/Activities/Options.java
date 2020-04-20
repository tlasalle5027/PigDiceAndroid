package com.tel5027.pigdice.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.tel5027.pigdice.R;
import com.tel5027.pigdice.Util.OptionStore;

public class Options extends AppCompatActivity {
    
    public static OptionStore os;

    public EditText name;
    public EditText pTwoName;
    private int difficulty = 1;
    private int finalScore = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options2);
        os = new OptionStore();
    }

    public void saveOptions(View view) {
        name = (EditText)findViewById(R.id.nameText);
        pTwoName = (EditText)findViewById(R.id.pTwoNameText);
        os.setName(name.getText().toString());
        os.setPlayerTwoName(pTwoName.getText().toString());
        os.setDifficulty(difficulty);
        os.setEndScore(finalScore);

        finish();
    }

    public void clearOptions(View view) {
        RadioButton easyButton = (RadioButton)findViewById(R.id.easyButton);
        RadioButton hundredButton = (RadioButton)findViewById(R.id.oneHundredPoints);
        name = (EditText)findViewById(R.id.nameText);
        pTwoName = (EditText)findViewById(R.id.pTwoNameText);
        name.setText("");
        pTwoName.setText("");
        easyButton.toggle();
        hundredButton.toggle();

        os.setName("PigDice");
        os.setPlayerTwoName("PigTwo");
        os.setDifficulty(1);
        os.setEndScore(100);
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
}
