package com.rafal.pathrecall.data;

public class Refree {
    public static int countAndAddPointsForPlayer(Player player, Path playerPath, Path playedPath){
        int score = 0;
        for (int i = 0; i < playedPath.getCount(); i++) {
            Point point = playedPath.getPointAt(i);
            if(playerPath.contains(point)){
                score += 10;
            }
            else {
                score -= 10;
            }
        }

        for (int i = 0; i < playerPath.getCount(); i++) {
            Point point = playerPath.getPointAt(i);
            if(!playedPath.contains(point)){
                score -= 10;
            }
        }

        if(score < 0){
            player.onLifeLost();
        }

        player.addPoints(score);
        return score;
    }
}
