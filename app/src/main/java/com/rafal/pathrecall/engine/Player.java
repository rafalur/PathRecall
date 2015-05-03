package com.rafal.pathrecall.engine;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import javax.inject.Inject;

public class Player {
    private int mScore;
    private int mLostLives;

    @SerializedName("name")
    private String mName;

    public Player(String name){
        mName = name;
    }

    public int getScore() {
        return mScore;
    }

    public void addPoints(int points){
        mScore += points;
        Log.d("Path", "Player points added: " + points);
    }

    public int getLostLives(){
        return mLostLives;
    }

    public void onLifeLost(){
        mLostLives++;
    }

    public String getName() {
        return mName;
    }
}
