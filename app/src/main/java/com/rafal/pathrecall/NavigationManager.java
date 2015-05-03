package com.rafal.pathrecall;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.rafal.pathrecall.engine.difficulty.Difficulty;
import com.rafal.pathrecall.ui.fragments.AboutFragment;
import com.rafal.pathrecall.ui.fragments.BaseFragment;
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
        switchToFragment(MenuFragment.newInstance());
    }

    public void switchToGameBoardFragment(String playerName, Difficulty difficulty){
        switchToFragment(GameBoardFragment.newInstance(playerName, difficulty));
    }

    public void switchToAboutFragment(){
        switchToFragment(AboutFragment.newInstance());
    }

    private void switchToFragment(BaseFragment fragment){
        mFragmentManager.beginTransaction()
                .replace(mFragmentContainerId, fragment)
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
