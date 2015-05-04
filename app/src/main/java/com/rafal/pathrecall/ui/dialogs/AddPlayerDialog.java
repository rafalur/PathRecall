package com.rafal.pathrecall.ui.dialogs;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.rafal.pathrecall.R;
import com.rafal.pathrecall.ui.views.ValidatedEditText;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

public class AddPlayerDialog extends BaseDialog {
    @InjectView(R.id.addPlayerEditText)
    ValidatedEditText mPlayerEdit;
    @InjectView(R.id.alreadyExistsErrorView)
    TextView mAlreadyExistsErrorView;

    private final List<String> mPlayersNames;
    private Drawable mErrorIcon;
    private boolean mCancelled;

    public AddPlayerDialog(Context context, List<String> existingPlayersNames) {
        super(context, R.style.DialogNormal);
        mPlayersNames = existingPlayersNames;
        confViews();
    }

    private void confViews() {
        mErrorIcon = getContext().getResources().getDrawable(R.drawable.ic_error);
        mErrorIcon.setBounds(0, 0, mErrorIcon.getIntrinsicWidth(), mErrorIcon.getIntrinsicHeight());

        mPlayerEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                showAlreadyExistsError(playerAlreadyExists(editable.toString()));
            }
        });
    }

    private void showAlreadyExistsError(boolean show) {
        mPlayerEdit.setError(null, show ? mErrorIcon : null);
        mAlreadyExistsErrorView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    private boolean playerAlreadyExists(String playerName) {
        if(mPlayersNames == null){
            return false;
        }
        for (String name : mPlayersNames){
            if(name.equalsIgnoreCase(playerName)){
                return true;
            }
        }
        return false;
    }

    @OnClick(R.id.addPlayerOkButton)
    protected void onOkClicked(){
        dismiss();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.add_player_dialog;
    }

    public String getPlayerName(){
        if(mCancelled){
            return null;
        }

        String playerName = mPlayerEdit.getText().toString();
        if(playerName == null || playerName.isEmpty() || playerAlreadyExists(playerName)){
            return null;
        }

        return playerName;
    }

    @Override
    public void onBackPressed() {
        mCancelled = true;
        super.onBackPressed();
    }
}
