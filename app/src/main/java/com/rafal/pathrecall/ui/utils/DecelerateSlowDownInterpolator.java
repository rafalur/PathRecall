package com.rafal.pathrecall.ui.utils;

import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by Rafal on 2015-02-10.
 */
public class DecelerateSlowDownInterpolator implements Interpolator {
    private AccelerateSlowDownInterpolator mAccelerateInterpolator;

    public DecelerateSlowDownInterpolator(float scaleFactor, float accelerationFactor){
        mAccelerateInterpolator = new AccelerateSlowDownInterpolator(scaleFactor, accelerationFactor);
    }

    @Override
    public float getInterpolation(float input) {
        float accelerateValue = mAccelerateInterpolator.getInterpolation(1 - input);

        return 1 - accelerateValue;
    }
}
