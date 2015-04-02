package com.rafal.pathrecall.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.Window;

import com.rafal.pathrecall.NavigationManager;
import com.rafal.pathrecall.PathRecallApp;
import com.rafal.pathrecall.R;
import com.rafal.pathrecall.ui.fragments.GameBoardFragment;
import com.rafal.pathrecall.ui.fragments.MenuFragment;


public class MainActivity extends FragmentActivity {
    NavigationManager mNavigationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        mNavigationManager = new NavigationManager(this, R.id.container);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            mNavigationManager.switchToMenuFragment();
        }
    }

    public NavigationManager getNavigationManager(){
        return mNavigationManager;
    }

    @Override
    public void onBackPressed() {
        if(!mNavigationManager.handleBackPressed()){
            finish();
        }
    }
}
