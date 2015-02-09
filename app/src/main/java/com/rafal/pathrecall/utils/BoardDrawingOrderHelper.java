package com.rafal.pathrecall.utils;

import android.util.Log;


import com.rafal.pathrecall.data.Board;

import java.util.LinkedList;

import javax.inject.Inject;

public class BoardDrawingOrderHelper {

    private LinkedList<Integer> mNotSelecteViewsList;
    private LinkedList<Integer> mSelectedViewsList;

    @Inject
    public BoardDrawingOrderHelper(){
        init();
    }

    private void init() {
        mSelectedViewsList = new LinkedList<Integer>();
        mNotSelecteViewsList = new LinkedList<Integer>();

        for (int i = 0; i < Board.BOARD_SIZE * Board.BOARD_SIZE; i++) {
            mNotSelecteViewsList.add(i);
        }
    }

    public void select(int index){
        mSelectedViewsList.add(index);

        if(mSelectedViewsList.size() == 8){
            Integer polled = mSelectedViewsList.poll();
            mNotSelecteViewsList.add(polled);
        }

        mNotSelecteViewsList.remove(new Integer(index));
//        Log.d("shaper", "Not selected: " + mNotSelecteViewsList.toString());
//        Log.d("shaper", "Selected: " + mSelectedViewsList.toString());
    }

    public void deselect(int index){
        if(mSelectedViewsList.contains(index)){
            mSelectedViewsList.remove(Integer.valueOf(index));
            if(!mNotSelecteViewsList.contains(index)) {
                mNotSelecteViewsList.add(index);
            }
        }
    }

    public int getDrawingOrderForIndex(int index){
        if(index < mNotSelecteViewsList.size()) {
            return mNotSelecteViewsList.get(index);
        }
        else{
            return mSelectedViewsList.get(index - mNotSelecteViewsList.size());
        }
    }

    public void reset(){
        init();
    }
}
