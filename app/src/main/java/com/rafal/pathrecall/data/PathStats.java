package com.rafal.pathrecall.data;

/**
 * Created by Rafal on 2014-12-04.
 */
public class PathStats {
    private final Path mPath;

    PathStats(Path path){
        mPath = path;
    }

    public int getTurnsNumber() {
        return mPath.getTurnsNumber();
    }

    public int getPointsToGet() {
        //TODO: Get score multiplier from separate object
        return mPath.getCount() * 10;
    }
}
