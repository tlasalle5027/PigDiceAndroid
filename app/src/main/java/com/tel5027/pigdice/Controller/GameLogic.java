package com.tel5027.pigdice.Controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    private boolean playerTwoTurn;

    private Button rollButton;
    private Button stayButton;

    private TextView pOneName;
    private TextView pTwoName;


    public GameLogic(OptionStore o){
        pigroll = new Random();

        difficulty = o.getDifficulty();
        endScore = o.getEndScore();
        pName = o.getName();

        playerTwoTurn = false;

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
            case 4:
                cName = o.getPlayerTwoName();
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
        return (playerScore >= endScore || compScore >= endScore);
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
        pOneName   = v.findViewById(R.id.playerName);
        pTwoName   = v.findViewById(R.id.compName);

        roll = turn();
        stayButton.setEnabled(true);

        changeDiceImage(v, roll);

        if(roll == 1){
            rollButton.setEnabled(false);
            roll = 0;
            runningScore = 0;
        }

        runningScore += roll;

        if(!playerTwoTurn){
            pOneName.setTextColor(Color.GREEN);
            pTwoName.setTextColor(Color.BLACK);
        }
        else{
            pOneName.setTextColor(Color.BLACK);
            pTwoName.setTextColor(Color.GREEN);
        }
    }

    public void endTurn(View v){
        rollButton = v.findViewById(R.id.rollButton);
        stayButton = v.findViewById(R.id.stayButton);

        if(!playerTwoTurn){
            stayButton.setEnabled(false);
            playerScore += runningScore;
            runningScore = 0;
        }
        else{
            stayButton.setEnabled(false);
            compScore += runningScore;
            runningScore = 0;
        }

        if(checkWinner()){
            rollButton.setEnabled(false);
            stayButton.setEnabled(false);
            winnerDialog(v.getContext());
        }
        else{
            if(!playerTwoTurn){
                computerTurn(v);
            }
            else{
                playerTwoTurn = false;
                rollButton.setEnabled(true);
            }
        }
    }

    private void computerTurn(View v){
        rollButton = v.findViewById(R.id.rollButton);
        int roll;

        switch(difficulty){
            case 1:
                for(int i = 1; i <= 2; i++){
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
                else if(playerScore - (compScore + runningScore) < 18){
                    while(runningScore < 10){
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
                    while(playerScore >= compScore){
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
                    double prob;
                    roll = turn();
                    if(roll == 1){
                        prob = Math.random();
                        if(prob <= .09){
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
                     || (((playerScore - (compScore + runningScore) < 18)) &&
                            (runningScore >= 15))){
                        hardCompDecision = false;
                        break;
                    }
                }
                break;
            case 4:
                playerTwoTurn = true;
                break;
        }

        if(!(difficulty == 4)) {
            compScore += runningScore;
            runningScore = 0;
            hardCompDecision = true;
        }

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
