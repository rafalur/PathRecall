package com.rafal.pathrecall.ui.dialogs;

import android.content.Context;
import android.widget.TextView;

import com.rafal.pathrecall.R;

import butterknife.InjectView;
import butterknife.OnClick;

public class GameOverDialog extends BaseDialog {
    @InjectView(R.id.dialogTitleView)
    TextView mTitleView;
    @InjectView(R.id.dialogSubtitleView)
    TextView mSubtitleView;

    public GameOverDialog(Context context, String title, String subtitle) {
        super(context, R.style.DialogNormal);

        mTitleView.setText(title);
        mSubtitleView.setText(subtitle);
    }

    @OnClick(R.id.dialogOkButton)
    protected void onOkClicked(){
        dismiss();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.game_over_dialog;
    }
}
