package com.rafal.pathrecall.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Path {
    private static final Random RND = new Random();
    public static final int MIN_SECTION_LENGHT = 3;

    private List<Point> mPoints;

    public Path(){
        mPoints = new ArrayList<Point>();
    }

    public void addPoint(Point point){
        mPoints.add(point);
    }

    public Point getPointAt(int index){
        if(index < mPoints.size()){
            return mPoints.get(index);
        }
        return null;
    }

    public Point getLastPoint(){
        if(mPoints != null){
            return mPoints.get(mPoints.size() - 1);
        }

        return null;
    }

    public void clear(){
        mPoints.clear();
    }

    public int getCount() {
        return mPoints.size();
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();

        int i;
        for (i = 0; i < mPoints.size() - 1; i++) {
            builder.append(mPoints.get(i).toString()).append(", ");
        }
        builder.append(mPoints.get(i).toString());

        return builder.toString();
    }


    public static Path generateRandomPath(int turnsNumber){
        Path path = new Path();

        Point startPoint;
        Direction direction = null;
        Direction oldDirection;
        int availableSpace;
        int length;

        for (int i = 0; i < turnsNumber; i++) {
            boolean isLastSection = (i == turnsNumber - 1);
            if(i == 0){
                direction = getRandomDirection();
                startPoint = getRandomStartPoint(direction);
                availableSpace = getAvailableSpaceInDirection(direction, startPoint);
            }
            else{
                startPoint = path.getLastPoint();
                oldDirection = direction;

                do {
                    direction = getRandomDirection();
                    availableSpace = getAvailableSpaceInDirection(direction, startPoint);
                }while (!oldDirection.isDirectionPerpendicular(direction) || availableSpace < MIN_SECTION_LENGHT);
            }

            length = RND.nextInt(availableSpace - MIN_SECTION_LENGHT + 1) + MIN_SECTION_LENGHT;

            generatePathSection(path, direction, startPoint, isLastSection ? availableSpace : length, i == 0);
        }

        Log.d("Path", "Path: " + path);

        return path;
    }

    private static void generatePathSection(Path path, Direction dir, Point startPoint, int length, boolean includeStartPoint) {
        if(includeStartPoint) {
            path.addPoint(startPoint);
        }

        for (int i = 0; i < length; i++) {
            path.addPoint(path.getLastPoint().addPoint(dir.getDiffPoint()));
        }
    }

    private static Point getRandomStartPoint(Direction dir){
        int diffX = dir.getDiffPoint().getX();
        int diffY = dir.getDiffPoint().getY();

        return new Point(convertDiffCoordToBoundaryCoord(diffX), convertDiffCoordToBoundaryCoord(diffY));
    }

    private static int convertDiffCoordToBoundaryCoord(int diffCoord){
        int boundryCoord;

        if(diffCoord > 0)
            boundryCoord = 0;
        else if(diffCoord < 0)
            boundryCoord = 9;
        else
            boundryCoord = RND.nextInt(9);

        return boundryCoord;
    }

    private static Direction getRandomDirection(){
        RandomEnum<Direction> r = new RandomEnum<Direction>(Direction.class);
        return r.random();
    }

    private static int getAvailableSpaceInDirection(Direction direction, Point point){
        Log.d("Path", "Counting space for: " + direction + " X:" + point.getX() + ", Y: " + point.getY());

        Point directionDiffPoint = direction.getDiffPoint();

        return getSpaceAlongDirectionPointedByDiffCoord(directionDiffPoint.getX(), point.getX()) +
               getSpaceAlongDirectionPointedByDiffCoord(directionDiffPoint.getY(), point.getY());
    }

    private static int getSpaceAlongDirectionPointedByDiffCoord(int diffCoord, int pointCoord){
        int ret = 0;
        if(diffCoord > 0){
            ret = 9 - pointCoord;
        }
        else if(diffCoord < 0){
            ret = pointCoord;
        }
        return ret;
    }

    private static class RandomEnum<E extends Enum> {
        private final E[] values;
        public RandomEnum(Class<E> token) {
            values = token.getEnumConstants();
        }
        public E random() {
            return values[RND.nextInt(values.length)];
        }
    }
}
