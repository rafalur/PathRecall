package com.rafal.pathrecall.modules;

import android.app.Application;

import com.rafal.pathrecall.NavigationManager;
import com.rafal.pathrecall.PathRecallApp;
import com.rafal.pathrecall.ui.MainActivity;
import com.rafal.pathrecall.ui.fragments.MenuFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        includes = {
//                GameEngineModule.class,
                UiModule.class,
        },
        injects = {
                PathRecallApp.class,
        },
        library = true
)
public final class AppModule {
    private final PathRecallApp app;

    public AppModule(PathRecallApp app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return app;
    }
}
