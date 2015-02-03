package com.rafal.pathrecall.modules;

import android.app.Application;
import android.content.Context;

import com.rafal.pathrecall.GameManager;
import com.rafal.pathrecall.data.Board;
import com.rafal.pathrecall.ui.GameBoardFragment;
import com.rafal.pathrecall.ui.views.GameBoardGridView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module(
        injects = {GameBoardFragment.class, GameBoardGridView.class, GameManager.class},
        includes = Board.class,
        complete = false,
        library = true
)

public class GameObjectsProviderModule {
    private final Application mApplication;

    public GameObjectsProviderModule(Application application){
        this.mApplication = application;
    }

    @Provides
    @Singleton
    public GameManager provideGameManager(){
        return new GameManager();
    }

    @Provides
    public Board getBoard(){
        return new Board();
    }

    @Provides @Singleton @ForApplication Context provideAppContext() {
        return mApplication;
    }
}
