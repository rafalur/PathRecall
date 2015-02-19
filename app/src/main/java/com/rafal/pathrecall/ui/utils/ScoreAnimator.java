package com.rafal.pathrecall.ui.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

public class ScoreAnimator {
    public static final int DURATION = 1000;

    public static final float SCALE_START = 0.6f;
    public static final float SCALE_END = 1.0f;

    public static final float ALPHA_FADED_IN = 1.0f;
    public static final float ALPHA_FADED_OUT = 0;

    public static final float ACCELERATION_FACTOR = 2.5f;
    public static final float ACCELERATION_SCALE_FACTOR = 0.4f;

    public static final float FADE_ACCELERATE_FACTOR = 1.2f;

    private ScoreAnimationListener mAnimationListener;

    public void playFloatInOutAnimation(View target, Context context){
        int screenWidth = UiUtils.getScreenSize(context).x;
        ObjectAnimator floatInAnimator = ObjectAnimator.ofFloat(target, "translationX", -screenWidth/2 - target.getWidth(), 0);
        ObjectAnimator floatOutAnimator = ObjectAnimator.ofFloat(target, "translationX", 0, screenWidth/2 + target.getWidth());

        ObjectAnimator fadeInAnimator = ObjectAnimator.ofFloat(target, "alpha", ALPHA_FADED_OUT, ALPHA_FADED_IN);
        ObjectAnimator fadeOutAnimator = ObjectAnimator.ofFloat(target, "alpha", ALPHA_FADED_IN, ALPHA_FADED_OUT);
        floatInAnimator.setInterpolator(new AccelerateSlowDownInterpolator(ACCELERATION_SCALE_FACTOR, ACCELERATION_FACTOR));
        floatOutAnimator.setInterpolator(new DecelerateSlowDownInterpolator(ACCELERATION_SCALE_FACTOR, ACCELERATION_FACTOR));

        fadeInAnimator.setInterpolator(new DecelerateInterpolator(FADE_ACCELERATE_FACTOR));
        fadeOutAnimator.setInterpolator(new AccelerateInterpolator(FADE_ACCELERATE_FACTOR));

        Interpolator zoomInInterpolator = new DecelerateInterpolator();
        Interpolator zoomOutInterpolator = new AccelerateInterpolator();

        ObjectAnimator zoomInXAnimator = ObjectAnimator.ofFloat(target, "scaleX", SCALE_START, SCALE_END);
        ObjectAnimator zoomInYAnimator = ObjectAnimator.ofFloat(target, "scaleY", SCALE_START, SCALE_END);
        zoomInXAnimator.setInterpolator(zoomInInterpolator);
        zoomInYAnimator.setInterpolator(zoomInInterpolator);
        ObjectAnimator zoomOutXAnimator = ObjectAnimator.ofFloat(target, "scaleX", SCALE_END, SCALE_START);
        ObjectAnimator zoomOutYAnimator = ObjectAnimator.ofFloat(target, "scaleY", SCALE_END, SCALE_START);
        zoomOutXAnimator.setInterpolator(zoomOutInterpolator);
        zoomOutYAnimator.setInterpolator(zoomOutInterpolator);

        AnimatorSet inAnimatorSet = new AnimatorSet();
        inAnimatorSet.play(floatInAnimator).with(fadeInAnimator).with(zoomInXAnimator).with(zoomInYAnimator);

        AnimatorSet outAnimatorSet = new AnimatorSet();
        outAnimatorSet.play(floatOutAnimator).with(fadeOutAnimator).with(zoomOutXAnimator).with(zoomOutYAnimator);

        inAnimatorSet.setDuration(DURATION);
        outAnimatorSet.setDuration(DURATION);

        AnimatorSet mainSet = new AnimatorSet();
        mainSet.play(inAnimatorSet).before(outAnimatorSet);

        mainSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(mAnimationListener != null){
                    mAnimationListener.onAnimationEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        mainSet.start();
    }

    public void setAnimationListener(ScoreAnimationListener listener){
        mAnimationListener = listener;
    }

    public interface ScoreAnimationListener{
        public void onAnimationEnd();
    }
}
