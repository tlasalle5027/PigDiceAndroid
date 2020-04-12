package com.tel5027.pigdice.Controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.tel5027.pigdice.R;
import com.tel5027.pigdice.Util.OptionStore;

import java.util.Random;

public class GameLogic {

    private Random pigroll;

    private String pName;
    private String cName;

    private int endScore;
    private int difficulty;
    private int runningScore;
    private int playerScore;
    private int compScore;

    private boolean hardCompDecision;

    private Button rollButton;
    private Button stayButton;

    public GameLogic(OptionStore o){
        pigroll = new Random();

        difficulty = o.getDifficulty();
        endScore = o.getEndScore();
        pName = o.getName();

        switch(o.getDifficulty()){
            case 1:
                cName = "Earl";
                break;
            case 2:
                cName = "Ian";
                break;
            case 3 :
                cName = "Harry";
                break;
        }

        runningScore = 0;
        playerScore = 0;
        compScore = 0;

        hardCompDecision = true;


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

    private boolean checkWinner(){
        return (playerScore > endScore || compScore > endScore);
    }

    private void winnerDialog(Context ctx){

        if(playerScore > compScore){
            new AlertDialog.Builder(ctx, android.R.style.Theme_Material_Dialog_Alert)
                    .setTitle("You Win!")
                    .setMessage(pName + " has won!")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
        }
        else{
            new AlertDialog.Builder(ctx, android.R.style.Theme_Material_Dialog_Alert)
                    .setTitle(cName + " wins!")
                    .setMessage(cName + " has won!")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

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
        rollButton = v.findViewById(R.id.rollButton);
        stayButton = v.findViewById(R.id.stayButton);

        stayButton.setEnabled(false);
        playerScore += runningScore;
        runningScore = 0;

        if(checkWinner()){
            rollButton.setEnabled(false);
            stayButton.setEnabled(false);
            winnerDialog(v.getContext());
        }
        else{
            computerTurn(v);
        }

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
                while(hardCompDecision){
                    double prob = 0;
                    roll = turn();
                    if(roll == 1){
                        prob = Math.random();
                        if(prob >= .08){
                            changeDiceImage(v, roll);
                            runningScore = 0;
                            break;
                        }
                        else{
                            roll = 2;
                        }
                    }
                    changeDiceImage(v, roll);
                    runningScore += roll;
                    if((compScore + runningScore >= endScore) || (runningScore >= 25)
                     || (((playerScore - (compScore + runningScore) < 18))) &&
                    runningScore >= 15){
                        hardCompDecision = false;
                        break;
                    }
                }
                break;
        }
        compScore += runningScore;
        runningScore = 0;
        hardCompDecision = true;

        if(checkWinner()){
            rollButton.setEnabled(false);
            stayButton.setEnabled(false);
            winnerDialog(v.getContext());
        }
        else{
            rollButton.setEnabled(true);
        }

    }

}
