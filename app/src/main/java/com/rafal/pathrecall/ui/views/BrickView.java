package com.rafal.pathrecall.ui.views;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.rafal.pathrecall.R;

public class BrickView extends RelativeLayout {

    private View mContentView;
    //private View mSelectionOverlay;

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
    }

    private void confViews() {
        setPivotX(getWidth()/2);
        setPivotY(getHeight()/2);
    }

    private void findViews(View root) {
        mContentView = root.findViewById(R.id.contentView);
        //mSelectionOverlay = root.findViewById(R.id.slectionOverlay);
    }

    public void setSelectionShade(float alpha){
        if(alpha == 1.0){
            playZoomInAnimation();
        }

        GradientDrawable bgShape = (GradientDrawable)mContentView.getBackground();

        Object strokeColor = new ArgbEvaluator().evaluate(alpha, 0xFF808080, 0xFFFFFFFF);
        Object color = new ArgbEvaluator().evaluate(alpha, 0xFF808080, 0xFF660005);
        bgShape.setStroke(2, (Integer) strokeColor);


        bgShape.setColor((Integer)color);
    }

    private void playZoomInAnimation() {
        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(),
                R.animator.brick_scale_down_anim);
        set.setTarget(this);
        set.start();
    }

    public void switchToUserSelesection(){
        final ValueAnimator valueAnimator = ValueAnimator.ofObject(new ArgbEvaluator(),
                0xFFFFFFFF,
                0xff78c5f9);

        final GradientDrawable background = (GradientDrawable) this.getBackground();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(final ValueAnimator animator) {
                background.setColor((Integer) animator.getAnimatedValue());
            }

        });
        valueAnimator.setDuration(1000);
        valueAnimator.start();
    }
}
