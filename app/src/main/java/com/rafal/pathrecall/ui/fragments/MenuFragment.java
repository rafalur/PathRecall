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
import com.rafal.pathrecall.engine.difficulty.Difficulty;
import com.rafal.pathrecall.ui.adapters.DifficultyAdapter;
import com.rafal.pathrecall.ui.adapters.PlayersAdapter;
import com.rafal.pathrecall.ui.dialogs.AddPlayerDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MenuFragment extends BaseFragment {

    @InjectView(R.id.menuPlayersSpinner)
    Spinner mPlayersSpinner;
    @InjectView(R.id.difficultySpinner)
    Spinner mDifficultySpinner;

    private PlayersAdapter mPlayersAdapter;
    private DifficultyAdapter mDifficultyAdapter;

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
        mPlayersAdapter = new PlayersAdapter(getActivity(), players);
        mPlayersSpinner.setAdapter(mPlayersAdapter);

        String defaultPlayer = PathRecallApp.get(getActivity()).getPreferences().getDefaultPlayer();
        if(defaultPlayer != null) {
            mPlayersSpinner.setSelection(mPlayersAdapter.getIndexOfPlayer(defaultPlayer));
        }
        mDifficultyAdapter = new DifficultyAdapter(getActivity());
        mDifficultySpinner.setAdapter(mDifficultyAdapter);
    }

    @OnClick({ R.id.menuStartGameButton, R.id.menuAboutButton, R.id.menuExitButton })
    protected void handleClicks(Button button) {
        switch (button.getId()) {
            case R.id.menuStartGameButton:
                startGame();
                break;
            case R.id.menuAboutButton:
                getNavigationManager().switchToAboutFragment();
                break;
            case R.id.menuExitButton:
                getActivity().finish();
                break;
        }
    }

    private void startGame() {
        if(mPlayersAdapter.getCount() < 1){
            final AddPlayerDialog dialog = new AddPlayerDialog(getActivity(), PlayersManager.getPlayersNamesFromPlayersList(mPlayersAdapter.getPlayers()));
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    String playerName = dialog.getPlayerName();
                    if(playerName != null && !playerName.isEmpty()) {
                        addPlayer(playerName);
                        startGame();
                    }
                }
            });
            dialog.show();
            return;
        }
        String currentPlayer = mPlayersAdapter.getItem(mPlayersSpinner.getSelectedItemPosition()).getName();
        Difficulty difficulty = mDifficultyAdapter.getItem(mDifficultySpinner.getSelectedItemPosition());
        PathRecallApp.get(getActivity()).getPreferences().storeDefaultPlayer(currentPlayer);
        getNavigationManager().switchToGameBoardFragment(currentPlayer, difficulty);
    }

    private void addPlayer(String playerName) {
        if(playerName != null){
            Player player = new Player(playerName);
            mPlayersAdapter.add(player);
            mPlayersSpinner.setSelection(mPlayersAdapter.getIndexOfPlayer(playerName));
            PathRecallApp.get(getActivity()).getPreferences().storePlayers(mPlayersAdapter.getPlayers());
        }
    }

    @OnClick(R.id.addPlayerButon)
    protected void addPlayer(){
        final AddPlayerDialog dialog = new AddPlayerDialog(getActivity(), PlayersManager.getPlayersNamesFromPlayersList(mPlayersAdapter.getPlayers()));
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                String playerName = dialog.getPlayerName();
                addPlayer(playerName);
            }
        });
        dialog.show();
    }
}
