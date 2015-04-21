package com.rafal.pathrecall.engine;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PlayersManager {
    private List<Player> mPlayers;

    @Inject
    public PlayersManager(){
    }

    public List<Player> getPlayers() {
        return mPlayers;
    }
    public static List<String> getPlayersNamesFromPlayersList(List<Player> players) {
        if(players == null || players.size() == 0){
            return null;
        }
        List<String> names = new ArrayList<String>();

        for (Player player : players){
            names.add(player.getName());
        }

        return names;
    }

    public void addPlayer(Player player){
        initializePlayersIfNecessary();
        mPlayers.add(player);
    }

    private void initializePlayersIfNecessary() {
        if(mPlayers == null){
            mPlayers = new ArrayList<Player>();
        }
    }

    public void addPlayers(List<Player> players){
        initializePlayersIfNecessary();
        mPlayers.addAll(players);
    }
}
