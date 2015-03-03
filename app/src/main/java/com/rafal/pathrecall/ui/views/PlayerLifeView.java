package com.rafal.pathrecall.ui.views;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;

import com.rafal.pathrecall.PathRecallApp;
import com.rafal.pathrecall.R;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PlayerLifeView extends RelativeLayout {

    @InjectView(R.id.contentView)
    View mContentView;

    public PlayerLifeView(Context context) {
        super(context);
        init(null, 0);

    }

    public PlayerLifeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PlayerLifeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
        confViews();
    }

    private void init(AttributeSet attrs, int defStyle) {
        View rootView = inflate(getContext(), R.layout.view_player_life, this);
        ButterKnife.inject(this, rootView);
    }

    private void confViews() {
        setPivotX(getWidth()/2);
        setPivotY(getHeight()/2);
    }

    public void setSelected(boolean selected){
        ((GradientDrawable)mContentView.getBackground()).setColor(
                getResources().getColor(selected ? R.color.brick_background_selected : R.color.button_regular_background_pressed));
    }
}
