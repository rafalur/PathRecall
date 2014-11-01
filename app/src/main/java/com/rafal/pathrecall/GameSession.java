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

    }

    private GameState mState;
    private List<GameSessionStatusListener> mGameStatusListeners;

    public void init(){
        setState(GameState.IDLE);
    }

    public GameState getState() {
        return mState;
    }

    public void setState(GameState state) {
        if(state != mState){
            this.mState = state;
            notifyStatusListeners(mState, state);
        }
    }

    private void notifyStatusListeners(GameState newState, GameState oldState) {
        if(mGameStatusListeners != null) {
            for (GameSessionStatusListener listener : mGameStatusListeners) {
                listener.onGameStateChanged(newState, oldState);
            }
        }
    }

    public void addGameStatusListener(GameSessionStatusListener listener){
        if(mGameStatusListeners == null)
            mGameStatusListeners = new ArrayList<GameSessionStatusListener>();
        mGameStatusListeners.add(listener);
    }

    public interface GameSessionStatusListener {
        public void onGameStateChanged(GameState newState, GameState oldState);
    }
}
