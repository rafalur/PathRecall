package com.rafal.pathrecall.utils;

import android.util.Log;


import com.rafal.pathrecall.data.Board;

import java.util.LinkedList;

public class BoardDrawingOrderHelper {

    private LinkedList<Integer> mNotSelecteViewsList;
    private LinkedList<Integer> mSelectedViewsList;

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

        Log.d("shaper", "Not selected: " + mNotSelecteViewsList.toString());
        Log.d("shaper", "Selected: " + mSelectedViewsList.toString());
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
