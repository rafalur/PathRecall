package com.rafal.pathrecall.data;

public class Point {

    private int mX;
    private int mY;

    public Point(int x, int y){
        mX = x;
        mY = y;
    }

    public int getY() {
        return mY;
    }

    public void setY(int mY) {
        this.mY = mY;
    }

    public int getX() {
        return mX;
    }

    public void setX(int mX) {
        this.mX = mX;
    }

    @Override
    public String toString(){
        return "(" + mX +", " + mY +")";
    }

    public Point addPoint(Point other){
        int x = 0;
        int y = 0;

        if(mX + other.mX >= 0)
            x = mX + other.mX;

        if(mY + other.mY >= 0)
            y = mY + other.mY;

        return new Point(x,y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Point point = (Point) obj;
        return point.getX() == getX() && point.getY() == getY();
    }
}
