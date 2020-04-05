package com.tel5027.pigdice.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.tel5027.pigdice.R;
import com.tel5027.pigdice.Util.OptionStore;

public class Options extends AppCompatActivity {
    
    public OptionStore os;

    public EditText name;
    public int difficulty = 1;
    public int finalScore = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options2);
        
        os = new OptionStore();
    }

    public void saveOptions(View view) {
        name = view.findViewById(R.id.nameText);
        os.setName(name.getText().toString());
    }

    public void clearOptions(View view) {
        name = view.findViewById(R.id.nameText);
        name.setText("");
    }

    public void difficultyRadioButtonClick(View view) {
    }

    public void scoreRadioButtonClick(View view) {
    }
}
