package com.rafal.pathrecall.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.rafal.pathrecall.NavigationManager;
import com.rafal.pathrecall.ui.MainActivity;

/**
 * Created by Rafal on 2015-04-02.
 */
public class BaseFragment extends Fragment{
    public NavigationManager getNavigationManager(){
        return ((MainActivity)getActivity()).getNavigationManager();
    }
}
