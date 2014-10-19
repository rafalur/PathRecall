package com.rafal.pathrecall.data;

public class Brick {
    private boolean mIsSelected;
    private float mSelectionShade;

    public boolean isSelected() {
        return mIsSelected;
    }

    public void setSelected(boolean selected) {
        this.mIsSelected = selected;
    }

    public void setSelectionShade(float alpha){
        mSelectionShade = alpha;
    }

    public float getSelectionShade() {
        return mSelectionShade;
    }
}
