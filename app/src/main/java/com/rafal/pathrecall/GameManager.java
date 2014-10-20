package com.rafal.pathrecall;

import android.os.Handler;

import com.rafal.pathrecall.data.Board;
import com.rafal.pathrecall.data.Path;
import com.rafal.pathrecall.data.Point;


public class GameManager {
    public static final int BOARD_SIZE = 10;
    public static final int PATH_STEP_DELAY_MILLIS = 80;
    public static final int PATH_TAIL_LENGTH = 7;

    private static GameManager mInstance;

    Handler mPathPlayTimerHandler = new Handler();

    private int mPathStepIndex;

    Runnable mPathPlayRunnable = new Runnable() {
        public void run() {
            playPathStep(mPathStepIndex);
        }
    };

    private Board mBoard;
    private Path mPath;
    private boolean mIsPathPlaying;

    private GameManager(){
        mBoard = new Board();
    };

    public static synchronized GameManager instance(){
        if(mInstance == null)
            mInstance = new GameManager();
        return mInstance;
    }

    private boolean mIsDrawingEnabled;

    public void enableDrawing(boolean enable) {
        mIsDrawingEnabled = enable;
    }

    public boolean isDrawingEnabled() {
        return mIsDrawingEnabled;
    }

    public Board getBoard() {
        return mBoard;
    }

    public void playPath(){
        if(!mIsPathPlaying) {
            mPath = Path.generateRandomPath(4);
            mIsPathPlaying = true;
            mPathPlayRunnable.run();
        }
    }

    public boolean isPathPlaying(){
        return mIsPathPlaying;
    }

    public void playPathStep(int index)
    {
        if(index < mPath.getCount() + PATH_TAIL_LENGTH ) {
            mPathPlayTimerHandler.postDelayed(mPathPlayRunnable, PATH_STEP_DELAY_MILLIS);
            mPathStepIndex++;

            selectFirstPointOfTail(index);
            markTailInnerPointsShaded(index);
            deselectLastPointOfTail(index);
        }
        else{
            mIsPathPlaying = false;
            mPathStepIndex = 0;
        }
    }

    private void markTailInnerPointsShaded(int index) {
        for (int i = 1; i < PATH_TAIL_LENGTH + 1; i++) {
            if(index - i >= 0 && index - i < mPath.getCount()){
                Point tailPoint = mPath.getPointAt(index - i);
                float alpha = (float)(PATH_TAIL_LENGTH - i)/PATH_TAIL_LENGTH;
                mBoard.setBrickSelectionShade(tailPoint.getX(), tailPoint.getY(), alpha);
            }
        }
    }

    private void deselectLastPointOfTail(int index) {
        if(index > PATH_TAIL_LENGTH){
            Point pointToDeselect = mPath.getPointAt(index - PATH_TAIL_LENGTH );
            mBoard.setBrickSelected(pointToDeselect.getX(), pointToDeselect.getY(), false);
        }
    }

    private void selectFirstPointOfTail(int index) {
        if(index < mPath.getCount()) {
            Point point = mPath.getPointAt(index);
            mBoard.setBrickSelected(point.getX(), point.getY(), true);
        }
    }
}
