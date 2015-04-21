package com.rafal.pathrecall.ui.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.rafal.pathrecall.PathRecallApp;
import com.rafal.pathrecall.R;
import com.rafal.pathrecall.engine.Player;
import com.rafal.pathrecall.engine.PlayersManager;
import com.rafal.pathrecall.ui.dialogs.AddPlayerDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MenuFragment extends BaseFragment {

    @InjectView(R.id.menuPlayersSpinner)
    Spinner mPlayersSpinner;


    PlayersAdapter mPlayersAdapter;

    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_menu, container, false);
        ButterKnife.inject(this, root);
        confViews();
        return root;
    }

    private void confViews() {
        List<Player> players = PathRecallApp.get(getActivity()).getPreferences().getPlayers();
        if(players == null){
            players = new ArrayList<Player>();
        }
        mPlayersAdapter = new PlayersAdapter(getActivity(), R.id.playerNameTextView, players);
        mPlayersSpinner.setAdapter(mPlayersAdapter);
    }

    @OnClick({ R.id.menuStartGameButton, R.id.menuAboutButton, R.id.menuExitButton })
    protected void handleClicks(Button button) {
        switch (button.getId()) {
            case R.id.menuStartGameButton:
                getNavigationManager().switchToGameBoardFragment();
                break;
            case R.id.menuAboutButton:
                getNavigationManager().switchToAboutFragment();
                break;
            case R.id.menuExitButton:
                getActivity().finish();
                break;
        }
    }

    private class PlayersAdapter extends ArrayAdapter<Player>{
        private final List<Player> mPlayers;

        public PlayersAdapter(Context context, int resource, List<Player> objects) {
            super(context, resource, objects);
            mPlayers = objects;
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

            LayoutInflater inflater = getLayoutInflater(null);
            View row=inflater.inflate(R.layout.players_spinner_row, parent, false);
            TextView label=(TextView)row.findViewById(R.id.playerNameTextView);
            label.setText(getItem(position).getName());

            return row;
        }

        public List<Player> getPlayers() {
            return mPlayers;
        }
    }

    @OnClick(R.id.addPlayerButon)
    protected void addPlayer(){
        final AddPlayerDialog dialog = new AddPlayerDialog(getActivity(), PlayersManager.getPlayersNamesFromPlayersList(mPlayersAdapter.getPlayers()));
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                String playerName = dialog.getPlayerName();
                if(playerName != null){
                    Player player = new Player(playerName);
                    mPlayersAdapter.add(player);
                    mPlayersSpinner.setSelection(mPlayersAdapter.getCount() - 1);
                    PathRecallApp.get(getActivity()).getPreferences().storePlayers(mPlayersAdapter.getPlayers());
                }
            }
        });
        dialog.show();
    }
}
