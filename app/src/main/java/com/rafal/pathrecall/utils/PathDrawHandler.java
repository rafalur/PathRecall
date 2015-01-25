package com.rafal.pathrecall.utils;

import android.util.Log;
import android.view.MotionEvent;

import com.rafal.pathrecall.GameManager;
import com.rafal.pathrecall.data.Board;
import com.rafal.pathrecall.data.Brick;
import com.rafal.pathrecall.data.Path;


public class PathDrawHandler {
    private int mBrickSize;

    private enum DrawAction{
        NONE,
        DRAW,
        CLEAR
    }

    public class EventPoint{
        public EventPoint(float _x, float _y){
            x = _x;
            y = _y;
        }
        public float x;
        public float y;
    }

    private Board mBoard;
    private Path mUserPath;
    private EventPoint mLastEventPoint;
    private PathDrawSimulatedEventListener mSimulatedEventsListener;

    private DrawAction mCurrentDrawAction;

    public PathDrawHandler(Board board){
        mBoard = board;
        mUserPath = new Path();
    }

    public void reset(){
        mUserPath.clear();
    }

    public void setBrickSize(int size){
        mBrickSize = size;
    }

    public void setSimulatedEventsListener(PathDrawSimulatedEventListener mSimulatedEventsListener) {
        this.mSimulatedEventsListener = mSimulatedEventsListener;
    }

    public void handleTouchOnBrick(int x, int y, MotionEvent event, boolean simulated){
        if( GameManager.instance().isDrawingEnabled()) {
            if(mLastEventPoint != null && !simulated ){
                simulateInBetweenEventsIfNeeded(event.getX(), event.getY());
            }

            if(x >= 0 && y >= 0) {
                Brick brick = mBoard.getBrick(x, y);
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mCurrentDrawAction = brick.isSelected() ? DrawAction.CLEAR : DrawAction.DRAW;
                }
                if (mCurrentDrawAction == DrawAction.CLEAR || mCurrentDrawAction == DrawAction.DRAW) {
                    mBoard.setBrickSelected(x, y, mCurrentDrawAction == DrawAction.DRAW);
                    //mUserPath.addPoint(new Point(x, y));
                }
            }

            if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_OUTSIDE){
                mCurrentDrawAction = DrawAction.NONE;
                mLastEventPoint = null;
            }

            if(!simulated) {
                storeLastEventPoint((int) event.getX(), (int) event.getY());
            }
        }
    }

    private void simulateInBetweenEventsIfNeeded(float x, float y) {
        generateSimulatedEventsIfNeeded(mLastEventPoint.x, mLastEventPoint.y, x, y);
    }

    private void generateSimulatedEventsIfNeeded(float startX, float startY, float endX, float endY) {
        float diffX = Math.abs(endX - startX);
        float diffY = Math.abs(endY - startY);

        if(diffX == 0 && diffY ==0){
            return;
        }

        boolean invertedAxises = diffY > diffX;

        if(invertedAxises){
            float temp = startX;
            startX = startY;
            startY = temp;
            temp = endX;
            endX = endY;
            endY = temp;
        }

        LinearFunctionParameters interpolationFunctionParams = LinearFunctionParameters.getParametersFromTwoPoints(startX, startY, endX, endY);

        float pointToSimualteX = startX;
        float pointToSimualteY = startY;

        while( Math.abs(endX - pointToSimualteX) >= 30 ){
            int sign = (int) Math.signum(endX - pointToSimualteX);

            float newPointToSimulateX = pointToSimualteX + sign * 30;
            float newPointToSimulateY = interpolationFunctionParams.a*newPointToSimulateX + interpolationFunctionParams.b;

            pointToSimualteX = newPointToSimulateX;
            pointToSimualteY = newPointToSimulateY;

            EventPoint point = getInterpolationEventPoint(pointToSimualteX, pointToSimualteY, invertedAxises);

            triggerSimulatedMotionEvent(point);
        }
    }

    private EventPoint getInterpolationEventPoint(float pointToSimualteX, float pointToSimualteY, boolean invertedAxises) {
        EventPoint point;

        if(invertedAxises){
            point = new EventPoint(pointToSimualteY, pointToSimualteX);
        }
        else{
            point = new EventPoint(pointToSimualteX, pointToSimualteY);
        }
        return point;
    }

    private void triggerSimulatedMotionEvent(EventPoint point) {
        MotionEvent event = MotionEvent.obtain( 0, 0, MotionEvent.ACTION_MOVE, point.x ,point.y, 0);

        if(mSimulatedEventsListener != null){
            mSimulatedEventsListener.onSimulatedEvent(event);
        }
    }

    private void storeLastEventPoint(float x, float y) {
        if(mLastEventPoint == null){
            mLastEventPoint = new EventPoint(x,y);
        }
        else {
            mLastEventPoint.x = x;
            mLastEventPoint.y = y;
        }
    }

    public interface PathDrawSimulatedEventListener{
        public void onSimulatedEvent(MotionEvent event);
    }
}
