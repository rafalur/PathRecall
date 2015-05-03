package com.rafal.pathrecall.data;

import com.rafal.pathrecall.engine.difficulty.DifficultyProfiler;

public class PathStats {
    private final Path mPath;

    PathStats(Path path){
        mPath = path;
    }

    public int getTurnsNumber() {
        return mPath.getTurnsNumber();
    }

    public int getPointsToGet(DifficultyProfiler profiler) {
        return mPath.getCount() * profiler.getHitPoints();
    }
}
