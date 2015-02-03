package com.rafal.pathrecall;

import android.app.Application;

import com.rafal.pathrecall.modules.GameObjectsProviderModule;

import dagger.ObjectGraph;

/**
 * Created by Rafal on 2015-01-31.
 */
public class PathRecallApp extends Application {
    private static ObjectGraph mApplicationGraph;

    @Override public void onCreate() {
        super.onCreate();

        mApplicationGraph = ObjectGraph.create(new GameObjectsProviderModule(this));
    }

    public static ObjectGraph getObjectGraph(){
        return mApplicationGraph;
    }
}
