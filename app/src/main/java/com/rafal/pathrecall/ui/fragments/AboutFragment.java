package com.rafal.pathrecall.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rafal.pathrecall.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rafal on 2015-04-03.
 */
public class AboutFragment extends BaseFragment {
    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.inject(this, root);
        confViews();
        return root;
    }

    private void confViews() {
    }
}
