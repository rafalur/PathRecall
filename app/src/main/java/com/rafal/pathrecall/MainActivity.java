package com.rafal.pathrecall;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;


public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, GameBoardFragment.newInstance("", ""))
                    .commit();
        }
    }
}
