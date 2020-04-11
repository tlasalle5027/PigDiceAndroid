package com.tel5027.pigdice.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

        pName = (TextView)findViewById(R.id.playerName);
        pScore = (TextView)findViewById(R.id.playerScore);
        cName = (TextView)findViewById(R.id.compName);
        cScore = (TextView)findViewById(R.id.compScore);
        runScore = (TextView)findViewById(R.id.runningScoreView);

        pName.setText(os.getName());

        switch(os.getDifficulty()){
            case 1:
                cName.setText("Earl");
                break;
            case 2:
                cName.setText("Ian");
                break;
            case 3 :
                cName.setText("Harry");
                break;
        }
    }

    public void quitGame(View view) {
        finish();
    }

    public void rollDice(View view) {
        engine.playerTurn(gameScreen);
        runScore.setText(Integer.toString(engine.getRunningScore()));
    }

    public void endTurn(View view){
        engine.endTurn(gameScreen);
        runScore.setText(Integer.toString(engine.getRunningScore()));
        pScore.setText(Integer.toString(engine.getPlayerScore()));
        cScore.setText(Integer.toString(engine.getCompScore()));
    }

}
