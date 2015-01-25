package com.rafal.pathrecall.data;

import android.util.Log;

public class Player {
    private int mScore;

    public int getScore() {
        return mScore;
    }

    public void addPoints(int points){
        mScore += points;
        Log.d("Path", "Player points added: " + points);
    }
}
