package com.rafal.pathrecall.ui.fragments;

import android.support.v4.app.Fragment;

import com.rafal.pathrecall.NavigationManager;
import com.rafal.pathrecall.ui.MainActivity;

public class BaseFragment extends Fragment{
    public NavigationManager getNavigationManager(){
        return ((MainActivity)getActivity()).getNavigationManager();
    }
}
