package com.rafal.pathrecall.data;

enum Direction{
    LEFT(new Point(-1, 0)),
    UP(new Point(0, -1)),
    RIGHT(new Point(1, 0)),
    DOWN(new Point(0, 1));

    private final Point mDiffPoint;

    Direction(Point diffPoint){
        mDiffPoint = diffPoint;
    }

    public boolean isDirectionPerpendicular(Direction otherDirection){
        return Math.abs(mDiffPoint.getX() - otherDirection.mDiffPoint.getX()) % 2 > 0;
    }

    public final Point getDiffPoint(){
        return mDiffPoint;
    }
}
