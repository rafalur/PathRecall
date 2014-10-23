package com.rafal.pathrecall;

import java.util.ArrayList;
import java.util.List;

public class GameSession {
    public enum GameState{
        IDLE,
        PLAYING_PATH,
        USER_DRAW,
        REPLAY_VERIFY
    }

    public GameSession(){
        mState = GameState.IDLE;
    }

    private GameState mState;
    private List<GameStatusListener> mGameStatusListeners;

    public GameState getState() {
        return mState;
    }

    public void setState(GameState state) {
        notifyStatusListeners(mState, state);
        this.mState = state;
    }

    private void notifyStatusListeners(GameState newState, GameState oldState) {
        for(GameStatusListener listener : mGameStatusListeners){
            listener.onGameStateChanged(newState, oldState);
        }
    }

    public void addGameStatusListener(GameStatusListener listener){
        if(mGameStatusListeners == null)
            mGameStatusListeners = new ArrayList<GameStatusListener>();
        mGameStatusListeners.add(listener);
    }

    public interface GameStatusListener{
        public void onGameStateChanged(GameState newState, GameState oldState);
    }
}
