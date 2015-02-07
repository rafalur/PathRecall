package com.rafal.pathrecall;

import com.rafal.pathrecall.data.Board;
import com.rafal.pathrecall.data.Path;
import com.rafal.pathrecall.data.Point;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

public class PathPlayer {
    public static final int PATH_STEP_DELAY_MILLIS = 80;
    public static final int PATH_TAIL_LENGTH = 7;

    private int mPathStepIndex;;

    private Board mBoard;
    private Path mPath;
    private boolean mIsPathPlaying;
    private Timer mPathPlayTimer;

    private PathPlayerStateListener mStateListener;

    @Inject
    public PathPlayer(Board board){
        mBoard = board;
    }

    public void loadPath(Path path){
        mPath = path;
    }

    public void playPath(){
        if(!mIsPathPlaying && mPath != null) {
            mIsPathPlaying = true;
            mPathPlayTimer = new Timer();
            mPathPlayTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    playPathStep(mPathStepIndex);
                }
            }, 0, PATH_STEP_DELAY_MILLIS);
        }
    }

    public boolean isPathPlaying(){
        return mIsPathPlaying;
    }

    private synchronized void playPathStep(int index)
    {
        //Log.d("Path", "Play path step: " + index);
        if(index < mPath.getCount() + PATH_TAIL_LENGTH ) {
            mPathStepIndex++;

            selectFirstPointOfTail(index);
            markTailInnerPointsShaded(index);
            deselectLastPointOfTail(index);
        }
        else{
            mPathPlayTimer.cancel();
            mIsPathPlaying = false;
            mPathStepIndex = 0;
            notifyPathFinished();
        }
    }

    private void notifyPathFinished() {
        if(mStateListener != null){
            mStateListener.onPathPlayFinished();
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
        if(index >= PATH_TAIL_LENGTH){
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

    public void setStateListener(PathPlayerStateListener stateListener) {
        this.mStateListener = stateListener;
    }

    public interface PathPlayerStateListener{
        public void onPathPlayFinished();
    }
}
