package com.rafal.pathrecall.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.rafal.pathrecall.R;

public class RemainingLivesView extends LinearLayout{
    public RemainingLivesView(Context context) {
        super(context);
        init(null, 0);
    }

    public RemainingLivesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public RemainingLivesView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RemainingLivesView, defStyle, 0);

        int livesNumber = a.getInteger(R.styleable.RemainingLivesView_livesNumber, 0);

        a.recycle();

        setLivesNumber(livesNumber);
    }

    public void setLivesNumber(int livesNumber){
        if(livesNumber > 0){
            removeAllViews();
            populateLivesViews(livesNumber);
        }
    }

    private void populateLivesViews(int livesNumber) {
        for(int i = 0; i < livesNumber; i++){
            PlayerLifeView view = new PlayerLifeView(getContext());
            int viewSize = getResources().getDimensionPixelOffset(R.dimen.life_view_size);
            int offset = getResources().getDimensionPixelOffset(R.dimen.offset_normal);
            LinearLayout.LayoutParams params = new LayoutParams(viewSize, viewSize);
            if(i > 0) {
                params.setMargins(offset, 0, 0, 0);
            }
            view.setLayoutParams(params);
            addView(view);
        }
    }
}
