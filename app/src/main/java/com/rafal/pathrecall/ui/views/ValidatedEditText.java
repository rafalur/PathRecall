package com.rafal.pathrecall.ui.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.EditText;

public class ValidatedEditText extends EditText {
    public ValidatedEditText(Context context) {
        super(context);
    }

    public ValidatedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ValidatedEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setError(CharSequence pError, Drawable pIcon) {
        setCompoundDrawables(null, null, pIcon, null);
    }
}
