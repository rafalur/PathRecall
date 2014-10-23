package com.rafal.pathrecall.data;

import android.util.Log;

public class Board {
    public static final int BOARD_SIZE = 10;

    private Brick[][] mBricks;
    private OnBoardStateChangedListener mBoardStateListener;

    public Board(){
        mBricks = new Brick[BOARD_SIZE][BOARD_SIZE];

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                mBricks[i][j] = new Brick();
            }
        }
    };

    public void clear(){
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                setBrickSelected(i, j, false);
            }
        }

        mBoardStateListener.onBoardCleared();
    }

    public Brick getBrick(int x, int y){
        return mBricks[x][y];
    }

    public void setBrickSelected(int x, int y, boolean selected){
        Brick brick = mBricks[x][y];

        if(brick.isSelected() != selected) {
            brick.setSelected(selected);
            setBrickSelectionShade(x, y, selected ? 1.0f : 0.0f);
            notifySelectionChanged(x, y, selected);
        }
    }

    public void setBrickSelectionShade(int x, int y, float alpha){
        Brick brick = mBricks[x][y];
        brick.setSelectionShade(alpha);
        notifySelectionShadeChanged(x, y, alpha);
    }

    public int getSelectedBricksCount(){
        int counter = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if(getBrick(i, j).isSelected()){
                    ++counter;
                }
            }
        }
        return counter;
    }

    private void notifySelectionChanged(int x, int y, boolean selected) {
        if (mBoardStateListener != null) {
            mBoardStateListener.onBrickSelectionChanged(x, y, selected);
        }
    }

    private void notifySelectionShadeChanged(int x, int y, float alpha) {
        if (mBoardStateListener != null) {
            mBoardStateListener.onBrickSelectionShadeChanged(x, y, alpha);
        }
    }

    public void setBrickSelectionListener(OnBoardStateChangedListener mBrickSelectionListener) {
        this.mBoardStateListener = mBrickSelectionListener;
    }

    public interface OnBoardStateChangedListener {
        public void onBrickSelectionChanged(int x, int y, boolean selected);
        public void onBrickSelectionShadeChanged(int x, int y, float alpha);
        public void onBoardCleared();
    }
}
