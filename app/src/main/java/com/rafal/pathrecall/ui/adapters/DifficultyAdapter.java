package com.rafal.pathrecall.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rafal.pathrecall.R;
import com.rafal.pathrecall.engine.difficulty.Difficulty;

import java.util.Arrays;
import java.util.List;

public class DifficultyAdapter extends ArrayAdapter<Difficulty> {

    public DifficultyAdapter(Context context){
        super(context, R.layout.players_spinner_row, Arrays.asList(Difficulty.values()));
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }


    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View row=inflater.inflate(R.layout.players_spinner_row, parent, false);
        TextView label=(TextView)row.findViewById(R.id.playerNameTextView);
        label.setText(getItem(position).toString());

        return row;
    }
}



