package com.tel5027.pigdice.Controller;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.tel5027.pigdice.R;
import com.tel5027.pigdice.Util.OptionStore;

import java.util.Random;

public class GameLogic {

    private Random pigroll;

    private int endScore;
    private int difficulty;
    private int runningScore;
    private int playerScore;
    private int compScore;

    private Button rollButton;
    private Button stayButton;

    public GameLogic(OptionStore o){
        pigroll = new Random();

        difficulty = o.getDifficulty();
        endScore = o.getEndScore();

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

    private int turn(){
        return pigroll.nextInt(6) + 1;
    }

    public boolean checkWinner(int pScore, int cScore){
        return (pScore < endScore && cScore < endScore);
    }

    private void changeDiceImage(View v, int roll){
        ImageView diceImage = v.findViewById(R.id.diceImage);
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

    public void playerTurn(View v){
        int roll;

        rollButton = v.findViewById(R.id.rollButton);
        stayButton = v.findViewById(R.id.stayButton);

        roll = turn();
        stayButton.setEnabled(true);

        changeDiceImage(v, roll);

        if(roll == 1){
            rollButton.setEnabled(false);
            roll = 0;
            runningScore = 0;
        }

        runningScore += roll;
    }

    public void endTurn(View v){
        stayButton = v.findViewById(R.id.stayButton);

        stayButton.setEnabled(false);
        playerScore += runningScore;
        runningScore = 0;

        //checkWinner(playerScore, compScore);
        computerTurn(v);
    }

    private void computerTurn(View v){
        rollButton = v.findViewById(R.id.rollButton);
        int roll;

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
