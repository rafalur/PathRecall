package com.rafal.pathrecall;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rafal.pathrecall.engine.Player;

import java.lang.reflect.Type;
import java.util.List;

public class AppPreferences {
    private static final String KEY_PREFS = "PATH_RECALL_PREFS";
    private static final String KEY_PLAYERS = "KEY_PLAYERS";
    private static final String KEY_DEFAULT_PLAYER = "KEY_DEFAULT_PLAYERS";

    SharedPreferences mPreferences;

    public AppPreferences(Context context){
        mPreferences = context.getSharedPreferences(KEY_PREFS, Context.MODE_PRIVATE);
    }

    public void storeDefaultPlayer(String player){
        storeString(KEY_DEFAULT_PLAYER, player);
    }

    public String getDefaultPlayer(){
        return mPreferences.getString(KEY_DEFAULT_PLAYER, null);
    }

    private void storeString(String key, String stringToStore) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(key, stringToStore);
        editor.commit();
    }

    public void storePlayers(List<Player> players){
        Gson gson = new Gson();
        String jsonPlayers = gson.toJson(players);
        storeString(KEY_PLAYERS, jsonPlayers);
    }

    public List<Player> getPlayers(){
        String jsonPlayers = mPreferences.getString(KEY_PLAYERS, null);
        if(jsonPlayers == null){
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<Player>>(){}.getType();
        List<Player> players = gson.fromJson(jsonPlayers, type);

        return players;
    }
}
