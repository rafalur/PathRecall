package com.rafal.pathrecall.data;

import android.util.Log;

import javax.inject.Inject;

public class Player {
    private int mScore;

    @Inject
    Player(){}

    public int getScore() {
        return mScore;
    }

    public void addPoints(int points){
        mScore += points;
        Log.d("Path", "Player points added: " + points);
    }
}
