package com.rafal.pathrecall;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.rafal.pathrecall.ui.fragments.GameBoardFragment;
import com.rafal.pathrecall.ui.fragments.MenuFragment;

public class NavigationManager {
    private final int mFragmentContainerId;
    private FragmentManager mFragmentManager;

    public NavigationManager(FragmentActivity activity, int fragmentContainerId){
        mFragmentManager = activity.getSupportFragmentManager();
        mFragmentContainerId = fragmentContainerId;
    }

    public void switchToMenuFragment(){
        mFragmentManager.beginTransaction()
                .replace(mFragmentContainerId, MenuFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    public void switchToGameBoardFragment(){
        mFragmentManager.beginTransaction()
                .replace(mFragmentContainerId, GameBoardFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    public boolean handleBackPressed() {
        if(mFragmentManager.getBackStackEntryCount() > 1) {
            mFragmentManager.popBackStack();
            return true;
        }

        return false;
    }
}
