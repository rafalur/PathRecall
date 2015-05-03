package com.rafal.pathrecall.engine.difficulty;

public enum Difficulty {
    EASY ("Easy"),
    NORMAL ("Normal"),
    HARD ("Hard");

    private final String mDescription;

    Difficulty(String description) {
        mDescription = description;
    }

    @Override
    public String toString() {
        return mDescription;
    }
}
