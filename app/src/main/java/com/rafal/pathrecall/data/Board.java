package com.rafal.pathrecall.data;

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

            notifySelectionStateChanged(x, y, selected);
        }
    }

    public void setBrickSelectionShade(int x, int y, float alpha){
        Brick brick = mBricks[x][y];
        brick.setSelectionShade(alpha);

        notifySelectionStateChanged(x, y, alpha == 1.0 ? true : false);
    }

    private void notifySelectionStateChanged(int x, int y, boolean selected) {
        if (mBoardStateListener != null) {
            mBoardStateListener.onBrickSelectionChanged(x, y, selected);
        }
    }

    public void setBrickSelectionListener(OnBoardStateChangedListener mBrickSelectionListener) {
        this.mBoardStateListener = mBrickSelectionListener;
    }

    public interface OnBoardStateChangedListener {
        public void onBrickSelectionChanged(int x, int y, boolean selected);
        public void onBoardCleared();
    }
}
