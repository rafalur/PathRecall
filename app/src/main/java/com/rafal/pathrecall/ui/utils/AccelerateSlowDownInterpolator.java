package com.rafal.pathrecall.ui.utils;

import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;


public class AccelerateSlowDownInterpolator implements Interpolator {
    private final float mScaleFactor;
    private final float mMaxValue;

    private DecelerateInterpolator mDecelerateInterpolator;

    public AccelerateSlowDownInterpolator(float scaleFactor, float accelerationFactor){
        mScaleFactor = scaleFactor;
        mDecelerateInterpolator = new DecelerateInterpolator(accelerationFactor);
        mMaxValue = mDecelerateInterpolator.getInterpolation(mScaleFactor);
    }

    @Override
    public float getInterpolation(float input) {

        float innerIterpolatorInput = input * mScaleFactor;

        float innerIterpolatorValue = mDecelerateInterpolator.getInterpolation(innerIterpolatorInput);

        return innerIterpolatorValue / mMaxValue;
    }
}
