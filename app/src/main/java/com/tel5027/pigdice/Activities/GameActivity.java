package com.tel5027.pigdice.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tel5027.pigdice.Controller.GameLogic;
import com.tel5027.pigdice.R;
import com.tel5027.pigdice.Util.Constants;

public class GameActivity extends AppCompatActivity {

    private GameLogic engine;
    private View gameScreen;

    private TextView pScore;
    private TextView cScore;
    private TextView runScore;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        SharedPreferences os = getApplicationContext().getSharedPreferences(Constants.PREFS_FILE, 0);

        ImageView diceImage = findViewById(R.id.diceImage);

        switch(os.getInt("dice_style", -1)){
            case 1:
                diceImage.setImageResource(R.drawable.die_1);
                break;
            case 2:
                diceImage.setImageResource(R.drawable.die_1_002);
                break;
            case 3:
                diceImage.setImageResource(R.drawable.die_1_003);
                break;
        }

        engine = new GameLogic(os);

        gameScreen = this.findViewById(android.R.id.content);

        TextView pName = findViewById(R.id.playerName);
        pScore = findViewById(R.id.playerScore);
        TextView cName = findViewById(R.id.compName);
        cScore = findViewById(R.id.compScore);
        runScore = findViewById(R.id.runningScoreView);

        pName.setText(os.getString("player_one_name", null));

        switch(os.getInt("difficulty", 0)){
            case 1:
                cName.setText("Earl");
                break;
            case 2:
                cName.setText("Ian");
                break;
            case 3 :
                cName.setText("Harry");
                break;
            case 4:
                cName.setText(os.getString("player_two_name", null));
                break;
        }
    }

    public void quitGame(View view) {
        finish();
    }

    @SuppressLint("SetTextI18n")
    public void rollDice(View view) {
        engine.playerTurn(gameScreen);
        runScore.setText(Integer.toString(engine.getRunningScore()));
    }

    @SuppressLint("SetTextI18n")
    public void endTurn(View view){
        engine.endTurn(gameScreen);
        runScore.setText(Integer.toString(engine.getRunningScore()));
        pScore.setText(Integer.toString(engine.getPlayerScore()));
        cScore.setText(Integer.toString(engine.getCompScore()));
    }

}
