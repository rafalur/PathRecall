package com.rafal.pathrecall.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rafal.pathrecall.R;
import com.rafal.pathrecall.engine.Player;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PlayersAdapter extends ArrayAdapter<Player> {
    private final List<Player> mPlayers;

    public PlayersAdapter(Context context, int resource, List<Player> objects) {
        super(context, resource, objects);
        mPlayers = objects;
        sortPlayers();
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public void add(Player object) {
        super.add(object);
        sortPlayers();
    }

    private void sortPlayers() {
        Collections.sort(mPlayers, new Comparator<Player>() {
            @Override
            public int compare(Player player1, Player player2) {
                return player1.getName().compareToIgnoreCase(player2.getName());
            }
        });
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View row=inflater.inflate(R.layout.players_spinner_row, parent, false);
        TextView label=(TextView)row.findViewById(R.id.playerNameTextView);
        label.setText(getItem(position).getName());

        return row;
    }

    public List<Player> getPlayers() {
        return mPlayers;
    }

    // find and returns player with matching name, if no one matches null returned
    public Integer getIndexOfPlayer(String playerName){
        if(mPlayers == null){
            return null;
        }

        for(int i = 0;  i < mPlayers.size(); i++){
            Player player = mPlayers.get(i);
            if(playerName.equalsIgnoreCase(player.getName())){
                return i;
            }
        }
        return null;
    }
}
