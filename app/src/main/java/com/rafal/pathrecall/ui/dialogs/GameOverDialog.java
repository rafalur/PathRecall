package com.rafal.pathrecall.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rafal.pathrecall.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class GameOverDialog extends Dialog {
    @InjectView(R.id.dialogTitleView)
    TextView mTitleView;
    @InjectView(R.id.dialogSubtitleView)
    TextView mSubtitleView;

    public GameOverDialog(Context context, String title, String subtitle) {
        super(context, R.style.GameOverDialog);
        setContentView(R.layout.game_over_dialog);
        ButterKnife.inject(this, this);

        mTitleView.setText(title);
        mSubtitleView.setText(subtitle);
    }

    @OnClick(R.id.dialogOkButton)
    protected void onOkClicked(){
        dismiss();
    }
}
