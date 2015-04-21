package com.rafal.pathrecall;

import android.app.Application;
import android.content.Context;

import com.rafal.pathrecall.modules.AppModule;

import dagger.ObjectGraph;

public class PathRecallApp extends Application {
    private static ObjectGraph mApplicationGraph;
    private AppPreferences mPreferences;

    @Override public void onCreate() {
        super.onCreate();

        mPreferences = new AppPreferences(getBaseContext());
        buildObjectGraphAndInject();
    }

    public static ObjectGraph getObjectGraph(){
        return mApplicationGraph;
    }

    public void buildObjectGraphAndInject() {
        mApplicationGraph = ObjectGraph.create(new AppModule(this));
        mApplicationGraph.inject(this);
    }

    public void inject(Object o) {
        mApplicationGraph.inject(o);
    }

    public ObjectGraph createScopedGraph(Object... modules) {
        mApplicationGraph = mApplicationGraph.plus(modules);
        return mApplicationGraph;
    }

    public void restoreMainObjectGraph() {
        mApplicationGraph = ObjectGraph.create(new AppModule(this));
    }

    public static PathRecallApp get(Context context) {
        return (PathRecallApp) context.getApplicationContext();
    }

    public AppPreferences getPreferences() {
        return mPreferences;
    }
}
