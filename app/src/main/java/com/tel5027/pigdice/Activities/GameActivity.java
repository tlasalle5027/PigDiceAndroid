package com.tel5027.pigdice.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.tel5027.pigdice.Controller.GameLogic;
import com.tel5027.pigdice.R;
import com.tel5027.pigdice.Util.OptionStore;

public class GameActivity extends AppCompatActivity {

    public OptionStore os;
    private GameLogic engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if(Options.os == null){
            os = new OptionStore();
        }
        else{
            os = Options.os;
        }

        engine = new GameLogic(os);
    }

    public void quitGame(View view) {
        finish();
    }

    public void rollDice(View view) {
        engine.playerTurn(view);
    }
}
