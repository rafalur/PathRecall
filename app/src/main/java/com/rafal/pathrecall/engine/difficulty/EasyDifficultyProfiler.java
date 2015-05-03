package com.rafal.pathrecall.engine.difficulty;

import com.rafal.pathrecall.engine.GameParameters;

/**
 * Created by Rafal on 2015-05-02.
 */
public class EasyDifficultyProfiler extends DifficultyProfiler {
    @Override
    public void updateForLevel(int level) {
        mHitPoints = GameParameters.DEFAULT_HIT_POINTS + (level - 1)/2 * 5;
        mMissPoints = GameParameters.DEFAULT_MISS_POINTS/2 - (level - 1)/2 * 5;

        mTurnsNumber = GameParameters.DEFAULT_TURNS_NUMBER + (level - 1)/5;
        mVelocity = GameParameters.DEFAULT_VELOCITY + (level - 1)/5;
    }

    @Override
    public void resetToDefault() {
        mHitPoints = GameParameters.DEFAULT_HIT_POINTS;
        mMissPoints = GameParameters.DEFAULT_MISS_POINTS/2;
        mTurnsNumber = GameParameters.DEFAULT_TURNS_NUMBER;
        mVelocity = GameParameters.DEFAULT_VELOCITY;
    }
}
