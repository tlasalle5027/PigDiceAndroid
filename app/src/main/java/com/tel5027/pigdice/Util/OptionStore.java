package com.tel5027.pigdice.Util;

public class OptionStore {
    private String name;
    private int difficulty;
    private int endScore;

    public OptionStore(){
        name = "PigDice";
        difficulty = 0;
        endScore=100;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getEndScore() {
        return endScore;
    }

    public void setEndScore(int endScore) {
        this.endScore = endScore;
    }
}
