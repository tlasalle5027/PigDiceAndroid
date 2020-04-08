package com.tel5027.pigdice.Controller;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.tel5027.pigdice.R;
import com.tel5027.pigdice.Util.OptionStore;

import java.util.Random;

public class GameLogic {

    private Random pigroll;
    private OptionStore os;

    private int endScore;
    private int difficulty;
    private int runningScore;
    private int playerScore;
    private int compScore;

    private ImageView diceImage;

    private Button rollButton;
    private Button stayButton;

    public GameLogic(OptionStore o){
        os = o;
        pigroll = new Random();

        difficulty = os.getDifficulty();
        endScore = os.getEndScore();

        runningScore = 0;
        playerScore = 0;
        compScore = 0;
    }

    public int getPlayerScore(){
        return playerScore;
    }

    public int getCompScore(){
        return compScore;
    }

    public int getRunningScore(){
        return runningScore;
    }

    public int turn(){
        return pigroll.nextInt(6) + 1;
    }

    public boolean checkWinner(int pScore, int cScore){
        return (pScore < endScore && cScore < endScore);
    }

    public void changeDiceImage(View v, int roll){
        diceImage = (ImageView)v.findViewById(R.id.diceImage);
        switch(roll){
            case 1:
                diceImage.setImageResource(R.drawable.die_1);
                break;
            case 2:
                diceImage.setImageResource(R.drawable.die_2);
                break;
            case 3:
                diceImage.setImageResource(R.drawable.die_3);
                break;
            case 4:
                diceImage.setImageResource(R.drawable.die_4);
                break;
            case 5:
                diceImage.setImageResource(R.drawable.die_5);
                break;
            case 6:
                diceImage.setImageResource(R.drawable.die_6);
                break;
        }
    }

    public int playerTurn(View v){
        int roll = 0;

        rollButton = (Button)v.findViewById(R.id.rollButton);
        stayButton = (Button)v.findViewById(R.id.stayButton);

        roll = turn();
        stayButton.setEnabled(true);

        changeDiceImage(v, roll);

        if(roll == 1){
            rollButton.setEnabled(false);
            roll = 0;
            runningScore = 0;
        }

        runningScore += roll;

        return roll;
    }

    public void endTurn(View v){
        stayButton = (Button)v.findViewById(R.id.stayButton);

        stayButton.setEnabled(false);
        playerScore += runningScore;
        runningScore = 0;

        //checkWinner(playerScore, compScore);
        computerTurn(v);
    }

    public void computerTurn(View v){
        rollButton = (Button)v.findViewById(R.id.rollButton);
        int roll = 0;

        switch(difficulty){
            case 1:
                for(int i = 1; i <= 3; i++){
                    roll = turn();
                    if(roll == 1){
                        changeDiceImage(v, roll);
                        runningScore = 0;
                        break;
                    }
                    changeDiceImage(v, roll);
                    runningScore += roll;
                }
                break;
            case 2:
                if(playerScore <= compScore){
                    for(int i = 1; i <= 3; i++){
                        roll = turn();
                        if(roll == 1){
                            changeDiceImage(v, roll);
                            runningScore = 0;
                            break;
                        }
                        changeDiceImage(v, roll);
                        runningScore += roll;
                    }
                }
                else{
                    while(playerScore > compScore){
                        roll = turn();
                        if(roll == 1){
                            changeDiceImage(v, roll);
                            runningScore = 0;
                            break;
                        }
                        changeDiceImage(v, roll);
                        runningScore += roll;
                    }
                }
                break;
            case 3:
                break;
        }
        compScore += runningScore;
        runningScore = 0;

        //checkWinner(playerScore, compScore);
        rollButton.setEnabled(true);

    }

}
