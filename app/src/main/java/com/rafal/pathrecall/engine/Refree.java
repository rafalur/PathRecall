package com.rafal.pathrecall.engine;

import com.rafal.pathrecall.data.Path;
import com.rafal.pathrecall.data.Point;

public class Refree {
    public static int countAndAddPointsForPlayer(Player player, Path playerPath, Path playedPath, DifficultyProfiler pointsProfiler){
        int score = 0;
        for (int i = 0; i < playedPath.getCount(); i++) {
            Point point = playedPath.getPointAt(i);
            if(playerPath.contains(point)){
                score += pointsProfiler.getHitPoints();
            }
            else {
                score += pointsProfiler.getMissPoints();
            }
        }

        for (int i = 0; i < playerPath.getCount(); i++) {
            Point point = playerPath.getPointAt(i);
            if(!playedPath.contains(point)){
                score += pointsProfiler.getMissPoints();
            }
        }

        if(score < 0){
            player.onLifeLost();
        }

        player.addPoints(score);
        return score;
    }
}
