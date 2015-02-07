package com.rafal.pathrecall.ui.views;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;

import com.rafal.pathrecall.PathRecallApp;
import com.rafal.pathrecall.R;

import javax.inject.Inject;

public class BrickView extends RelativeLayout {

    private View mContentView;
    @Inject
    ArgbEvaluator mColorEvaluator;

    public BrickView(Context context) {
        super(context);
        init(null, 0);

    }

    public BrickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public BrickView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
        confViews();
    }

    private void init(AttributeSet attrs, int defStyle) {
        View rootView = inflate(getContext(), R.layout.view_brick, this);
        findViews(rootView);

        PathRecallApp.get(getContext()).inject(this);
    }

    private void confViews() {
        setPivotX(getWidth()/2);
        setPivotY(getHeight()/2);
    }

    private void findViews(View root) {
        mContentView = root.findViewById(R.id.contentView);
    }

    public void setSelectionShade(float alpha){
        if(alpha == 1.0){
            playZoomInAnimation();
        }

        GradientDrawable bgShape = (GradientDrawable)mContentView.getBackground();

        Object strokeColor = evaluateStrokeColor(alpha);
        Object color = evaluateBackgroundColor(alpha);
        bgShape.setStroke(2, (Integer) strokeColor);

        bgShape.setColor((Integer)color);
    }

    private void playZoomInAnimation() {
        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(),
                R.animator.brick_scale_down_anim);
        set.setTarget(this);
        set.start();
    }



    public void switchToUserFadedOutSelection(){
        final float endAlpha = 0.4f;

        final ValueAnimator valueAnimator = ValueAnimator.ofObject(new FloatEvaluator(), 1.0f, endAlpha);

        final GradientDrawable background = (GradientDrawable)mContentView.getBackground();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(final ValueAnimator animator) {
                background.setColor((Integer) evaluateBackgroundColor((Float) animator.getAnimatedValue()));
                background.setStroke(2, (Integer) evaluateStrokeColor((Float) animator.getAnimatedValue()));
            }
        });


        valueAnimator.setDuration(getResources().getInteger(R.integer.brick_user_selection_fade_out_duration));
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.start();
    }

    private Object evaluateBackgroundColor(float alpha){
        return mColorEvaluator.evaluate(alpha, getResources().getColor(R.color.brick_background_normal), getResources().getColor(R.color.brick_background_selected));
    }

    private Object evaluateStrokeColor(float alpha){
        return mColorEvaluator.evaluate(alpha, getResources().getColor(R.color.brick_background_normal), getResources().getColor(R.color.white));
    }
}
