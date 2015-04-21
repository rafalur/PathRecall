package com.rafal.pathrecall.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewStub;
import android.widget.RelativeLayout;

import com.rafal.pathrecall.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public abstract class BaseDialog extends Dialog {

    @InjectView(R.id.baseLayoutRoot)
    RelativeLayout mRootLayout;

    ViewStub mContentStub;

    public BaseDialog(Context context) {
        super(context);
        init();
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    private void init() {
        setContentView(R.layout.base_dialog);
        mContentStub = (ViewStub)findViewById(R.id.baseLayoutContentStub);
        inflate();
        ButterKnife.inject(this, getWindow().getDecorView());
    }

    private View inflate(){
        mContentStub.setLayoutResource(getLayoutId());
        return mContentStub.inflate();
    }

    protected abstract int getLayoutId();
}
