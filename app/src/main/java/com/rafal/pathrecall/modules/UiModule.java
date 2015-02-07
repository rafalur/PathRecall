package com.rafal.pathrecall.modules;

import android.animation.ArgbEvaluator;

import com.rafal.pathrecall.ui.views.BrickView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {BrickView.class},
        library = true
)
public class UiModule {
    @Provides
    @Singleton
    public ArgbEvaluator provideArgEvaluator(){
        return new ArgbEvaluator();
    }
}
