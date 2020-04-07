package com.tel5027.pigdice.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tel5027.pigdice.Controller.GameLogic;
import com.tel5027.pigdice.R;
import com.tel5027.pigdice.Util.OptionStore;

public class GameActivity extends AppCompatActivity {

    public OptionStore os;
    private GameLogic engine;
    private View gameScreen;

    private TextView pName;
    private TextView pScore;
    private TextView cName;
    private TextView cScore;
    private TextView runScore;

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

        gameScreen = this.findViewById(android.R.id.content);

        runScore = (TextView)findViewById(R.id.runningScoreView);
    }

    public void quitGame(View view) {
        finish();
    }

    public void rollDice(View view) {
        engine.playerTurn(gameScreen);
        runScore.setText(Integer.toString(engine.getRunningScore()));
    }
}
