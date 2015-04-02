package com.rafal.pathrecall.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rafal.pathrecall.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuFragment extends BaseFragment {

    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_menu, container, false);
        ButterKnife.inject(this, root);
        confViews();
        return root;
    }

    private void confViews() {
    }

    @OnClick({ R.id.menuNewGameButton, R.id.menuAboutButton, R.id.menuExitButton })
    protected void handleClicks(Button button) {
        switch (button.getId()) {
            case R.id.menuNewGameButton:
                getNavigationManager().switchToGameBoardFragment();
                break;
            case R.id.menuAboutButton:
                break;
            case R.id.menuExitButton:
                getActivity().finish();
                break;
        }
    }
}
