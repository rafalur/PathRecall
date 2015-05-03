package com.rafal.pathrecall.engine.difficulty;

public abstract class DifficultyProfiler {
    protected int mHitPoints;
    protected int mMissPoints;
    protected int mTurnsNumber;
    protected int mVelocity;

    public abstract void updateForLevel(int level);
    public abstract void resetToDefault();

    public int getHitPoints() {
        return mHitPoints;
    }

    public int getMissPoints() {
        return mMissPoints;
    }

    public int getTurnsNumber() {
        return mTurnsNumber;
    }

    public int getVelocity() {
        return mVelocity;
    }
}
