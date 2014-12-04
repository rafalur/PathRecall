package com.rafal.pathrecall.ui.views;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.rafal.pathrecall.R;

public class BrickView extends RelativeLayout {

    private View mContentView;
    private View mSelectionOverlay;

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
        mSelectionOverlay = root.findViewById(R.id.slectionOverlay);
    }

    public void setSelectionShade(float alpha){
        if(alpha == 1.0){
            playZoomInAnimation();
        }

        mSelectionOverlay.getBackground().setAlpha((int)(255 * alpha));
    }

    private void playZoomInAnimation() {
        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(),
                R.animator.brick_scale_down_anim);
        set.setTarget(this);
        set.start();
    }
}
