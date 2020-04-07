package com.tel5027.pigdice.Controller;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.tel5027.pigdice.R;
import com.tel5027.pigdice.Util.OptionStore;

import java.util.Random;

public class GameLogic {

    private Random pigroll;
    private OptionStore os;
    private int endScore;
    private int difficulty;

    public GameLogic(OptionStore o){
        os = o;
        pigroll = new Random();

        difficulty = os.getDifficulty();
        endScore = os.getEndScore();
    }

    public int turn(){
        return pigroll.nextInt(6) + 1;
    }

    public boolean checkWinner(int pScore, int cScore){
        return (pScore < endScore && cScore < endScore);
    }

    public int playerTurn(View v){
        int roll = 0;
        ImageView diceImage = (ImageView)v.findViewById(R.id.diceImage);

        roll = turn();

        switch(roll){
            case 1:
                diceImage.setImageResource(R.drawable.die_1);
                roll = 0;
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

         return roll;

    }

}
